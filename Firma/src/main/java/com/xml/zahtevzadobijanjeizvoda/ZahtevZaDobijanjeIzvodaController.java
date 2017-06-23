package com.xml.zahtevzadobijanjeizvoda;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/izvod")
public class ZahtevZaDobijanjeIzvodaController {

	@PostMapping
	public void posaljiZahtevZaDobijanjeIzvoda(@RequestBody ZahtevZaDobijanjeIzvoda zahtevZaDobijanjeIzvoda){
		
		System.out.println(zahtevZaDobijanjeIzvoda.brojRacuna);
		System.out.println(zahtevZaDobijanjeIzvoda.datum);
		System.out.println(zahtevZaDobijanjeIzvoda.redniBrojPreseka);
	}
	
	
	
}
