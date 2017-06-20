package encryption;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.keys.KeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLEncryptionUtility {
    public XMLEncryptionUtility() {
        Security.addProvider(new BouncyCastleProvider());
        org.apache.xml.security.Init.init();
    }
	
	/**
	 * Generise tajni kljuc
	 */
	public SecretKey generateDataEncryptionKey() {
        try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede"); //Triple DES
			return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	/**
	 * Sifruje sadrzaj prvog elementa odsek
	 */
	public Document encrypt(Document doc, SecretKey key, Certificate certificate) {
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
			NodeList odseci = doc.getElementsByTagName("ns2:getNalogZaPlacanjeRequest");
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
	
	/**
	 * Kriptuje sadrzaj prvog elementa odsek
	 */
	public Document decrypt(Document doc, PrivateKey privateKey) {
		try {
			XMLCipher xmlCipher = XMLCipher.getInstance();
			//Inicijalizacija za desifrovanje
			xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
			//Postavlja se kljuc za desifrovanje tajnog kljuca
			xmlCipher.setKEK(privateKey);
			
			//Trazi se prvi EncryptedData element
			NodeList encDataList = doc.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
			Element encData = (Element) encDataList.item(0);
			
			//Desifruje se tajni kljuc, koji se onda koristi za desifrovanje podataka
			xmlCipher.doFinal(doc, encData); 
			return doc;
		} catch (XMLEncryptionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
