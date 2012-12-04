package no.difi.vefa.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.xml.Utils.XMLNamespace;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UtilsTest {

	private Utils utils;
	private String xmlTestString;	
	private String xmlTestInvoice;
	
	@Before
	public void setUp() throws Exception {
		utils = new Utils();

		String basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		xmlTestString = new Scanner(new File(basePath + "/config.xml")).useDelimiter("\\Z").next();
		xmlTestInvoice = new Scanner(new File(basePath + "/Invoice.xml")).useDelimiter("\\Z").next();						
	}

	@Test
	public void testXmlDOMToString() throws Exception {				
		Document xmlDoc = utils.stringToXMLDOM("<data><test>This is a test</test></data>");
		
		String expected = "<data><test>This is a test</test></data>";
		String result = utils.xmlDOMToString(xmlDoc);
		
		assertEquals(expected, result);
	}

	@Test
	public void testStringToXMLDOM() throws Exception {		
		Document xmlDoc1 = utils.stringToXMLDOM(xmlTestString);
		Document xmlDoc2 = utils.stringToXMLDOM(xmlTestString);		
		assertEquals(utils.xmlDOMToString(xmlDoc1), utils.xmlDOMToString(xmlDoc2));		
	}

	@Test
	public void testXmlDOMXPathQuery() throws Exception {
		Document xmlDoc1 = utils.stringToXMLDOM(xmlTestString);
		NodeList validate = utils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']");
		assertEquals(1, validate.getLength());
	}

	@Test
	public void testXmlDOMXPathQueryWithNS() throws Exception {
		List<XMLNamespace> namespaces = new ArrayList<XMLNamespace>();		
		XMLNamespace ns = utils.new XMLNamespace();
		ns.prefix = "cbc";
		ns.url = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
		namespaces.add(ns);
		
		Document xmlDoc1 = utils.stringToXMLDOM(xmlTestInvoice);
		Node pId = utils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:ProfileID", namespaces).item(0);
		Node cId = utils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:CustomizationID", namespaces).item(0);
		assertEquals("urn:www.cenbii.eu:profile:bii04:ver1.0", pId.getTextContent());
		assertEquals("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1", cId.getTextContent());		
	}

	@Test
	public void testInnerXml() throws Exception {
		Document xmlDoc1 = utils.stringToXMLDOM(xmlTestString);
		Node name = utils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']/name").item(0);

		String myXML = "<name xmlns:xi=\"http://www.w3.org/2001/XInclude\">\n" +
				"\t\t\t<en>EHF invoice in Norway, profile invoice only</en>\n" +
				"\t\t\t<no>EHF faktura i Norge, profil kun faktura</no>\n" +
				"\t\t</name>";		
		
		assertEquals(myXML, utils.innerXml(name));
	}
}
