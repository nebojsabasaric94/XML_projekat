package encryption;

import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.implementations.RSAKeyValueResolver;
import org.apache.xml.security.keys.keyresolver.implementations.X509CertificateResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Potpisuje dokument, koristi se enveloped tip
public class XMLSigningUtility {
    public XMLSigningUtility() {
        Security.addProvider(new BouncyCastleProvider());
        org.apache.xml.security.Init.init();
    }
	
	public Document signDocument(Document doc, PrivateKey privateKey, Certificate cert) {
        try {
			Element rootEl = doc.getDocumentElement();
			
			//Kreira se signature objekat
			XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			//Kreiraju se transformacije nad dokumentom
			Transforms transforms = new Transforms(doc);
			    
			//Iz potpisa uklanja Signature element, sto je potrebno za enveloped tip po specifikaciji
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			//Normalizacija
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
			    
			//Potpisuje se citav dokument (URI "")
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
			    
			//U KeyInfo se postavalja javni kljuc uz citav sertifikat, za proveru digitalnog potpisa
			sig.addKeyInfo(cert.getPublicKey());
			sig.addKeyInfo((X509Certificate) cert);
			    
			//Poptis je child root elementa
			rootEl.appendChild(sig.getElement());
			    
			//Potpisivanje
			sig.sign(privateKey);
			
			return doc;
		} catch (TransformationException e) {
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XMLSecurityException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public boolean verifySignature(Document doc) {
		try {
			//Pronalazi se prvi Signature element 
			NodeList signatures = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
			Element signatureEl = (Element) signatures.item(0);
			
			//Kreira se signature objekat od elementa
			XMLSignature signature = new XMLSignature(signatureEl, null);
			//Preuzima se key info
			KeyInfo keyInfo = signature.getKeyInfo();
			if(keyInfo != null) {
				//Registruju se resolver-i za javni kljuc i sertifikat
				keyInfo.registerInternalKeyResolver(new RSAKeyValueResolver());
			    keyInfo.registerInternalKeyResolver(new X509CertificateResolver());
			    
			    //Ako postoji sertifikat, provera potpisa
			    if(keyInfo.containsX509Data() && keyInfo.itemX509Data(0).containsCertificate()) { 
			        Certificate cert = keyInfo.itemX509Data(0).itemCertificate(0).getX509Certificate();
			        if(cert != null) 
			        	return signature.checkSignatureValue((X509Certificate) cert);
			    }
			}
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		} catch (XMLSecurityException e) {
			e.printStackTrace();
		}
		return false;
	}
}
