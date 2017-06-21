package com.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.crypto.SecretKey;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.support.MarshallingUtils;
import org.w3c.dom.Document;

import com.xml.encryption.KeyStoreReader;
import com.xml.encryption.XMLEncryptionUtility;
import com.xml.encryption.XMLSigningUtility;
import com.xml.mt102.GetMt102Request;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeRequest;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaRequest;



public class WSTemplate extends WebServiceTemplate {
	
	
	//koristi se ovaj WebServiceTemplate da bi mogao sam da menjam izlaznu poruku, tj da sifrujem
	
	@Override
	public Object marshalSendAndReceive(String uri,
										final Object requestPayload,
										final WebServiceMessageCallback requestCallback) {
		return sendAndReceive(uri, new WebServiceMessageCallback() {

			public void doWithMessage(WebServiceMessage request) throws IOException, TransformerException {
				if (requestPayload != null) {
					Marshaller marshaller = getMarshaller();
					if (marshaller == null) {
						throw new IllegalStateException(
								"No marshaller registered. Check configuration of WebServiceTemplate.");
					}
					MarshallingUtils.marshal(marshaller, requestPayload, request);
					if (requestCallback != null) {
						requestCallback.doWithMessage(request);
					}
				}
				if(requestPayload instanceof GetNalogZaPlacanjeRequest){
				
					DOMSource source = (DOMSource) request.getPayloadSource();
					Document doc = source.getNode().getOwnerDocument();
					
					
					//-----sve za print na izlaz----
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(source, result);
					System.out.println("XML IN String format is: \n" + writer.toString());
					
					
					//---------sifrovanje---------------------
					KeyStoreReader ksReader = new KeyStoreReader();
					XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
					XMLSigningUtility sigUtility = new XMLSigningUtility();
					SecretKey secretKey = encUtility.generateDataEncryptionKey();
					Certificate certNationalBank = ksReader.readCertificate("ksCentralBank\\Narodna banka.jks", "123", "nbs1");
					Certificate certBank = ksReader.readCertificate("ksBanks\\Banka A.jks", "123", "ba1");
					
					doc = encUtility.encryptRtgs(doc, secretKey, certNationalBank);
					PrivateKey privateKey = ksReader.readPrivateKey("ksBanks\\Banka A.jks", "123", "ba1", "123");
					doc = sigUtility.signDocument(doc, privateKey, certBank);
					saveDocument(doc,"nalog_encrypted_and_signed.xml");
				}
				else if(requestPayload instanceof GetStrukturaRtgsNalogaRequest){
					DOMSource source = (DOMSource) request.getPayloadSource();
					Document doc = source.getNode().getOwnerDocument();
					
					
					//-----sve za print na izlaz----
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(source, result);
					System.out.println("RTGS XML IN String format is: \n" + writer.toString());
					
					
					//---------sifrovanje---------------------
					KeyStoreReader ksReader = new KeyStoreReader();
					XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
					XMLSigningUtility sigUtility = new XMLSigningUtility();
					SecretKey secretKey = encUtility.generateDataEncryptionKey();
					Certificate certNationalBank = ksReader.readCertificate("ksCentralBank\\Narodna banka.jks", "123", "nbs1");
					Certificate certBank = ksReader.readCertificate("ksBanks\\Banka A.jks", "123", "ba1");
					
					doc = encUtility.encryptRtgs(doc, secretKey, certNationalBank);
					PrivateKey privateKey = ksReader.readPrivateKey("ksBanks\\Banka A.jks", "123", "ba1", "123");
					doc = sigUtility.signDocument(doc, privateKey, certBank);
					saveDocument(doc,"RTGS_encrypted_and_signed.xml");
				}
				else if(requestPayload instanceof GetMt102Request){
					DOMSource source = (DOMSource) request.getPayloadSource();
					Document doc = source.getNode().getOwnerDocument();
					
					
					//-----sve za print na izlaz----
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(source, result);
					System.out.println("MT102 XML IN String format is: \n" + writer.toString());
					
					
					//---------sifrovanje---------------------
					KeyStoreReader ksReader = new KeyStoreReader();
					XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
					XMLSigningUtility sigUtility = new XMLSigningUtility();
					SecretKey secretKey = encUtility.generateDataEncryptionKey();
					Certificate certNationalBank = ksReader.readCertificate("ksCentralBank\\Narodna banka.jks", "123", "nbs1");
					Certificate certBank = ksReader.readCertificate("ksBanks\\Banka A.jks", "123", "ba1");
					
					doc = encUtility.encryptMT102(doc, secretKey, certNationalBank);
					PrivateKey privateKey = ksReader.readPrivateKey("ksBanks\\Banka A.jks", "123", "ba1", "123");
					doc = sigUtility.signDocument(doc, privateKey, certBank);
					saveDocument(doc,"MT102_encrypted_and_signed.xml");
				}
				
		}
		}, new WebServiceMessageExtractor<Object>() {

			public Object extractData(WebServiceMessage response) throws IOException {
				Unmarshaller unmarshaller = getUnmarshaller();
				if (unmarshaller == null) {
					throw new IllegalStateException(
							"No unmarshaller registered. Check configuration of WebServiceTemplate.");
				}
				return MarshallingUtils.unmarshal(unmarshaller, response);
			}
		});
	}
	
	
	
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
	
	

}
