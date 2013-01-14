package no.difi.vefa.xml;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WellFormedTest {

	private String xmlTestString;	

	@Before
	public void setUp() throws Exception {
		xmlTestString = "<config xmlns:xi=\"http://www.w3.org/2001/XInclude\"><validate " +
				"id=\"urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1\" " +
				"version=\"1.4\">" +
				"<name>" +
				"<en>EHF invoice in Norway, profile invoice only</en>" +
				"<no>EHF faktura i Norge, profil kun faktura</no>" +
				"</name>" +
				"</validate>" +
				"</config>";		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMain() throws Exception {
		no.difi.vefa.xml.WellFormed wellFormed = new no.difi.vefa.xml.WellFormed();
		
		try {			
			wellFormed.main(xmlTestString);
		} catch (Exception e) {
			fail("XML is not wellformed");
		}		
	}
}
