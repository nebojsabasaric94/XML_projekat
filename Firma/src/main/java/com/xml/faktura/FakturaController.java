package com.xml.faktura;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
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
	public List<Faktura> getFakture() {
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<Faktura> fakture = new ArrayList<Faktura>();
		for (int i = 0; i < firm.getFakture().size(); i++) {
			if (!firm.getFakture().get(i).isObradjena()) {
				fakture.add(firm.getFakture().get(i));
			}
		}
		return fakture;
	}
	@GetMapping("/obr")
	public List<Faktura> getFaktureObr() {
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<Faktura> fakture = new ArrayList<Faktura>();
		for (int i = 0; i < firm.getFakture().size(); i++) {
			if (firm.getFakture().get(i).isObradjena()) {
				fakture.add(firm.getFakture().get(i));
			}
		}
		return fakture;
	}

	@GetMapping("/findFirm")
	public Firma findFirm() {
		User user = getUserDetails();
		Firma firm = user.getFirma();
		return firm;
	}

	@PreAuthorize("hasAuthority('sendInvoice')")
	@PostMapping
	public boolean addFaktura(@Valid @RequestBody Faktura faktura) {
		Firma firm = firmService.findByName(faktura.getNazivKupca());
		boolean validateFaktura = validateFakturaXml(faktura);
		if(validateFaktura){
			faktura.setFirma(firm);
			faktura.setObradjena(false);
			logger.info("User " + getUserDetails().getUsername() + " from firm " + getUserDetails().getFirma().getName()
					+ " send invoice to " + faktura.getNazivKupca());
			fakturaService.save(faktura);
			return true;
		}
		return false;
		

	}
	
	@PostMapping("/transformHTML")
	public File transformAndGenerate(@RequestBody Faktura faktura) throws IOException, DocumentException {
		File file = new File("fakturaForTransform.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Faktura.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "faktura.xsd");
			        
			jaxbMarshaller.marshal(faktura, file);
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder sb = new StringBuilder();

			while((line=br.readLine())!= null){
			    sb.append(line.trim());
			}
			String testni=sb.toString();
			String testniKo;
			testniKo=testni.replaceAll("<Faktura xmlns=\"http://localhost:8080/faktura\">", "<?xml-stylesheet type=\"text/xsl\" href=\"faktReslt/xsltFaktura.xsl\"?> <Faktura>");
			
			System.out.println(testniKo);
			File filer = new File("transformnaFakt.xml");
			try {
			    BufferedWriter out = new BufferedWriter(new FileWriter(filer));
			    out.write(testniKo);  
			    out.close();
			}
			catch (IOException e)
			{
			    System.out.println("Exception ");

			}

			
			Transformation.ExecuteOrder();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File(Transformation.HTML_FILE);
		
	}
	
	private boolean validateFakturaXml(Faktura faktura){
		File file = new File("faktura.xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Faktura.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(faktura, file);
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			try {
				schema = factory.newSchema(new StreamSource("faktura.xsd"));
				//Faktura fakturaXml = (Faktura) jaxbUnmarshaller.unmarshal(file);
				Validator validator = schema.newValidator();
				try {
					validator.validate(new StreamSource("faktura.xml"));
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					return false;
				}
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return false;
			}
			
					
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}

		
		
	}

	@GetMapping("/findAllFirms")
	public List<Firma> findAllFirms() {
		User user = getUserDetails();
		Firma firm = user.getFirma();
		List<Firma> firms = new ArrayList<Firma>();
		for (int i = 0; i < firmService.findAll().size(); i++) {
			if (firmService.findAll().get(i).getId() != firm.getId()) {
				firms.add(firmService.findAll().get(i));
			}
		}
		return firms;
	}

	@PreAuthorize("hasAuthority('sendInvoice')")
	@PostMapping("/obrada/{hitno}")
	public void obradi(@RequestBody Faktura faktura, @PathVariable boolean hitno) {
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
