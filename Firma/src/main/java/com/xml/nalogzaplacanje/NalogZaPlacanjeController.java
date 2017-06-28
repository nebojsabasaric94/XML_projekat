package com.xml.nalogzaplacanje;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.xml.faktura.Faktura;
import com.xml.faktura.Transformation;
import com.xml.firm.Firma;
import com.xml.user.User;
import com.xml.user.UserService;

@RestController
@RequestMapping("/nalog")
public class NalogZaPlacanjeController {


	@Autowired
	private NalogZaPlacanjeService nzps;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public List<NalogZaPlacanje> getNaloge() {
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<NalogZaPlacanje> nalozi = new ArrayList<NalogZaPlacanje>();
		for (int i = 0; i < firm.getNaloziZaPlacanjeNalogodavac().size(); i++) {
			
			nalozi.add(firm.getNaloziZaPlacanjeNalogodavac().get(i));
			
		}
		return nalozi;
	}
	
	@PostMapping("/transformHTML")
	public void transformAndGenerate(@RequestBody NalogZaPlacanje NalogZaPlacanje) throws IOException, DocumentException {
		File file = new File("nalogXMl.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(NalogZaPlacanje.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "faktura.xsd");
			        
			jaxbMarshaller.marshal(NalogZaPlacanje, file);
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder sb = new StringBuilder();

			while((line=br.readLine())!= null){
			    sb.append(line.trim());
			}
			
			String testni=sb.toString();
			String testniKo;
			testniKo=testni.replaceAll("<NalogZaPlacanje xmlns=\"http://nalogZaPlacanje.xml.com\"", "<?xml-stylesheet type=\"text/xsl\" href=\"faktReslt/xsltNalog.xsl\"?> <NalogZaPlacanje");
			
			System.out.println(testniKo);
			File filer = new File("nalogZaTransformac.xml");
			try {
			    BufferedWriter out = new BufferedWriter(new FileWriter(filer));
			    out.write(testniKo);  
			    out.close();
			}
			catch (IOException e)
			{
			    System.out.println("Exception ");

			}

			
			TransformationNalog.ExecuteOrder66();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return new File(Transformation.HTML_FILE);
		
	}
	
	private User getUserDetails() {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		User user = userService.findByUsername(authentication.getName());

		return user;

	}
}