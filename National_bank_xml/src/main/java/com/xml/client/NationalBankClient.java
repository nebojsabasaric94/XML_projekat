package com.xml.client;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.xml.mt102.GetMt910RequestMt102;
import com.xml.strukturartgsnaloga.GetMt910Request;
import com.xml.strukturartgsnaloga.GetMt910Response;
import com.xml.ws.WSTemplate;


@Component
public class NationalBankClient {

	@Autowired
	private WSTemplate webServiceTemplate;
	
	public GetMt910Response sendMt910(GetMt910Request request){
		
		GetMt910Response response  = (GetMt910Response) webServiceTemplate.marshalSendAndReceive(request);
		return response;
	}
	
	public com.xml.mt102.GetMt910Response sendMt910mt102(GetMt910RequestMt102 request){
		
		com.xml.mt102.GetMt910Response response = (com.xml.mt102.GetMt910Response) webServiceTemplate.marshalSendAndReceive(request);
		
		return response;
	}
	
	public boolean validateMt910Request(GetMt910Request request){

		File file = new File("getMt910Request.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetMt910Request.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(request, file);
			
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

	public boolean validateGetMt910RequestMt102(GetMt910RequestMt102 mt910Request) {

		File file = new File("getMt910RequestMt102.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetMt910RequestMt102.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(mt910Request, file);
			
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
	
}
