package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XSDValidationTest {

	private String basePath;
	private Utils utils;
	private String xml;
	private Document xmlDoc;
	private List<Message> messages;
	private XSDValidation xsdValidation;
	private PropertiesFile propFile;
	private Validate validate;
	
	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		utils = new Utils();
		xsdValidation = new XSDValidation();
		validate = new Validate();
		propFile = validate.getPropertiesFile();		
	}

	@Test
	public void testMain() throws Exception {
		xml = new Scanner(new File(basePath + "/TestXSD.xml")).useDelimiter("\\Z").next();
		xmlDoc	= utils.stringToXMLDOM(xml);			
		messages = new ArrayList<Message>();
		xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
		assertEquals(0, messages.size());

		xml = new Scanner(new File(basePath + "/TestXSDWithErrors.xml")).useDelimiter("\\Z").next();
		xmlDoc	= utils.stringToXMLDOM(xml);		
		messages = new ArrayList<Message>();
		xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
		assertEquals(1, messages.size());
		assertEquals(MessageType.Fatal, messages.get(0).messageType);
		assertEquals(ValidationType.XSD, messages.get(0).validationType);
		
		messages = new ArrayList<Message>();
		xsdValidation.main(null, basePath + "/TestXSD.xsd", messages, propFile);
		assertEquals(1, messages.size());
		assertEquals(MessageType.Fatal, messages.get(0).messageType);
		assertEquals(ValidationType.XSD, messages.get(0).validationType);			
	}

}
