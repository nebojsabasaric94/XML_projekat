package com.xml.bank;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xml.client.BankClient;
import com.xml.encryption.KeyStoreReader;
import com.xml.encryption.XMLEncryptionUtility;
import com.xml.encryption.XMLSigningUtility;
import com.xml.firm.FirmService;
import com.xml.firm.Firma;
import com.xml.mt102.GetMt910RequestMt102;
import com.xml.mt102.Mt102;
import com.xml.mt102.Mt102Service;
import com.xml.mt102.NalogZaMT102;
import com.xml.mt102.NalogZaMT102Service;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeResponse;
import com.xml.nalogzaplacanje.NalogZaPlacanje;
import com.xml.nalogzaplacanje.NalogZaPlacanjeService;
import com.xml.strukturartgsnaloga.GetMt910Request;
import com.xml.strukturartgsnaloga.GetMt910Response;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaRequest;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaResponse;
import com.xml.strukturartgsnaloga.Mt900Service;
import com.xml.strukturartgsnaloga.Mt910Service;
import com.xml.strukturartgsnaloga.ObjectFactory;
import com.xml.strukturartgsnaloga.StrukturaRtgsNaloga;
import com.xml.zahtevzadobijanjeizvoda.GetZahtevRequest;
import com.xml.zahtevzadobijanjeizvoda.GetZahtevResponse;
import com.xml.zahtevzadobijanjeizvoda.Presek;
import com.xml.zahtevzadobijanjeizvoda.PresekAlati;
import com.xml.zahtevzadobijanjeizvoda.StavkaPreseka;
import com.xml.zahtevzadobijanjeizvoda.ZaglavljePreseka;



@Endpoint
public class BankEndpoint {
	
	@Autowired
	private BankClient bankClient;

	@Autowired
	private FirmService firmService;
	
	@Autowired
	private Mt102Service mt102Service;
	
	@Autowired
	private Mt900Service mt900Service;
	
	@Autowired
	private Mt910Service mt910Service;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private NalogZaPlacanjeService nalogZaPlacanjeService;
		
	@Autowired
	private NalogZaMT102Service nalogZaMt102Service;
	
	@Autowired
	private com.xml.mt102.Mt910Service mt910ServiceMt102;
	
	private static final int BR_STAVKI_PO_PRESEKU = 3;
	
	private static final String NAMESPACE_URI = "http://strukturaRtgsNaloga.xml.com";

	private static final String NAMESPACE_URI1 = "http://nalogZaPlacanje.xml.com";

	
	private static final String NAMESPACE_URI2 = "http://mt102.xml.com";
	//private static final String NAMESPACE_URI3 = "http://zahtevzadobijanjeizvoda.xml.com";
	
	private static final String NAMESPACE_URI3 = "http://zahtevzadobijanjeizvoda.xml.com";

	@PayloadRoot(namespace = NAMESPACE_URI1, localPart = "getNalogZaPlacanjeRequest")
	@XmlAnyElement
	@ResponsePayload
	public GetNalogZaPlacanjeResponse getNalogZaPlacanje(@RequestPayload Element request) {
		Document doc = null;
		if(checkSignature(request))
			doc = decrypt(request);
		//----------------------------------gotov deo za desifrovanje-------------
		NalogZaPlacanje nalogZaPlacanje = getObjectFromXMLDoc(doc);
		//------------------------------------------------------------------------
		String oznakaBankeDuznika = nalogZaPlacanje.getRacunDuznika().substring(0, 3);
		String oznakaBankePoverioca = nalogZaPlacanje.getRacunPoverioca().substring(0,3);
		Firma duznik = firmService.findByAccount(nalogZaPlacanje.getRacunDuznika());
		Firma poverilac = firmService.findByAccount(nalogZaPlacanje.getRacunPoverioca());

		if(oznakaBankeDuznika.equals(oznakaBankePoverioca)){
		//u istoj su banci, samo prebaci novac sa racuna na racun
			
			duznik.setStanjeRacuna(duznik.getStanjeRacuna() - nalogZaPlacanje.getIznos().intValue());
			firmService.save(duznik);
			poverilac.setStanjeRacuna(poverilac.getStanjeRacuna()+ nalogZaPlacanje.getIznos().intValue());
			firmService.save(poverilac);
			GetNalogZaPlacanjeResponse responseNZP = new GetNalogZaPlacanjeResponse();
			responseNZP.setNalogZaPlacanje(nalogZaPlacanje);
			return responseNZP;			
		}
		else if(nalogZaPlacanje.isHitno() || nalogZaPlacanje.getIznos().intValue() > 250000){
			//RTGS
			ObjectFactory factory = new ObjectFactory();
			GetStrukturaRtgsNalogaRequest rtgs = factory.createGetStrukturaRtgsNalogaRequest();
			
			StrukturaRtgsNaloga strukturaRtgsNaloga = factory.createStrukturaRtgsNaloga();
			strukturaRtgsNaloga.setDatumNaloga(nalogZaPlacanje.getDatumNaloga());
			strukturaRtgsNaloga.setDatumValute(nalogZaPlacanje.getDatumValute());
			strukturaRtgsNaloga.setDuznikNalogodavac(nalogZaPlacanje.getDuznikNalogodavac());
			strukturaRtgsNaloga.setIznos(nalogZaPlacanje.getIznos());
			strukturaRtgsNaloga.setModelOdobrenja(nalogZaPlacanje.getModelOdobrenja());
			strukturaRtgsNaloga.setModelZaduzenja(nalogZaPlacanje.getModelZaduzenja());
			strukturaRtgsNaloga.setObracunskiRacunBankeDuznika(duznik.getBank().getObracunskiRacunBanke());
			strukturaRtgsNaloga.setObracunskiRacunBankePoverioca(poverilac.getBank().getObracunskiRacunBanke());
			strukturaRtgsNaloga.setPozivNaBrojOdobrenja(nalogZaPlacanje.getPozivNaBrojOdobrenja());
			strukturaRtgsNaloga.setPozivNaBrojZaduzenja(nalogZaPlacanje.getPozivNaBrojZaduzenja());
			strukturaRtgsNaloga.setPrimalacPoverilac(nalogZaPlacanje.getPrimalacPoverilac());
			strukturaRtgsNaloga.setRacunDuznika(nalogZaPlacanje.getRacunDuznika());
			strukturaRtgsNaloga.setRacunPoverioca(nalogZaPlacanje.getRacunPoverioca());
			strukturaRtgsNaloga.setSifraValute(nalogZaPlacanje.getOznakaValute());
			strukturaRtgsNaloga.setSvrhaPlacanja(nalogZaPlacanje.getSvrhaPlacanja());
			strukturaRtgsNaloga.setSwiftKodBankeDuznika(duznik.getBank().getSwiftKodBanke());
			strukturaRtgsNaloga.setSwiftKodBankePoverioca(poverilac.getBank().getSwiftKodBanke());
			strukturaRtgsNaloga.setIdPoruke("MT103");
			
			rtgs.setStrukturaRtgsNaloga(strukturaRtgsNaloga);
			boolean validate = validateGetStrukturaRtgsNalogaRequest(rtgs);
			if(validate){
				GetStrukturaRtgsNalogaResponse response = bankClient.sendMt103ToNationalBank(rtgs);
				if(response.getMt900().getIdPoruke().equals("MT900")){
					duznik.setStanjeRacuna(duznik.getStanjeRacuna()-response.getMt900().getIznos().intValue());
					firmService.save(duznik);
					Bank bankaDuznika = bankService.findByObracunskiRacunBanke(response.getMt900().getObracunskiRacunBankeDuznika());
					response.getMt900().setBankaDuznika(bankaDuznika);
					mt900Service.save(response.getMt900());
					GetNalogZaPlacanjeResponse responseNZP = new GetNalogZaPlacanjeResponse();
					responseNZP.setNalogZaPlacanje(nalogZaPlacanje);
					return responseNZP;
				}
			}
		}
		else{
			//MT102
			Mt102 mt102 = mt102Service.checkBankAccount(duznik.getBank().getObracunskiRacunBanke(), poverilac.getBank().getObracunskiRacunBanke(),false);
			if(mt102 == null){
				mt102 = new Mt102();
				mt102.setIdPoruke("MT102");
				mt102.setSwiftKodBankeDuznika(duznik.getBank().getSwiftKodBanke());
				mt102.setSWIFTKodBankePoverioca(poverilac.getBank().getSwiftKodBanke());
				mt102.setObracunskiRacunBankePoverioca(poverilac.getBank().getObracunskiRacunBanke());
				mt102.setObracunskiRacunBankeDuznika(duznik.getBank().getObracunskiRacunBanke());
				mt102.setUkupanIznos(nalogZaPlacanje.getIznos());
				mt102.setDatum(new Date());
				mt102.setDatumValute(new Date());
				mt102.setSifraValute("RSD");
				mt102.setBanka(duznik.getBank());
				mt102.setObradjen(false);
				mt102.setBankaPoverioca(poverilac.getBank());
				NalogZaMT102 nalog = new NalogZaMT102();
				nalog.setIdNalogaZaPlacanje("idNaloga");
				nalog.setDuznikNalogodavac(nalogZaPlacanje.getDuznikNalogodavac());
				nalog.setSvrhaPlacanja(nalogZaPlacanje.getSvrhaPlacanja());
				nalog.setPrimalacPoverilac(nalogZaPlacanje.getPrimalacPoverilac());
				nalog.setDatumNaloga(nalogZaPlacanje.getDatumNaloga());
				nalog.setRacunDuznika(nalogZaPlacanje.getRacunDuznika());
				nalog.setModelZaduzenja(nalogZaPlacanje.getModelZaduzenja());
				nalog.setPozivNaBrojZaduzenja(nalogZaPlacanje.getPozivNaBrojZaduzenja());
				nalog.setRacunPoverioca(nalogZaPlacanje.getRacunPoverioca());
				nalog.setModelOdobrenja(nalogZaPlacanje.getModelOdobrenja());
				nalog.setPozivNaBrojOdobrenja(nalogZaPlacanje.getPozivNaBrojOdobrenja());
				nalog.setIznos(nalogZaPlacanje.getIznos());
				nalog.setSifraValute("SRB");
				
				mt102 = mt102Service.save(mt102);
				nalog.setMt102(mt102);
				nalogZaMt102Service.save(nalog);

			} else {
				//Mt102 mt102 = new Mt102();
				NalogZaMT102 nalog = new NalogZaMT102();
				nalog.setIdNalogaZaPlacanje("idNaloga");
				nalog.setDuznikNalogodavac(nalogZaPlacanje.getDuznikNalogodavac());
				nalog.setSvrhaPlacanja(nalogZaPlacanje.getSvrhaPlacanja());
				nalog.setPrimalacPoverilac(nalogZaPlacanje.getPrimalacPoverilac());
				nalog.setDatumNaloga(nalogZaPlacanje.getDatumNaloga());
				nalog.setRacunDuznika(nalogZaPlacanje.getRacunDuznika());
				nalog.setModelZaduzenja(nalogZaPlacanje.getModelZaduzenja());
				nalog.setPozivNaBrojZaduzenja(nalogZaPlacanje.getPozivNaBrojZaduzenja());
				nalog.setRacunPoverioca(nalogZaPlacanje.getRacunPoverioca());
				nalog.setModelOdobrenja(nalogZaPlacanje.getModelOdobrenja());
				nalog.setPozivNaBrojOdobrenja(nalogZaPlacanje.getPozivNaBrojOdobrenja());
				nalog.setIznos(nalogZaPlacanje.getIznos());
				nalog.setSifraValute("SRB");
				//mt102.getNalogZaMT102().add(nalog);
				//mt102 = mt102Service.save(mt102);
				nalog.setMt102(mt102);
				
				nalogZaMt102Service.save(nalog);
				//mt102.setNalogZaMT102(nalogZaMT102);
				//mt102Service.save(mt102);
				 
				 
			}
			GetNalogZaPlacanjeResponse responseNZP = new GetNalogZaPlacanjeResponse();
			responseNZP.setNalogZaPlacanje(nalogZaPlacanje);
			return responseNZP;			
		}

		
		return null;
	}
	
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMt910Request")//za rtgs tj mt103
	@ResponsePayload
	public GetMt910Response getMt910(@RequestPayload Element request){
		//GetMt910Request
		Document doc = null;
		if(checkSignature(request))
			doc = decrypt(request);
		//----------------------------------gotov deo za desifrovanje-------------
		GetMt910Request getMt910Request = getMt910FromXMLDoc(doc);
		
		boolean validateGetMt910Request = validateGetMt910Request(getMt910Request);
		if(validateGetMt910Request){
			Firma poverilac = firmService.findByAccount(getMt910Request.getRtgsNalog().getRacunPoverioca());
			poverilac.setStanjeRacuna(poverilac.getStanjeRacuna()+ getMt910Request.getRtgsNalog().getIznos().intValue());
			firmService.save(poverilac);
			GetMt910Response response = new GetMt910Response();
			getMt910Request.getMt910().setBankaPoverioca(bankService.findByObracunskiRacunBanke(getMt910Request.getMt910().getObracunskiRacunBankePoverioca()));
			mt910Service.save(getMt910Request.getMt910());
			response.setStatus("success");
			return response;
		}
		return null;
	}
	
	



	@PayloadRoot(namespace = NAMESPACE_URI2, localPart = "getMt910RequestMt102")//za mt102
	@ResponsePayload
	public com.xml.mt102.GetMt910Response getMt910mt102(@RequestPayload Element request){
		//GetMt910RequestMt102
		Document doc = null;
		if(checkSignature(request))
			doc = decrypt(request);
		//----------------------------------gotov deo za desifrovanje-------------
		GetMt910RequestMt102 getMt910Request = getMt910RequestMt102FromXMLDoc(doc);
		boolean validateGetMt910RequestMt102 = validateGetMt910RequestMt102(getMt910Request);
		if(validateGetMt910RequestMt102){
			String racunPoverioca = null;
			for(int i =0; i < getMt910Request.getMt102().getNalogZaMT102().size();i++){
				racunPoverioca = getMt910Request.getMt102().getNalogZaMT102().get(i).getRacunPoverioca();
				Firma poverilac = firmService.findByAccount(racunPoverioca);
				poverilac.setStanjeRacuna(poverilac.getStanjeRacuna()+ getMt910Request.getMt102().getNalogZaMT102().get(i).getIznos().intValue());
				firmService.save(poverilac);
			}
			com.xml.mt102.GetMt910Response response = new com.xml.mt102.GetMt910Response();
			getMt910Request.getMt910().setBankaPoverioca(bankService.findByObracunskiRacunBanke(getMt910Request.getMt910().getObracunskiRacunBankePoverioca()));
			mt910ServiceMt102.save(getMt910Request.getMt910());
			response.setStatus("success");
			return response;
		}
		return null;
	}
	
	



	@PayloadRoot(namespace = NAMESPACE_URI3, localPart = "getZahtevRequest")
	@ResponsePayload
	public GetZahtevResponse getZahtev(@RequestPayload GetZahtevRequest getZahtevRequest){
		
		Presek presek = new Presek();
		
		System.out.println("--------ZAHTEV PRIMLJEN----------");
		String brojRacuna = getZahtevRequest.getZahtevZaDobijanjeIzvoda().getBrojRacuna();
		List<NalogZaPlacanje> nalozi = nalogZaPlacanjeService.findForIzvod(
				getZahtevRequest.getZahtevZaDobijanjeIzvoda().getDatum(),
				brojRacuna,
				BR_STAVKI_PO_PRESEKU, 
				getZahtevRequest.getZahtevZaDobijanjeIzvoda().getRedniBrojPreseka().intValue()
				);
		ZaglavljePreseka zaglavljePreseka = new ZaglavljePreseka();
		zaglavljePreseka.setBrojRacuna(brojRacuna);
		zaglavljePreseka.setDatumNaloga(getZahtevRequest.getZahtevZaDobijanjeIzvoda().getDatum());
		zaglavljePreseka.setBrojPreseka(getZahtevRequest.getZahtevZaDobijanjeIzvoda().getRedniBrojPreseka().intValue());
		zaglavljePreseka.setPrethodnoStanje(new BigDecimal(100));	//TODO
		zaglavljePreseka.setBrojPromenaUKorist(PresekAlati.IzracunajBrojPromenaUKorist(nalozi, brojRacuna));
		zaglavljePreseka.setUkupnoUKorist(PresekAlati.IzracunajUkupnoUKorist(nalozi, brojRacuna));
		zaglavljePreseka.setBrojPromenaNaTeret(PresekAlati.IzracunajBrojPromenaNaTeret(nalozi, brojRacuna));
		zaglavljePreseka.setUkupnoNaTeret(PresekAlati.IzracunajUkupnoNaTeret(nalozi, brojRacuna));
		zaglavljePreseka.setNovoStanje(new BigDecimal(100));//TODO
		
		presek.setZaglavlje(zaglavljePreseka);
		
		
		for (NalogZaPlacanje n : nalozi) {
			StavkaPreseka stavkaPreseka = new StavkaPreseka();
			stavkaPreseka.setDuznikNalogodavac(n.getDuznikNalogodavac());
			stavkaPreseka.setSvrhaPlacanja(n.getSvrhaPlacanja());
			stavkaPreseka.setPrimalacPoverilac(n.getPrimalacPoverilac());
			stavkaPreseka.setDatumNaloga(n.getDatumNaloga());
			stavkaPreseka.setDatumValute(n.getDatumValute());
			stavkaPreseka.setRacunDuznika(n.getRacunDuznika());
			stavkaPreseka.setModelZaduzenja(n.getModelZaduzenja());
			stavkaPreseka.setPozivNaBrojZaduzenja(n.getPozivNaBrojZaduzenja());
			stavkaPreseka.setRacunPoverioca(n.getRacunPoverioca());
			stavkaPreseka.setModelOdobrenja(n.getModelOdobrenja());
			stavkaPreseka.setPozivNaBrojOdobrenja(n.getPozivNaBrojOdobrenja());
			stavkaPreseka.setIznos(n.getIznos());
			stavkaPreseka.setSmer(PresekAlati.izracunajSmer(n, brojRacuna));
			presek.getStavka().add(stavkaPreseka);
		}
		
		GetZahtevResponse getZahtevResponse = new GetZahtevResponse();
		
		getZahtevResponse.setPresek(presek);
		
		return getZahtevResponse;
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
		/*DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
		Document document1 = db.newDocument();
		NodeList nodeList = document.getElementsByTagNameNS("*", "nalogZaPlacanje");
		document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
		saveDocument(document1,"nalog_encrypted.xml");*/
		
		XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
        KeyStoreReader ksReader = new KeyStoreReader();
		PrivateKey privateKey = ksReader.readPrivateKey("ksBanks\\Banka A.jks", "123", "ba1", "123");
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
	
	public static GetMt910Request getMt910FromXMLDoc(Document document){
		try{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
		Document document1 = db.newDocument();
		NodeList nodeList = document.getElementsByTagNameNS("*", "getMt910Request");
		document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
		JAXBContext context = JAXBContext.newInstance(GetMt910Request.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		GetMt910Request nzp = (GetMt910Request) unmarshaller.unmarshal(document1);
		System.out.println("----UNMARSHALED MT910----\n ");
		return nzp;
		}catch(Exception tt)
		{
			tt.printStackTrace();
			return null;
		}
	}
	
	public static GetMt910RequestMt102 getMt910RequestMt102FromXMLDoc(Document document){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
			Document document1 = db.newDocument();
			NodeList nodeList = document.getElementsByTagNameNS("*", "getMt910RequestMt102");
			document1.appendChild(document1.adoptNode(nodeList.item(0).cloneNode(true)));
			JAXBContext context = JAXBContext.newInstance(GetMt910RequestMt102.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetMt910RequestMt102 nzp = (GetMt910RequestMt102) unmarshaller.unmarshal(document1);
			System.out.println("----! UNMARSHALED MT910mt102 !----\n ");
			return nzp;
			}catch(Exception tt)
			{
				tt.printStackTrace();
				return null;
			}
	}
	
	private boolean validateGetStrukturaRtgsNalogaRequest(GetStrukturaRtgsNalogaRequest rtgs) {

		File file = new File("getStrukturaRtgsNalogaRequest.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetStrukturaRtgsNalogaRequest.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(rtgs, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("classpath:strukturaRtgsNaloga.xsd"));
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("getStrukturaRtgsNalogaRequest.xml"));
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
					
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	
	private boolean validateGetMt910Request(GetMt910Request getMt910Request) {

		File file = new File("getMt910Request.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetMt910Request.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(getMt910Request, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("classpath:strukturaRtgsNaloga.xsd"));
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("getMt910Request.xml"));
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
					
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	private boolean validateGetMt910RequestMt102(GetMt910RequestMt102 getMt910Request) {


		File file = new File("getMt910RequestMt102.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetMt910RequestMt102.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(getMt910Request, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("classpath:mt102.xsd"));
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("getMt910RequestMt102.xml"));
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
					
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	
	}

	
	
	
	/*
	 * Postoji samo radi testiranja enkripcije
	 */
/*	private void saveDocument(Document doc, String fileName) {
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
	}*/
	
	
}
