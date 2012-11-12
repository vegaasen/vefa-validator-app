package no.difi.vefa.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		
		xmlTestString = "<config xmlns:xi=\"http://www.w3.org/2001/XInclude\"><validate " +
				"id=\"urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1\" " +
				"version=\"1.4\">" +
				"<name>" +
				"<en>EHF invoice in Norway, profile invoice only</en>" +
				"<no>EHF faktura i Norge, profil kun faktura</no>" +
				"</name>" +
				"</validate>" +
				"</config>";
		
		xmlTestInvoice = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<Invoice " +
				"xsi:schemaLocation=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2 UBL-Invoice-2.0.xsd\" " +
				"xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\" " +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" " +
				"xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" " +
				"xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\">" +
				"<cbc:UBLVersionID>2.0</cbc:UBLVersionID>" +
				"<cbc:CustomizationID>urn:www.cenbii.eu:transaction:BiiCoreTrdm001:ver1.0:extentionId</cbc:CustomizationID>" +
				"<cbc:ProfileID>urn:www.cenbii.eu:profile:bii05:ver1.0</cbc:ProfileID>" +
				"<cbc:ID>123456</cbc:ID>" +
				"<cbc:IssueDate>2009-11-12</cbc:IssueDate>" +
				"<cbc:DocumentCurrencyCode>NOK</cbc:DocumentCurrencyCode>" +
				"<cac:OrderReference>" +
				"<cbc:ID>Prosjekt 13</cbc:ID>" +
				"</cac:OrderReference>" +
				"<cac:ContractDocumentReference>" +
				"<cbc:ID>K987654321</cbc:ID>" +
				"</cac:ContractDocumentReference>" +
				"</Invoice>";
	}

	@Test
	public void testXmlDOMToString() throws Exception {		
		Document xmlDoc = utils.stringToXMLDOM(xmlTestString);
		assertEquals(xmlTestString, utils.xmlDOMToString(xmlDoc));
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
		assertEquals("urn:www.cenbii.eu:profile:bii05:ver1.0", pId.getTextContent());
		assertEquals("urn:www.cenbii.eu:transaction:BiiCoreTrdm001:ver1.0:extentionId", cId.getTextContent());		
	}

	@Test
	public void testInnerXml() throws Exception {
		Document xmlDoc1 = utils.stringToXMLDOM(xmlTestString);
		Node name = utils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']/name").item(0);

		String myXML = "<name xmlns:xi=\"http://www.w3.org/2001/XInclude\">" +
				"<en>EHF invoice in Norway, profile invoice only</en>" +
				"<no>EHF faktura i Norge, profil kun faktura</no>" +
				"</name>";
		
		assertEquals(myXML, utils.innerXml(name));
	}
}
