package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Message;
import no.difi.vefa.validation.DetectSchema;
import no.difi.vefa.xml.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class DetectSchemaTest {
	
	private DetectSchema detectSchema;
	private String basePath;
	private String xml;
	
	@Before
	public void setUp() throws Exception {
		detectSchema = new DetectSchema();
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetSchemaIdentifier() throws Exception {				
		Document xmlDoc;
		String version = "1.4";
		List<Message> messages;
		Utils utils = new Utils();
		
		xml = new Scanner(new File(basePath + "/Invoice.xml")).useDelimiter("\\Z").next();
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectSchema.setSchemaIdentifier(xmlDoc, version, messages);		
		assertEquals(0, messages.size());
				
		xml = new Scanner(new File(basePath + "/InvoiceMissingProfileID.xml")).useDelimiter("\\Z").next();
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectSchema.setSchemaIdentifier(xmlDoc, version, messages);
		assertEquals(1, messages.size());
		
		xml = new Scanner(new File(basePath + "/InvoiceMissingCustomizationID.xml")).useDelimiter("\\Z").next();
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectSchema.setSchemaIdentifier(xmlDoc, version, messages);
		assertEquals(1, messages.size());		
	}

}
