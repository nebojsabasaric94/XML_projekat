package com.xml.zahtevzadobijanjeizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xml.firm.FirmClient;

@RestController
@RequestMapping("/izvod")
public class ZahtevZaDobijanjeIzvodaController {

	
	@Autowired
	FirmClient firmClient;
	
	GetZahtevResponse getZahtevResponseTemp;
	
	@PostMapping
	public void posaljiZahtevZaDobijanjeIzvoda(@RequestBody ZahtevZaDobijanjeIzvoda zahtevZaDobijanjeIzvoda){
		
		System.out.println(zahtevZaDobijanjeIzvoda.brojRacuna);
		System.out.println(zahtevZaDobijanjeIzvoda.datum);
		System.out.println(zahtevZaDobijanjeIzvoda.redniBrojPreseka);
		
		GetZahtevResponse response = firmClient.sendZahtevZaDobijanjeIzvoda(zahtevZaDobijanjeIzvoda);
		System.out.println("-----------success----------");
		System.out.println(response.getPresek().getZaglavlje().getNovoStanje());
		getZahtevResponseTemp = response;
	}
	
	@GetMapping
	public GetZahtevResponse getZahtevResponseTemp(){
		return getZahtevResponseTemp;
	}
	
	
	
}
