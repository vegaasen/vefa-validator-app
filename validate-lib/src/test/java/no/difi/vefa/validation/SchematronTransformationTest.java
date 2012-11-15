package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.xml.Utils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class SchematronTransformationTest {
	
	private String basePath;
	private Utils utils;
	private String xml;
	private Document xmlDoc;
	private List<Message> messages;
	private SchematronTransformation schematronTransformation;

	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		utils = new Utils();
		schematronTransformation = new SchematronTransformation();
	}

	@Test
	public void testMain() throws Exception {
		xml = new Scanner(new File(basePath + "/Invoice.xml")).useDelimiter("\\Z").next();
		xmlDoc	= utils.stringToXMLDOM(xml);												
				
		messages = new ArrayList<Message>();
		schematronTransformation.main(xmlDoc, basePath + "/UBL-T10-EUgen.xsl", messages);		
		assertEquals(1, messages.size());
		assertEquals("EUGEN-T10-R024", messages.get(0).schematronRuleId);		
		
		messages = new ArrayList<Message>();
		schematronTransformation.main(null, basePath + "/UBL-T10-EUgen.xsl", messages);
		assertEquals(1, messages.size());
		assertEquals(MessageType.Fatal, messages.get(0).messageType);
		assertEquals(ValidationType.XSL, messages.get(0).validationType);
	}

}
