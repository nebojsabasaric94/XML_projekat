package com.xml.faktura;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Transformation {
	
	private static DocumentBuilderFactory docFactory;
	
	private static TransformerFactory transFactory;
	
	public static final String INPUT_FILE = "transformnaFakt.xml";
	
	
	/// dodati fajl xsl
	public static final String XSL_FILE = "faktReslt/xsltFaktura.xsl";
	
	public static final String HTML_FILE = "faktReslt/hmtlFaktura.html";
	
	public static final String OUTPUT_FILE = "faktReslt/pdfFaktura.pdf";


	static {

		/* Inicijalizacija fabrike za dokumenta */
		docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		docFactory.setIgnoringComments(true);
		docFactory.setIgnoringElementContentWhitespace(true);
		
		/* Inicijalizacija fabrike za trnsformacije */
		transFactory = TransformerFactory.newInstance();
		
	}
	
	public void generatePDF(String filePath) throws IOException, DocumentException {

	    	Document document = new Document();
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
	        document.open();
	        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML_FILE));
	        document.close();
	        
	  }
	
	public org.w3c.dom.Document buildDocument(String filePath) {

		org.w3c.dom.Document document = null;
		try {
			
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			document = builder.parse(new File(filePath)); 

			if (document != null)
				System.out.println("[INFO] Uspesno parsiran");
			else
				System.out.println("[WARN] ***** NULL ****");

		} catch (Exception e) {
			return null;
			
		} 

		return document;
	}
	
	
	public void generateHTML(String xmlPath, String xslPath) throws FileNotFoundException {
    	
		try {
			StreamSource transformSource = new StreamSource(new File(xslPath));
			
			Transformer transformer = transFactory.newTransformer(transformSource);
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			// Generate XHTML
			transformer.setOutputProperty(OutputKeys.METHOD, "xhtml");

			// Transform DOM to HTML
			DOMSource source = new DOMSource(buildDocument(xmlPath));
			StreamResult result = new StreamResult(new FileOutputStream(HTML_FILE));
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			
			e.printStackTrace();
		} catch (TransformerException e) {
			
			e.printStackTrace();
		}
    
    }
	//konacna za odradjivanje
	public static void ExecuteOrder() throws IOException, DocumentException {

    	System.out.println("[INFO] " + Transformation.class.getSimpleName());
    	
    	
    	File pdfFile = new File(OUTPUT_FILE);
    	
		if (!pdfFile.getParentFile().exists()) {
			System.out.println("[INFO] A new directory is created: " + pdfFile.getParentFile().getAbsolutePath() + ".");
			pdfFile.getParentFile().mkdir();
		}
    	
		Transformation pdfTransformer = new Transformation();
		
		pdfTransformer.generateHTML(INPUT_FILE, XSL_FILE);
		pdfTransformer.generatePDF(OUTPUT_FILE);
		
		System.out.println("[INFO] File \"" + OUTPUT_FILE + "\" generated successfully.");
		System.out.println("[INFO] End.");
    }
}
