package com.example.national_bank_xml.National_bank_xml.ws;

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

import com.example.bankxml.bankxml.mt102.GetMt102Request;
import com.example.bankxml.bankxml.mt102.GetMt910RequestMt102;
import com.nalogzaplacanje.GetNalogZaPlacanjeRequest;
import com.strukturartgsnaloga.GetMt910Request;
import com.strukturartgsnaloga.GetStrukturaRtgsNalogaRequest;

import encryption.KeyStoreReader;
import encryption.XMLEncryptionUtility;
import encryption.XMLSigningUtility;


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
				if(requestPayload instanceof GetMt910Request){
				
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
				
					doc = encUtility.encryptMt910(doc, secretKey, certBank);
					PrivateKey privateKey = ksReader.readPrivateKey("ksCentralBank\\Narodna banka.jks", "123", "nbs1", "123");
					doc = sigUtility.signDocument(doc, privateKey, certNationalBank);
					saveDocument(doc,"MT910encrypted_signed.xml");
				}
				else if(requestPayload instanceof GetMt910RequestMt102){
					DOMSource source = (DOMSource) request.getPayloadSource();
					Document doc = source.getNode().getOwnerDocument();
					
					
					//-----sve za print na izlaz----
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(source, result);
					System.out.println("MT910MT102 XML IN String format is: \n" + writer.toString());
					
					
					//---------sifrovanje---------------------
					KeyStoreReader ksReader = new KeyStoreReader();
					XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
					XMLSigningUtility sigUtility = new XMLSigningUtility();
					SecretKey secretKey = encUtility.generateDataEncryptionKey();
					Certificate certNationalBank = ksReader.readCertificate("ksCentralBank\\Narodna banka.jks", "123", "nbs1");
					Certificate certBank = ksReader.readCertificate("ksBanks\\Banka A.jks", "123", "ba1");
				
					doc = encUtility.encryptMt910Mt102(doc, secretKey, certBank);
					PrivateKey privateKey = ksReader.readPrivateKey("ksCentralBank\\Narodna banka.jks", "123", "nbs1", "123");
					doc = sigUtility.signDocument(doc, privateKey, certNationalBank);
					saveDocument(doc,"MT910encrypted_signed.xml");
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
