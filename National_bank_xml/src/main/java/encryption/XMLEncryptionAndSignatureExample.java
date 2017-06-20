package encryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Ova klasa isprobava algoritme implementirane od strane Apache Sanuario projekta
 * Ovaj projekat implementira XML Encryption & Signature standard, koji diktira
 * kako XML dokument treba da bude sifrovan i digitalno potpisan
 * @author Nikola
 *
 */
public class XMLEncryptionAndSignatureExample {
	
	public void testIt() {
		String originalFile = "./files/data/xml/univerzitet.xml";
		String encryptedFile = "./files/data/xml/univerzitet_encrypted.xml";
		String signedFile = "./files/data/xml/univerzitet_signed.xml";
		String endFile = "./files/data/xml/univerzitet_end.xml";
		String keyStoreFile = "./files/pki/keystores/primer.jks";
		
		KeyStoreReader ksReader = new KeyStoreReader();
		XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
		XMLSigningUtility sigUtility = new XMLSigningUtility();
		
		
		System.out.println("===== Primer izvrsavanja XML Encryption & Signature algoritma =====");
		//Ucitava se dokument
		Document doc = loadDocument(originalFile);
		//Generise se tajni kljuc
		SecretKey secretKey = encUtility.generateDataEncryptionKey();
		System.out.println("\n===== Generisan kljuc =====");
		//System.out.println(Base64Utility.encode(secretKey.getEncoded()));
		
		//Ucitava sertifikat za sifrovanje tajnog kljuca
		Certificate cert = ksReader.readCertificate(keyStoreFile, "primer", "primer");
		//Sifruje se dokument
		System.out.println("\n===== Sifrovanje XML dokumenta =====");
		doc = encUtility.encrypt(doc, secretKey, cert);
		//Snima se XML dokument, koji sadrzi tajni kljuc
		saveDocument(doc, encryptedFile);
		
		System.out.println("\n===== Potpisivanje XML dokumenta =====");
		PrivateKey privateKey = ksReader.readPrivateKey(keyStoreFile, "primer", "primer", "primer");
		doc = sigUtility.signDocument(doc, privateKey, cert);
		saveDocument(doc, signedFile);
		
		System.out.println("\n===== Transfer XML dokumenta od tacke A do tacke B =====");
		
		System.out.println("\n===== Provera validnosti digitalnog potpisa =====");
		boolean res = sigUtility.verifySignature(doc);
		if(res) {
			System.out.println("\n===== Potpis je validan, dokument se desifruje =====");
			doc = encUtility.decrypt(doc, privateKey);
			saveDocument(doc, endFile);
			System.out.println("\n===== Desifrovanje zavrseno, tacka B je primila dokument =====");
			System.out.println("===== Za pregled rezultujucih dokumenata otvoriti files/data/xml folder =====");
			//Ukoliko je proces uspesan garantovana je poverljivost i integritet poruke, kao i autentifikacija i neporecivost postupka slanja
		} else {
			System.out.println("\n===== Potpis nije validan, dokument se odbacuje =====");
		}
	}
	
	/**
	 * Kreira DOM od XML dokumenta
	 */
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
	
	/**
	 * Snima DOM u XML fajl 
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLEncryptionAndSignatureExample xml = new XMLEncryptionAndSignatureExample();
		xml.testIt();
	}

}
