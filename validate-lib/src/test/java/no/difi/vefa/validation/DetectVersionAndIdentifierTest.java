package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Message;
import no.difi.vefa.validation.DetectVersionAndIdentifier;
import no.difi.vefa.xml.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class DetectVersionAndIdentifierTest {
	
	private DetectVersionAndIdentifier detectIdentifier;
	private String basePath;
	private String xml;
	
	@Before
	public void setUp() throws Exception {
		detectIdentifier = new DetectVersionAndIdentifier();
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetVersionAndIdentifier() throws Exception {				
		Document xmlDoc;
		List<Message> messages;
		Utils utils = new Utils();
		Scanner scanner;
		
		scanner = new Scanner(new File(basePath + "/Invoice.xml"));
		xml = scanner.useDelimiter("\\Z").next();
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);		
		assertEquals(1, messages.size());

		scanner = new Scanner(new File(basePath + "/InvoiceMissingProfileID.xml"));
		xml = scanner.useDelimiter("\\Z").next();		
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);
		assertEquals(2, messages.size());
		
		scanner = new Scanner(new File(basePath + "/InvoiceMissingCustomizationID.xml"));
		xml = scanner.useDelimiter("\\Z").next();		
		xmlDoc = utils.stringToXMLDOM(xml);
		messages = new ArrayList<Message>();
		detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);
		assertEquals(2, messages.size());	
		
		scanner.close();
	}

}
