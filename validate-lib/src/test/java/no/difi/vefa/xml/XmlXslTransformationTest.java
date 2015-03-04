package no.difi.vefa.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XmlXslTransformationTest {

	private String basePath;
	private String xslFile;
	private String xmlFile;
	
	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		xslFile = basePath + "/Test.xsl";		
		Scanner scanner = new Scanner(new File(basePath + "/Invoice.xml"));
		xmlFile = scanner.useDelimiter("\\Z").next();
		scanner.close();
	}

	@Test
	public void testMain() throws Exception {
		Utils utils = new Utils();
		Document xmlDoc = utils.stringToXMLDOM(xmlFile);
		
		no.difi.vefa.xml.XmlXslTransformation xmlXslTransformation = new no.difi.vefa.xml.XmlXslTransformation();
		Document result = xmlXslTransformation.main(xmlDoc, xslFile);

		assertEquals("true", result.getElementsByTagName("status").item(0).getTextContent());
	}
}
