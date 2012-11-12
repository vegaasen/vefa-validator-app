package no.difi.vefa.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import no.difi.vefa.properties.PropertiesFile;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XSDValidationTest {

	private String basePath;
	private String xsdFile;
	private String xmlFile;
	private PropertiesFile propFile;
	
	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/java/no/difi/vefa/xml/").getCanonicalPath();
		xsdFile = basePath + "/TestXSD.xsd";		
		xmlFile = new Scanner(new File(basePath + "/TestXSD.xml")).useDelimiter("\\Z").next();	
		
		String path = new java.io.File("src/test/java/no/difi/vefa/xml/validator.properties").getCanonicalPath();
		propFile = new PropertiesFile();
		propFile.main(path);
		propFile.dataDir = new java.io.File(".").getCanonicalPath();		
	}

	@Test
	public void testMain() throws Exception {
		Utils utils = new Utils();
		Document xmlDoc = utils.stringToXMLDOM(xmlFile);
		
		no.difi.vefa.xml.XSDValidation xsdValidation = new no.difi.vefa.xml.XSDValidation();		
		
		try {
			xsdValidation.main(xmlDoc, xsdFile, propFile);
		} catch (Exception e) {
			fail("XML has failed XSD validation");
		}
	}
}
