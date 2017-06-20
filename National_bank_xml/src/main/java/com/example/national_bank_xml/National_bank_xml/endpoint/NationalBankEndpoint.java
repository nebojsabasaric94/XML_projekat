package com.example.national_bank_xml.National_bank_xml.endpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.xml.bind.Marshaller;
import javax.crypto.SecretKey;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.support.MarshallingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.bankxml.bankxml.mt102.GetMt102Request;
import com.example.bankxml.bankxml.mt102.GetMt102Response;
import com.example.bankxml.bankxml.mt102.GetMt910RequestMt102;
import com.example.bankxml.bankxml.mt102.Mt102;
import com.example.national_bank_xml.National_bank_xml.bank.Bank;
import com.example.national_bank_xml.National_bank_xml.bank.BankService;
import com.example.national_bank_xml.National_bank_xml.client.NationalBankClient;
import com.nalogzaplacanje.NalogZaPlacanje;
import com.strukturartgsnaloga.GetMt910Request;
import com.strukturartgsnaloga.GetMt910Response;
import com.strukturartgsnaloga.GetStrukturaRtgsNalogaResponse;
import com.strukturartgsnaloga.Mt900;
import com.strukturartgsnaloga.Mt910;
import com.strukturartgsnaloga.ObjectFactory;
import com.strukturartgsnaloga.StrukturaRtgsNaloga;

import encryption.KeyStoreReader;
import encryption.XMLEncryptionUtility;
import encryption.XMLSigningUtility;


@Endpoint
public class NationalBankEndpoint {

	
	
	private static final String NAMESPACE_URI1 = "http://nalogZaPlacanje.com";
	private static final String NAMESPACE_URI = "http://strukturaRtgsNaloga.com";
	private static final String NAMESPACE_URI2 = "http://mt102.BankXml.bankXml.example.com";
	
	
	@Autowired
	private NationalBankClient client;
	
	@Autowired
	private BankService bankService;
	
	/*@PayloadRoot(namespace = NAMESPACE_URI1, localPart = "getNalogZaPlacanjeRequest")
	@XmlAnyElement
	@ResponsePayload
	public GetNalogZaPlacanjeResponse getNalogZaPlacanje(@RequestPayload Element request) {
		Document doc = null;
		if(checkSignature(request))
			doc = decrypt(request);
		//----------------------------------gotov deo za desifrovanje-------------
		NalogZaPlacanje nalogZaPlacanje = getObjectFromXMLDoc(doc);
		//------------------------------------------------------------------------
		GetNalogZaPlacanjeResponse response = new GetNalogZaPlacanjeResponse();
		System.out.println("usao narodna banka");
		//response.setCountry(countryRepository.findCountry(request.getName()));
		//bankClient.sendToNationalBank(response.getNalogZaPlacanje());
		return response;
	}*/
	
	
	
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStrukturaRtgsNalogaRequest")
	@XmlAnyElement
	@ResponsePayload
	public GetStrukturaRtgsNalogaResponse getStrukturaRtgsNaloga(@RequestPayload Element request) {
		//getstrukturartgsnalogarequest
		Document doc = request.getOwnerDocument();
		saveDocument(doc, "PRISTIGAO_RTGS.XML");
		if(checkSignature(request))
			doc = decrypt(request);
		StrukturaRtgsNaloga rtgsNalog = getStrukturaRtgsNalogaFromXMLDoc(doc);
		
		
		//StrukturaRtgsNaloga rtgsNalog = request.getStrukturaRtgsNaloga();
		Bank bankaDuznika = bankService.findBySwiftCode(rtgsNalog.getSwiftKodBankeDuznika());
		Bank bankaPoverioca = bankService.findBySwiftCode(rtgsNalog.getSwiftKodBankePoverioca());
		bankaPoverioca.setStanjeRacunaBanke(bankaPoverioca.getStanjeRacunaBanke() + rtgsNalog.getIznos().intValue());
		bankaDuznika.setStanjeRacunaBanke(bankaDuznika.getStanjeRacunaBanke()-rtgsNalog.getIznos().intValue());
		System.out.println("Usao rtgs");
		ObjectFactory factory = new ObjectFactory();
		GetMt910Request mt910Request = factory.createGetMt910Request();
		
		Mt910 mt910 = factory.createMt910();
		mt910.setDatumValute(null);
		mt910.setIdPoruke("MT910");
		mt910.setIdPorukeNaloga("Nalog za prenos");
		mt910.setIznos(rtgsNalog.getIznos());
		mt910.setObracunskiRacunBankePoverioca(rtgsNalog.getObracunskiRacunBankePoverioca());
		mt910.setSifraValute(rtgsNalog.getSifraValute());
		mt910.setSwiftKodBankePoverioca(rtgsNalog.getSwiftKodBankePoverioca());
		
		mt910Request.setMt910(mt910);
		mt910Request.setRtgsNalog(rtgsNalog);
		GetMt910Response mt910Response = client.sendMt910(mt910Request);
		
		Mt900 mt900 = new Mt900();
		mt900.setDatumValute(null);
		mt900.setIdPoruke("MT900");
		mt900.setIdPorukeNaloga("Nalog za prenos");
		mt900.setIznos(rtgsNalog.getIznos());
		mt900.setObracunskiRacunBankeDuznika(bankaDuznika.getObracunskiRacunBanke());
		mt900.setSifraValute("RSD");
		mt900.setSwiftBankeDuznika(bankaDuznika.getSwiftKodBanke());
		
		
		
		GetStrukturaRtgsNalogaResponse response = new GetStrukturaRtgsNalogaResponse();
		response.setMt900(mt900);
		
//		JAXBContext jaxbMarshaller = null;
			//try {
			//	jaxbMarshaller = JAXBContext.newInstance(GetStrukturaRtgsNalogaResponse.class);
			//} catch (JAXBException e) {
			///	// TODO Auto-generated catch block
		//	/	e.printStackTrace();
//			}
			
		
			////////////
			File file = new File("RESPONSEfile.xml");
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(GetStrukturaRtgsNalogaResponse.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				jaxbMarshaller.marshal(response, file);
				jaxbMarshaller.marshal(response, System.out);
				
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Document document = loadDocument("RESPONSEfile.xml");

			
			KeyStoreReader ksReader = new KeyStoreReader();
			XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
			XMLSigningUtility sigUtility = new XMLSigningUtility();
			
			SecretKey secretKey = encUtility.generateDataEncryptionKey();
			System.out.println("\n===== Generisan kljuc =====");
			System.out.println(Base64.encode(secretKey.getEncoded()));
			
			//Ucitava sertifikat za sifrovanje tajnog kljuca
			Certificate cert = ksReader.readCertificate("primer.jks", "primer", "primer");
			//Sifruje se dokument
			System.out.println("\n===== Sifrovanje XML dokumenta =====");
			document = encUtility.encrypt(document, secretKey, cert);
			//Snima se XML dokument, koji sadrzi tajni kljuc
			saveDocument(document, file.getPath());
			
			System.out.println("\n===== Potpisivanje XML dokumenta =====");
			PrivateKey privateKey = ksReader.readPrivateKey("primer.jks", "primer", "primer", "primer");
			document = sigUtility.signDocument(document, privateKey, cert);
			saveDocument(document, "RESPONSEsignedFile.xml");
			
		return response;
		
	}	
	
	@PayloadRoot(namespace = NAMESPACE_URI2, localPart = "getMt102Request")
	@XmlAnyElement
	@ResponsePayload
	public GetMt102Response getMt102(@RequestPayload Element request) {
		////GetMt102Request
		Document doc = request.getOwnerDocument();
		saveDocument(doc, "PRISTIGAO_MT102.xml");
		if(checkSignature(request))
			doc = decrypt(request);
		Mt102 mt102 = getMt102FromXMLDoc(doc);
		
		
		com.example.bankxml.bankxml.mt102.ObjectFactory factory = new com.example.bankxml.bankxml.mt102.ObjectFactory();
		com.example.bankxml.bankxml.mt102.Mt910 mt910 = factory.createMt910();
		GetMt910RequestMt102 mt910Request = factory.createGetMt910RequestMt102();
		System.out.println("Usao MT102");

		mt910.setDatumValute(null);
		mt910.setIdPoruke("MT910");
		mt910.setIdPorukeNaloga("Nalog za prenos");
		mt910.setIznos(mt102.getUkupanIznos());
		mt910.setObracunskiRacunBankePoverioca(mt102.getObracunskiRacunBankePoverioca());
		mt910.setSifraValute(mt102.getSifraValute());
		mt910.setSwiftKodBankePoverioca(mt102.getSWIFTKodBankePoverioca());
		
		mt910Request.setMt910(mt910);
		mt910Request.setMt102(mt102);
		com.example.bankxml.bankxml.mt102.GetMt910Response mt910response = client.sendMt910mt102(mt910Request);
		/////////////////////
		com.example.bankxml.bankxml.mt102.Mt900 mt900 = new com.example.bankxml.bankxml.mt102.Mt900();
		mt900.setDatumValute(null);
		mt900.setIdPoruke("MT900");
		mt900.setIdPorukeNaloga("Nalog za prenos");
		mt900.setIznos(mt102.getUkupanIznos());
		mt900.setObracunskiRacunBankeDuznika(mt102.getObracunskiRacunBankeDuznika());
		mt900.setSifraValute(mt102.getSifraValute());
		mt900.setSwiftBankeDuznika(mt102.getSwiftKodBankeDuznika());
		GetMt102Response response = new GetMt102Response();
		response.setMt900(mt900);
	
		
		return response;
	}
	
	
	
	
	
	
	public boolean checkSignature(Element request){
		Document doc = request.getOwnerDocument();
		XMLSigningUtility sigUtility = new XMLSigningUtility();
		boolean res = sigUtility.verifySignature(doc);
		System.out.println("signature ok: "+res);
		return res;
	}
	
	
	
	public Document decrypt(Element request){
		try{
		Document document = request.getOwnerDocument();
		XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
        KeyStoreReader ksReader = new KeyStoreReader();
		PrivateKey privateKey = ksReader.readPrivateKey("ksCentralBank\\Narodna banka.jks", "123", "nbs1", "123");
		document = encUtility.decrypt(document, privateKey);
		return document;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static NalogZaPlacanje getObjectFromXMLDoc(Document document){
		try{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
		Document document1 = db.newDocument();
		NodeList nodeList = document.getElementsByTagNameNS("*", "nalogZaPlacanje");
		document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
		JAXBContext context = JAXBContext.newInstance(NalogZaPlacanje.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		NalogZaPlacanje nzp = (NalogZaPlacanje) unmarshaller.unmarshal(document1);
		System.out.println("----UNMARSHALED----\n "+nzp.getIdPoruke());
		return nzp;
		}catch(Exception tt)
		{
			tt.printStackTrace();
			return null;
		}
	}
	
	public static StrukturaRtgsNaloga getStrukturaRtgsNalogaFromXMLDoc(Document document){
		try{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
		Document document1 = db.newDocument();
		NodeList nodeList = document.getElementsByTagNameNS("*", "strukturaRtgsNaloga");
		document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
		JAXBContext context = JAXBContext.newInstance(StrukturaRtgsNaloga.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StrukturaRtgsNaloga nzp = (StrukturaRtgsNaloga) unmarshaller.unmarshal(document1);
		System.out.println("----UNMARSHALED----\n "+nzp.getIdPoruke());
		return nzp;
		}catch(Exception tt)
		{
			tt.printStackTrace();
			return null;
		}
	}
	
	
	public static Mt102 getMt102FromXMLDoc(Document document){
		try{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
		Document document1 = db.newDocument();
		NodeList nodeList = document.getElementsByTagNameNS("*", "mt102");
		document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
		JAXBContext context = JAXBContext.newInstance(Mt102.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Mt102 nzp = (Mt102) unmarshaller.unmarshal(document1);
		System.out.println("----UNMARSHALED  MT102----\n "+nzp.getIdPoruke());
		return nzp;
		}catch(Exception tt)
		{
			tt.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Postoji samo radi testiranja enkripcije
	 */
	private void saveDocument(Document doc, String fileName) {
		try {
			File outFile = new File(fileName);
			FileOutputStream f = new FileOutputStream(outFile);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			f.close();
		} catch (Exception r){r.printStackTrace();}
	}
	
	public Document encryptit(Document doc, SecretKey key, Certificate certificate) {
		try {
		    //Sifra koja ce se koristiti za sifrovanje XML-a u ovom slucaju je 3DES
		    XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES);
		    //Inicijalizacija za kriptovanje
		    xmlCipher.init(XMLCipher.ENCRYPT_MODE, key);
		    
		    //Sadrzaj XMLa se sifruje tajnim kljucem, putem simetricne sifre (3DES)
		    //Tajni kljuc se potom sifruje javnim kljucem koji se preuzima sa sertifikata putem asimetricne sifre (RSA)
			XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_v1dot5);
		    //Inicijalizacija za kriptovanje tajnog kljuca javnim RSA kljucem
		    keyCipher.init(XMLCipher.WRAP_MODE, certificate.getPublicKey());
		    EncryptedKey encryptedKey = keyCipher.encryptKey(doc, key);
		    
		    //U EncryptedData elementa koji se sifruje kao KeyInfo stavljamo sifrovan tajni kljuc
		    EncryptedData encryptedData = xmlCipher.getEncryptedData();
	        //kreira se KeyInfo
		    KeyInfo keyInfo = new KeyInfo(doc);
		    keyInfo.addKeyName("Sifrovan tajni kljuc");
		    keyInfo.add(encryptedKey);
		    //postavljamo KeyInfo za element koji se sifruje
	        encryptedData.setKeyInfo(keyInfo);
			
			//Trazi se element ciji sadrzaj se sifruje
			NodeList odseci = doc.getElementsByTagName("getStrukturaRtgsNalogaResponse");
			Element odsek = (Element) odseci.item(0);
			
			xmlCipher.doFinal(doc, odsek, true); //Sifruje sa sadrzaj
			
			return doc;
		} catch (XMLEncryptionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
private Document loadDocument(String file) {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new File(file));

			return document;
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
}
