package no.difi.vefa.ws;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DetectSchemaTest {
	
	private DetectSchema detectSchema;
	private String basePath;
	private String xml;
	
	@Before
	public void setUp() throws Exception {
		detectSchema = new DetectSchema();
		basePath = new java.io.File("src/test/java/no/difi/vefa/ws/").getCanonicalPath();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetSchemaIdentifier() throws Exception {
		String version = "1.4";
		
		xml = new Scanner(new File(basePath + "/Invoice.xml")).useDelimiter("\\Z").next();
		detectSchema.setSchemaIdentifier(xml, version);		
		assertEquals(0, detectSchema.messages.size());
				
		xml = new Scanner(new File(basePath + "/InvoiceMissingProfileID.xml")).useDelimiter("\\Z").next();
		detectSchema.setSchemaIdentifier(xml, version);
		assertEquals(1, detectSchema.messages.size());
	}

}
