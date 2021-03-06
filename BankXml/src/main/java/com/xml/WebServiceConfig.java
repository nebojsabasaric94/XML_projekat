package com.xml;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);

		return new ServletRegistrationBean(servlet, "/ws/*");
	}
	@Bean
	public SimpleWsdl11Definition banka() {
		return new SimpleWsdl11Definition(new ClassPathResource("banka.wsdl"));
	}
/*	@Bean(name = "banka")
	public DefaultWsdl11Definition defaultWsdl11Definition(CommonsXsdSchemaCollection schemaCollection)
			throws Exception {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("BankaPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://banka_xml.com/ws/");

		wsdl11Definition.setSchemaCollection(schemaCollection);
		// wsdl11Definition.setSchema(countriesSchema);

		return wsdl11Definition;
	}

	@Bean
	public CommonsXsdSchemaCollection schemeCollection() {
		CommonsXsdSchemaCollection collection = new CommonsXsdSchemaCollection(
				new Resource[] { new ClassPathResource("/nalogZaPlacanje.xsd"),new ClassPathResource("/strukturaRtgsNaloga.xsd"), new ClassPathResource("/mt102.xsd"),new ClassPathResource("/ZahtevZaDobijanjeIzvoda.xsd") });

		collection.setInline(true);
		return collection;
	}*/

	@Bean
	Jaxb2Marshaller jaxb2Marshaller() {

		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath("com.xml.nalogzaplacanje:com.xml.strukturartgsnaloga:com.xml.mt102:com.xml.zahtevzadobijanjeizvoda:");
		return jaxb2Marshaller;
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {

		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMarshaller(jaxb2Marshaller());
		webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
		webServiceTemplate.setDefaultUri("http://localhost:8083/ws");

		return webServiceTemplate;
	}
	
	@Bean
	public WSTemplate wsTemplate() {

		WSTemplate webServiceTemplate = new WSTemplate();
		webServiceTemplate.setMarshaller(jaxb2Marshaller());
		webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
		webServiceTemplate.setDefaultUri("http://localhost:8083/ws");

		return webServiceTemplate;
	}
}
