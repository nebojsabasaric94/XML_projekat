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

import com.xml.WSTemplate;
import com.xml.bank.Bank;
import com.xml.bank.BankService;
import com.xml.firm.FirmService;
import com.xml.firm.Firma;
import com.xml.mt102.GetMt102Request;
import com.xml.mt102.GetMt102Response;
import com.xml.mt102.Mt102;
import com.xml.mt102.Mt900Service;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaRequest;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaResponse;


@Component
public class BankClient {

	@Autowired
	private WSTemplate webServiceTemplate;

	@Autowired
	private BankService bankService;	
	
	@Autowired
	private FirmService firmService;
	
	@Autowired
	private Mt900Service mt900Service;
	
/*	public void sendToNationalBank(NalogZaPlacanje nalog){
		ObjectFactory factory = new ObjectFactory();
		GetNalogZaPlacanjeRequest nalogZaPlacanjeRequest = factory.createGetNalogZaPlacanjeRequest();
		
		GetNalogZaPlacanjeResponse response = (GetNalogZaPlacanjeResponse) webServiceTemplate.marshalSendAndReceive(nalogZaPlacanjeRequest);
		
	}*/
	
	public GetStrukturaRtgsNalogaResponse sendMt103ToNationalBank(GetStrukturaRtgsNalogaRequest request){
		
		return (GetStrukturaRtgsNalogaResponse) webServiceTemplate.marshalSendAndReceive(request);
	}
	
	public GetMt102Response sendMt102ToNationalBank(Mt102 mt102){
		com.xml.mt102.ObjectFactory factory = new com.xml.mt102.ObjectFactory();
		
		GetMt102Request request = factory.createGetMt102Request();
		request.setMt102(mt102);
		boolean validateGetMt102Request = validateGetMt102Request(request);
		if(validateGetMt102Request){
			GetMt102Response response = (GetMt102Response) webServiceTemplate.marshalSendAndReceive(request);
			//nakon vracene poruke skini pare sa racuna
			if(response.getMt900().getIdPoruke().equals("MT900")){
				String racunDuznika = null;
				for(int i =0; i < mt102.getNalogZaMT102().size();i++){
					racunDuznika = mt102.getNalogZaMT102().get(i).getRacunDuznika();
					Firma duznik = firmService.findByAccount(racunDuznika);
					duznik.setStanjeRacuna(duznik.getStanjeRacuna()-mt102.getNalogZaMT102().get(i).getIznos().intValue());
					firmService.save(duznik);
				}
				Bank bankaDuznika = bankService.findByObracunskiRacunBanke(response.getMt900().getObracunskiRacunBankeDuznika());
				response.getMt900().setBankaDuznika(bankaDuznika);			
				mt900Service.save(response.getMt900());
				return response;
			}
		}
		return null;
		/*response.getMt102().setObradjen(true);
		mt102Service.save(response.getMt102());*/
		//return null;
	}

	private boolean validateGetMt102Request(GetMt102Request request) {



		File file = new File("getMt102Request.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetMt102Request.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(request, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("classpath:mt102.xsd"));
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("getMt102Request.xml"));
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
