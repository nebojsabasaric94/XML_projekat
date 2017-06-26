package com.xml.firm;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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

import com.xml.faktura.Faktura;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeRequest;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeResponse;
import com.xml.nalogzaplacanje.NalogZaPlacanje;
import com.xml.nalogzaplacanje.NalogZaPlacanjeService;
import com.xml.nalogzaplacanje.ObjectFactory;
import com.xml.ws.WSTemplate;
import com.xml.zahtevzadobijanjeizvoda.GetZahtevRequest;
import com.xml.zahtevzadobijanjeizvoda.GetZahtevResponse;
import com.xml.zahtevzadobijanjeizvoda.ZahtevZaDobijanjeIzvoda;

@Component
public class FirmClient {

	@Autowired
	private WSTemplate webServiceTemplate;

	@Autowired
	private NalogZaPlacanjeService nalogService;


	public boolean sendNalog(Faktura f, boolean hitno, Firma firmaPoverioca) {
		ObjectFactory factory = new ObjectFactory();
		GetNalogZaPlacanjeRequest nalogZaPlacanjeRequest = factory.createGetNalogZaPlacanjeRequest();

		NalogZaPlacanje nalogZaPlacanje = factory.createNalogZaPlacanje();
		nalogZaPlacanje.setDatumNaloga(new Date());
		nalogZaPlacanje.setDatumValute(f.getDatumValute());
		nalogZaPlacanje.setDuznikNalogodavac(f.getNazivKupca());
		nalogZaPlacanje.setHitno(hitno);
		nalogZaPlacanje.setIdPoruke("Nalog za prenos");
		nalogZaPlacanje.setIznos(new BigDecimal(f.getIznosZaUplatu()));
		nalogZaPlacanje.setModelOdobrenja(97);
		nalogZaPlacanje.setModelZaduzenja(92);
		nalogZaPlacanje.setOznakaValute(f.getOznakaValute());
		nalogZaPlacanje.setPozivNaBrojOdobrenja("pozivNaBrojZaduzenja");
		nalogZaPlacanje.setPozivNaBrojZaduzenja("pozivNaBrojZaduzenja");
		nalogZaPlacanje.setPrimalacPoverilac(f.getNazivDobavljaca());
		nalogZaPlacanje.setRacunDuznika(f.getUplataNaRacun());

		nalogZaPlacanje.setRacunPoverioca(firmaPoverioca.getBrojRacuna());
		nalogZaPlacanje.setSvrhaPlacanja("Svrha placanja");
		//nalogZaPlacanje.setFirmaNalogodavac(f.getFirma());
		//nalogZaPlacanje.setFirmaPoverilac(firmaPoverioca);

		nalogZaPlacanjeRequest.setNalogZaPlacanje(nalogZaPlacanje);
		boolean validate = validateNalogZaPlacanjeRequest(nalogZaPlacanjeRequest);
		if(validate){
			GetNalogZaPlacanjeResponse getNalogZaPlacanjeResponse = (GetNalogZaPlacanjeResponse) webServiceTemplate
					.marshalSendAndReceive(nalogZaPlacanjeRequest);
			if (getNalogZaPlacanjeResponse.getNalogZaPlacanje() != null) {
				System.out.println("proslo");
				getNalogZaPlacanjeResponse.getNalogZaPlacanje().setFirmaNalogodavac(f.getFirma());
				getNalogZaPlacanjeResponse.getNalogZaPlacanje().setFirmaPoverilac(firmaPoverioca);
				
				nalogService.save(getNalogZaPlacanjeResponse.getNalogZaPlacanje());
				return true;
			}
			
		}
		return false;
		// System.out.println(getNalogZaPlacanjeResponse.getNalogZaPlacanje().getDuznikNalogodavac());
	}
	
	
	public GetZahtevResponse sendZahtevZaDobijanjeIzvoda(ZahtevZaDobijanjeIzvoda zahtevZaDobijanjeIzvoda){
		GetZahtevRequest getZahtevRequest = new GetZahtevRequest();
		getZahtevRequest.setZahtevZaDobijanjeIzvoda(zahtevZaDobijanjeIzvoda);
		GetZahtevResponse response = (GetZahtevResponse) webServiceTemplate
				.marshalSendAndReceive(getZahtevRequest);
		return response;
		
	}
	
	private boolean validateNalogZaPlacanjeRequest(GetNalogZaPlacanjeRequest nalogZaPlacanje){
		File file = new File("getNalogZaPlacanjeRequest.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GetNalogZaPlacanjeRequest.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(nalogZaPlacanje, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("classpath:nalogZaPlacanje.xsd"));
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("getNalogZaPlacanjeRequest.xml"));
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
			//e.printStackTrace();
			return false;
		}

		
		
	}

}
