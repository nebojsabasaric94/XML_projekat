package bank.firm;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nalogzaplacanje.GetNalogZaPlacanjeRequest;
import com.nalogzaplacanje.GetNalogZaPlacanjeResponse;
import com.nalogzaplacanje.NalogZaPlacanje;
import com.nalogzaplacanje.ObjectFactory;

import bank.faktura.Faktura;
import bank.firm.ws.WSTemplate;



@Component
public class FirmClient {

	@Autowired
	private WSTemplate webServiceTemplate;


	public void sendNalog(Faktura f, boolean hitno) {
		ObjectFactory factory = new ObjectFactory();
		GetNalogZaPlacanjeRequest nalogZaPlacanjeRequest = factory.createGetNalogZaPlacanjeRequest();
		
		NalogZaPlacanje nalogZaPlacanje = factory.createNalogZaPlacanje();
		nalogZaPlacanje.setDatumNaloga(new Date());
		nalogZaPlacanje.setDatumValute(f.getDatumValute());
		nalogZaPlacanje.setDuznikNalogodavac(f.getNazivDobavljaca());
		nalogZaPlacanje.setHitno(hitno);
		nalogZaPlacanje.setIdPoruke("Nalog za prenos");
		nalogZaPlacanje.setIznos(new BigDecimal(f.getIznosZaUplatu()));
		nalogZaPlacanje.setModelOdobrenja(97);
		nalogZaPlacanje.setModelZaduzenja(92);
		nalogZaPlacanje.setOznakaValute(f.getOznakaValute());
		nalogZaPlacanje.setPozivNaBrojOdobrenja("147541214");
		nalogZaPlacanje.setPozivNaBrojZaduzenja("41247412");
		nalogZaPlacanje.setPrimalacPoverilac(f.getNazivKupca());
		nalogZaPlacanje.setRacunDuznika(f.getBrojRacuna());
		nalogZaPlacanje.setRacunPoverioca(f.getUplataNaRacun());
		nalogZaPlacanje.setSvrhaPlacanja("Svrha placanja");
		
		nalogZaPlacanjeRequest.setNalogZaPlacanje(nalogZaPlacanje);
		/*String sub = webServiceTemplate.getDefaultUri().substring(0, 20);
		webServiceTemplate.setDefaultUri(sub+"5/ws");*/
		
		GetNalogZaPlacanjeResponse getNalogZaPlacanjeResponse = (GetNalogZaPlacanjeResponse) webServiceTemplate.marshalSendAndReceive(nalogZaPlacanjeRequest);
		System.out.println("proslo");
	//	System.out.println(getNalogZaPlacanjeResponse.getNalogZaPlacanje().getDuznikNalogodavac());
		}

}
