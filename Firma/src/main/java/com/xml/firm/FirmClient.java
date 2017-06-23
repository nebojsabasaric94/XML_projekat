package com.xml.firm;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.xml.faktura.Faktura;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeRequest;
import com.xml.nalogzaplacanje.GetNalogZaPlacanjeResponse;
import com.xml.nalogzaplacanje.NalogZaPlacanje;
import com.xml.nalogzaplacanje.NalogZaPlacanjeService;
import com.xml.nalogzaplacanje.ObjectFactory;
import com.xml.ws.WSTemplate;
import com.xml.zahtevzadobijanjeizvoda.GetZahtevRequest;
import com.xml.zahtevzadobijanjeizvoda.ZahtevZaDobijanjeIzvoda;

@Component
public class FirmClient {

	@Autowired
	private WSTemplate webServiceTemplate;

	@Autowired
	private NalogZaPlacanjeService nalogService;


	public void sendNalog(Faktura f, boolean hitno, Firma firmaPoverioca) {
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
		nalogZaPlacanje.setPozivNaBrojOdobrenja("147541214");
		nalogZaPlacanje.setPozivNaBrojZaduzenja("41247412");
		nalogZaPlacanje.setPrimalacPoverilac(f.getNazivDobavljaca());
		nalogZaPlacanje.setRacunDuznika(f.getUplataNaRacun());

		nalogZaPlacanje.setRacunPoverioca(firmaPoverioca.getBrojRacuna());
		nalogZaPlacanje.setSvrhaPlacanja("Svrha placanja");
		//nalogZaPlacanje.setFirmaNalogodavac(f.getFirma());
		//nalogZaPlacanje.setFirmaPoverilac(firmaPoverioca);

		nalogZaPlacanjeRequest.setNalogZaPlacanje(nalogZaPlacanje);

		GetNalogZaPlacanjeResponse getNalogZaPlacanjeResponse = (GetNalogZaPlacanjeResponse) webServiceTemplate
				.marshalSendAndReceive(nalogZaPlacanjeRequest);
		if (getNalogZaPlacanjeResponse.getNalogZaPlacanje() != null) {
			System.out.println("proslo");
			getNalogZaPlacanjeResponse.getNalogZaPlacanje().setFirmaNalogodavac(f.getFirma());
			getNalogZaPlacanjeResponse.getNalogZaPlacanje().setFirmaPoverilac(firmaPoverioca);
			
			nalogService.save(getNalogZaPlacanjeResponse.getNalogZaPlacanje());
		}
		// System.out.println(getNalogZaPlacanjeResponse.getNalogZaPlacanje().getDuznikNalogodavac());
	}
	
	
	public void sendZahtevZaDobijanjeIzvoda(ZahtevZaDobijanjeIzvoda zahtevZaDobijanjeIzvoda){
		GetZahtevRequest getZahtevRequest = new GetZahtevRequest();
		getZahtevRequest.setZahtevZaDobijanjeIzvoda(zahtevZaDobijanjeIzvoda);
		GetNalogZaPlacanjeResponse response = (GetNalogZaPlacanjeResponse) webServiceTemplate
				.marshalSendAndReceive(getZahtevRequest);
		
	}

}
