package bank.nationalBank;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import bank.selfCertificate.SelfCertificate;

public class SelfSignedGenerator {

	public SelfSignedGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public X509Certificate generateCertificate(KeyPair keyPair, SelfCertificate certificate, X500Name x500Name){
		

		try {
			//Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
			//Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
			//Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			//Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
			builder = builder.setProvider("BC");

			//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
			ContentSigner contentSigner = builder.build(keyPair.getPrivate());

			//Postavljaju se podaci za generisanje sertifiakta
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500Name, new BigInteger(certificate.getSerialNumber()), certificate.getStartDate(),
					new Date(certificate.getStartDate().getTime()+ 365*86400000l),x500Name, keyPair.getPublic());
			
			//----------
			KeyUsage keyUsage = new KeyUsage( KeyUsage.digitalSignature |
                    KeyUsage.nonRepudiation|
                    KeyUsage.keyEncipherment|
                    KeyUsage.dataEncipherment|
                    KeyUsage.keyCertSign |
                    KeyUsage.cRLSign);
			
			//Postavljaju se podaci za generisanje samopotpisujucih sertifiakta
			//X509v3CertificateBuilder generator = new JcaX509v3CertificateBuilder(x500Name, new BigInteger(certificate.getSerialNumber()), certificate.getStartDate(),
			//		certificate.getEndDate(),x500Name, keyPair.getPublic());
			try {
				certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
				certGen.addExtension(Extension.keyUsage, false, keyUsage);
			} catch (CertIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//-------------
			
			//Generise se samopotpisujuci sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);
			
			
			//Builder generise sertifikat kao objekat klase X509CertificateHolder
			//Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			//Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return null;
	
		
	}

}
