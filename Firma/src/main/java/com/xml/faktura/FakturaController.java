package com.xml.faktura;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xml.firm.FirmClient;
import com.xml.firm.FirmService;
import com.xml.firm.Firma;
import com.xml.user.User;
import com.xml.user.UserService;

@RestController
@RequestMapping("/faktura")
public class FakturaController {
	
	private static Logger logger = LoggerFactory.getLogger(FakturaController.class);

	@Autowired
	private FakturaService fakturaService;
	@Autowired
	private FirmService firmService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	FirmClient firmClient;
	
	@GetMapping
	public List<Faktura> getFakture(){
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<Faktura> fakture = new ArrayList<Faktura>();
		for(int i = 0 ; i < firm.getFakture().size(); i++){
			if(!firm.getFakture().get(i).isObradjena()){
				fakture.add(firm.getFakture().get(i));
			}
		}
		return fakture;
	}
	
	@GetMapping("/findFirm")
	public Firma findFirm(){
		User user = getUserDetails();
		Firma firm = user.getFirma();
		return firm;
	}
	
	@PreAuthorize("hasAuthority('sendInvoice')")
	@PostMapping
	public void addFaktura(@Valid @RequestBody Faktura faktura){
		Firma firm = firmService.findByName(faktura.getNazivKupca());
		faktura.setFirma(firm);
		faktura.setObradjena(false);
		logger.info("User " + getUserDetails().getUsername() + " from firm " + getUserDetails().getFirma().getName() + " send invoice to " + faktura.getNazivKupca());
		fakturaService.save(faktura);
	
	}
	
	@GetMapping("/findAllFirms")
	public List<Firma> findAllFirms(){
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<Firma> firms = new ArrayList<Firma>();
		for(int i = 0 ; i < firmService.findAll().size(); i++){
			if(firmService.findAll().get(i).getId() != firm.getId()){
				firms.add(firmService.findAll().get(i));
			}
		}
		return firms;
	}
	
	@PreAuthorize("hasAuthority('sendInvoice')")
	@PostMapping("/obrada/{hitno}")
	public void obradi(@RequestBody Faktura faktura, @PathVariable boolean hitno){
		Faktura f = fakturaService.findOne(faktura.getId());
		Firma firmaPoverioca = firmService.findByName(f.getNazivDobavljaca());
		firmClient.sendNalog(f, hitno, firmaPoverioca);
		faktura.setObradjena(true);
		fakturaService.save(faktura);
		logger.info("User " + getUserDetails() + " processing invoice.");
	}
	private User getUserDetails() {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();		
		User user = userService.findByUsername(authentication.getName());
		
		return user;

	}
	
	
	
}
