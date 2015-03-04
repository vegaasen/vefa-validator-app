package no.difi.vefa.utils.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import no.difi.vefa.utils.PropertiesUtils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XSDValidationTest {

	private String basePath;
	private String xsdFile;
	private String xmlFile;
	private PropertiesUtils propFile;
	
	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		xsdFile = basePath + "/TestXSD.xsd";		
		Scanner scanner = new Scanner(new File(basePath + "/TestXSD.xml"));
		xmlFile = scanner.useDelimiter("\\Z").next();	
		scanner.close();
		
		String path = new java.io.File("src/test/resources/validator.properties").getCanonicalPath();
		propFile = new PropertiesUtils();
		propFile.main(path);
		propFile.dataDir = new java.io.File(".").getCanonicalPath();		
	}

	@Test
	public void testMain() throws Exception {
		XmlUtils xmlUtils = new XmlUtils();
		Document xmlDoc = xmlUtils.stringToXMLDOM(xmlFile);
		
		no.difi.vefa.utils.xml.XSDValidation xsdValidation = new no.difi.vefa.utils.xml.XSDValidation();
		
		try {
			xsdValidation.main(xmlDoc, xsdFile, propFile);
		} catch (Exception e) {
			fail("XML has failed XSD validation");
		}
	}
}
