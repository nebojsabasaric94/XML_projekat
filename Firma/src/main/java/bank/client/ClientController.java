package bank.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.apache.sshd.common.util.Base64;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bank.certificate.BankCertificate;
import bank.certificate.CertificateController;
import bank.certificate.CertificateGenerator;
import bank.certificate.CertificateService;
import bank.certificate.SubjectData;
import bank.user.User;
import bank.user.UserService;

@RestController
@RequestMapping("/client")
public class ClientController {
	
	private static Logger logger = LoggerFactory.getLogger(CertificateController.class);

	
	private final CertificateService certificateService;
	private final UserService userService;
	
	@Autowired
	public ClientController(final CertificateService certificateService,final UserService userService) {
		Security.addProvider(new BouncyCastleProvider());
		this.certificateService = certificateService;
		this.userService = userService;
	}
	
	@PreAuthorize("hasAuthority('createCSR')")
	@PostMapping("/certificates/csr")
	public void generateCSR(@RequestBody ClientCertificate clientCertificate) throws OperatorCreationException, IOException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		KeyPair pair = generateKeyPair();

	
		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();
		int unique_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
		
		byte[] encrypted = encrypt(clientCertificate.getPassword(), publicKey);
        FileOutputStream outputStream = new FileOutputStream(new File(unique_id +".txt"));
        outputStream.write(encrypted);
        outputStream.close();

		//http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+APIs
		ContentSigner signGen = new JcaContentSignerBuilder("SHA1withRSA").build(privateKey);
		X500Principal subject = new X500Principal("C=" +clientCertificate.getCountry()+ ", O="+clientCertificate.getOrganization()+", OU="+clientCertificate.getOrganizationUnit()+", CN="+clientCertificate.getCommonName()+", EMAILADDRESS="+clientCertificate.getEmail());
		PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
		PKCS10CertificationRequest certRequest = builder.build(signGen);
		

		
		PemObject pemObject = new PemObject("CERTIFICATE REQUEST", certRequest.getEncoded());
		//StringWriter str = new StringWriter();
		PemWriter pemWriter = new PemWriter(new FileWriter(unique_id+".pem"));
		pemWriter.writeObject(pemObject);
		//pemWriter.writeObject(pemObject);
		pemWriter.close();

		logger.info("User " + getUserDetails().getUsername() + " created CSR.");
		
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(unique_id+".key");		
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();		
	}
	
	@GetMapping("/certificates/requests")
	public java.util.List<String> findAllRequests(){
		ArrayList<String> result = new ArrayList<>();
		File dir = new File(".");
		File[] listing = dir.listFiles(new FilenameFilter() { // ovo ce mi trebati kada budem listao  sve sertifikate
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".pem");
			}
		});
		for (int i = 0; i < listing.length; i++) {
			String cName = listing[i].getPath().replaceAll(".pem", "");
			result.add(cName.substring(2));
		}
		return result;
	}
	@GetMapping("/certificates/requests/{request}")
	public ClientCertificate findRequest(@PathVariable String request) throws IOException{
		FileReader fileReader = new FileReader(request+".pem");
		PemReader pemReader = new PemReader(fileReader);

		PKCS10CertificationRequest csr = 
		    new PKCS10CertificationRequest(pemReader.readPemObject().getContent());	
		pemReader.close();

		X500Name x500name = csr.getSubject();
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		RDN e = x500name.getRDNs(BCStyle.EmailAddress)[0];
		RDN c = x500name.getRDNs(BCStyle.C)[0];
		RDN o = x500name.getRDNs(BCStyle.O)[0];
		RDN ou = x500name.getRDNs(BCStyle.OU)[0];
		
		
		String commonName = IETFUtils.valueToString(cn.getFirst().getValue());
		String email = IETFUtils.valueToString(e.getFirst().getValue());
		String country = IETFUtils.valueToString(c.getFirst().getValue());
		String organization = IETFUtils.valueToString(o.getFirst().getValue());
		String organizationUnit = IETFUtils.valueToString(ou.getFirst().getValue());
		ClientCertificate clientCertificate = new ClientCertificate(commonName, country, organization, organizationUnit, email);
		return  clientCertificate;
	}
	
	
	@PreAuthorize("hasAuthority('signCSR')")
	@RequestMapping(value = "/certificates",method = RequestMethod.POST)
	public void signCSR(@RequestBody BankCertificate bc) throws IOException, KeyStoreException,
			NoSuchProviderException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		FileReader fileReader = new FileReader(bc.getSerialNumber()+".pem");
		PemReader pemReader = new PemReader(fileReader);

		PKCS10CertificationRequest request = 
		    new PKCS10CertificationRequest(pemReader.readPemObject().getContent());	
		pemReader.close();

		JcaPKCS10CertificationRequest jcaRequest = new JcaPKCS10CertificationRequest(request);
		PublicKey publicKey = jcaRequest.getPublicKey();
		KeyStore ks = KeyStore.getInstance("JKS", "SUN");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream("ksBanks\\"+bc.getIssuerCommonName() + ".jks"));
		ks.load(in, "123".toCharArray());

		//citam privatni kljuc
		File filePrivateKey = new File(bc.getSerialNumber()+ ".key");
		FileInputStream fis = new FileInputStream(bc.getSerialNumber()+ ".key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();	
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        File inputFile = new File(bc.getSerialNumber()+".txt");
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);
        inputStream.close();        
        byte[] dec =  decrypt(inputBytes, privateKey);
       	String pass = new String(dec);		
		
       	
		if (ks.isKeyEntry(bc.getIssuerAlias())) {

			PrivateKey issuerPrivateKey = (PrivateKey) ks.getKey(bc.getIssuerAlias(),
					bc.getIssuerPassword().toCharArray());// privatni kljuc issuer-a
			Certificate issuerCert = ks.getCertificate(bc.getIssuerAlias());
			X500Name issuerX500Name = new JcaX509CertificateHolder((X509Certificate) issuerCert).getSubject();
			Certificate[] issuerChain = ks.getCertificateChain(bc.getIssuerAlias());// certificate chain if issuer , kako bi odredio ceo chain za  subject

			// sada imam potrebne podatke vezane za issuer-a za potpis
			// sertifikata


			SubjectData subject = generateSubjectData(bc, publicKey);
			CertificateGenerator cg = new CertificateGenerator();
			X509Certificate certificate = cg.generateCertificate(subject, issuerPrivateKey, issuerX500Name,false);
			boolean ca = certificate.getBasicConstraints() != -1;
			System.out.println("CA: " + ca);
			
			Certificate[] chain = new Certificate[issuerChain.length + 1];
			chain[0] = certificate;
			for (int i = 0; i < issuerChain.length; i++) {
				chain[i + 1] = issuerChain[i];
			}
			File file = new File("ksClients\\"+bc.getCommonName() + ".jks");
			// keyStore.load(null, null);

			if (!file.exists()) {
				file.createNewFile();
				ks.load(null, bc.getCommonName().replaceAll(" ", "").toCharArray());
			} else {
				ks.load(new FileInputStream(file), bc.getCommonName().replaceAll(" ", "").toCharArray());
			}
			ks.setKeyEntry(bc.getAlias(), privateKey, pass.toCharArray(),chain);
			//ks.setCertificateEntry(bc.getAlias(), certificate);
			ks.store(new FileOutputStream(file), bc.getCommonName().replaceAll(" ", "").toCharArray());
			
			// cuvanje u bazu serial number-revoke
			certificateService.save(new bank.certificate.Certificate(bc.getSerialNumber(), false));
			
			// export to .cer fajl
			final FileOutputStream os = new FileOutputStream("certificates\\"+certificate.getSerialNumber() + ".cer");
			os.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
			os.write(Base64.encodeBase64(certificate.getEncoded(), true));
			os.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
			os.close();
			
    		File removeFile = new File(bc.getSerialNumber()+".pem");
    		File removeKey = new File(bc.getSerialNumber()+".key");
    		File removePass = new File(bc.getSerialNumber()+".txt");

    		removeFile.delete();
    		removeKey.delete();
    		removePass.delete();
			logger.info("User " + getUserDetails().getUsername() + " signed CSR .Certifiate serial number is: " + bc.getSerialNumber());
		}



	}	
	
	private KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
	private SubjectData generateSubjectData(BankCertificate bc, PublicKey publicKey) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, bc.getCommonName());

		builder.addRDN(BCStyle.O, bc.getOrganization());
		builder.addRDN(BCStyle.OU, bc.getOrganizationUnit());
		builder.addRDN(BCStyle.C, bc.getCountry());
		builder.addRDN(BCStyle.E, bc.getEmail());
		// UID (USER ID) je ID korisnika
		builder.addRDN(BCStyle.UID, bc.getSerialNumber());
		return new SubjectData(publicKey, builder.build(), bc.getSerialNumber(), bc.getStartDate());
	}	
	private User getUserDetails() {
		  SecurityContext context = SecurityContextHolder.getContext();
		  Authentication authentication = context.getAuthentication();  
		  User user = userService.findByUsername(authentication.getName());
		  return user;
	}
	
	private byte[] encrypt(String plainText, PublicKey key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		

		Cipher rsaCipherEnc = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
		rsaCipherEnc.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = rsaCipherEnc.doFinal(plainText.getBytes());
		return cipherText;

	}
	private byte[] decrypt(byte[] cipherText, PrivateKey key) {
		try {			
			Cipher rsaCipherDec = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			rsaCipherDec.init(Cipher.DECRYPT_MODE, key);
			
			//desifrovanje
			byte[] plainText = rsaCipherDec.doFinal(cipherText);
			return plainText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
}
