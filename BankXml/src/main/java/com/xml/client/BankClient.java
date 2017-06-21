package com.xml.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xml.WSTemplate;
import com.xml.firm.FirmService;
import com.xml.firm.Firma;
import com.xml.mt102.GetMt102Request;
import com.xml.mt102.GetMt102Response;
import com.xml.mt102.Mt102;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaRequest;
import com.xml.strukturartgsnaloga.GetStrukturaRtgsNalogaResponse;


@Component
public class BankClient {

	@Autowired
	private WSTemplate webServiceTemplate;

	@Autowired
	private FirmService firmService;
	
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
		GetMt102Response response = (GetMt102Response) webServiceTemplate.marshalSendAndReceive(request);
		//nakon vracene poruke skini pare sa racuna
		String racunDuznika = null;
		for(int i =0; i < mt102.getNalogZaMT102().size();i++){
			racunDuznika = mt102.getNalogZaMT102().get(i).getRacunDuznika();
			Firma duznik = firmService.findByAccount(racunDuznika);
			duznik.setStanjeRacuna(duznik.getStanjeRacuna()-mt102.getNalogZaMT102().get(i).getIznos().intValue());
			firmService.save(duznik);
		}
		return response;
		
		/*response.getMt102().setObradjen(true);
		mt102Service.save(response.getMt102());*/
		//return null;
	}
	
}
