package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Hint;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ValidateTest {

	private Validate validate;
	private List<Message> messages;
	private String xml;
	private PropertiesFile propFile;
	private String basePath;
	
	@Before
	public void setUp() throws Exception {
		validate = new Validate();
		messages = new ArrayList<Message>();
		propFile = validate.getPropertiesFile();
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
	}
	
	@Test
	public void testNotWellFormedXML() throws Exception {
		validate = new Validate();
		validate.version = "test";
		validate.schema = "test";
		validate.xml = "<test>notwellformed</testtest>";
		validate.main();
		
		assertEquals(MessageType.Fatal, validate.messages.get(0).messageType);
	}

	@Test
	public void testIfValidationDefinitionIsNotPresentInConfigurationFile() throws Exception {
		validate = new Validate();
		validate.version = "test";
		validate.schema = "test";
		validate.xml = "<test>we do not find any validation definition</test>";
		validate.main();
		
		assertEquals(MessageType.Fatal, validate.messages.get(0).messageType);
	}
	
	@Test
	public void testAutoDetectionOfSchema() throws Exception {
		xml = new Scanner(new File(basePath + "/Invoice.xml")).useDelimiter("\\Z").next();
		
		validate = new Validate();
		validate.autodetectSchema = true;
		validate.version = "1.4";
		validate.xml = xml;
		validate.main();		
	}	

	@Test
	public void testFilesFromTestConfiguration() throws Exception {					
		// Read test configuration
		String configDoc = new Scanner(new File(propFile.dataDir + "/STANDARD/configTestValidation.xml")).useDelimiter("\\Z").next();
		Utils utils = new Utils();
		Document xmlDoc = utils.stringToXMLDOM(configDoc);
		NodeList tests = utils.xmlDOMXPathQuery(xmlDoc, "/config/test");
		
		// Loop all test cases and perform validation
		for(int i=0; i<tests.getLength(); i++){
			Element test = (Element) tests.item(i);
			String id = test.getAttributes().getNamedItem("id").getNodeValue();
			Boolean ignore = "true".equals(test.getAttributes().getNamedItem("ignore").getNodeValue());
			String version = test.getElementsByTagName("version").item(0).getTextContent();
			String schema = test.getElementsByTagName("schema").item(0).getTextContent();
			String file = test.getElementsByTagName("file").item(0).getTextContent();
			
			if (ignore == false) {
				System.out.println("\nStarting test of XML file no: " + id);
				
				xml = new Scanner(new File(propFile.dataDir + file)).useDelimiter("\\Z").next();
				
				validate = new Validate();
				validate.version = version;
				validate.schema = schema;
				validate.xml = xml;
				validate.main();

				// Get all error rules
				NodeList errors = utils.xmlDOMXPathQuery(xmlDoc, "/config/test[@id='" + id + "']/errors/schematronrule");												

				System.out.println("\tStarting errortesting based on result:");
				
				// Loop all errors in result and compare with configuration
				compareResultWithConfiguration(errors, "Starting assertion of error: ", MessageType.Fatal);
				
				System.out.println("\tStarting errortesting based on configuration:");
				
				// Loop all errors in configuration and compare with validation result
				compareConfigurationWithResult(errors, "Starting assertion of error: ", MessageType.Fatal);
				
				
				// Get all warning rules
				NodeList warnings = utils.xmlDOMXPathQuery(xmlDoc, "/config/test[@id='" + id + "']/warnings/schematronrule");												

				System.out.println("\tStarting warningtesting based on result:");
				
				// Loop all warnings in result and compare with configuration
				compareResultWithConfiguration(warnings, "Starting assertion of warning: ", MessageType.Warning);
				
				System.out.println("\tStarting warningtesting based on configuration:");
				
				// Loop all warnings in configuration and compare with validation result
				compareConfigurationWithResult(warnings, "Starting assertion of warning: ", MessageType.Warning);								
				
				System.out.println("");				
			} else {
				System.out.println("\nIgnoring test of XML file no: " + id);
			}
		}				
	}

	private void compareResultWithConfiguration(NodeList errors, String msg, MessageType messageType) {
		List<Message> tmpMessages = new ArrayList<Message>();
		for (int i = 0; i < validate.messages.size(); i++) {
			if (validate.messages.get(i).messageType == messageType) {
				tmpMessages.add(validate.messages.get(i));
			}			
		}

		for(int x=0; x<tmpMessages.size(); x++){
			if (tmpMessages.get(x).messageType == messageType) {				
				System.out.println("\t\t" + msg + tmpMessages.get(x).schematronRuleId);
				
				Element error = (Element) errors.item(x);
				String schematronrule = error.getTextContent();			
				
				assertEquals(schematronrule, tmpMessages.get(x).schematronRuleId);
			}
		}
	}

	private void compareConfigurationWithResult(NodeList errors, String msg, MessageType messageType) {
		List<Message> tmpMessages = new ArrayList<Message>();
		for (int i = 0; i < validate.messages.size(); i++) {
			if (validate.messages.get(i).messageType == messageType) {
				tmpMessages.add(validate.messages.get(i));
			}			
		}
		
		for(int x=0; x<errors.getLength(); x++){
			Element error = (Element) errors.item(x);
			String schematronrule = error.getTextContent();

			System.out.println("\t\t" + msg + schematronrule);
			
			assertEquals(schematronrule, tmpMessages.get(x).schematronRuleId);
		}
	}

	@Test
	public void testMessagesAsXML() throws Exception {
		Message message = new Message();
		message.validationType = ValidationType.Configuration;
		message.messageType = MessageType.Fatal;
		message.title = "My test title";
		message.description = "My test description";
		message.schematronRuleId = "ASD-1234-BBB";

		Hint hint = new Hint();
		hint.title = "My hint title";
		hint.description = "My hint description";
		
		message.hints.add(hint);
		
		this.messages.add(message);
		
		validate.version = "1.4";
		validate.schema = "testschema";
		validate.messages = this.messages;
		validate.suppressWarnings = false;
		
		String expected = "<messages><message schema=\"testschema\" validationType=\"Configuration\" version=\"1.4\">" +
				"<messageType>Fatal</messageType><title>My test title</title><description>My test description</description>" +
				"<schematronRuleId>ASD-1234-BBB</schematronRuleId><hints><hint><title>My hint title</title>" +
				"<description>My hint description</description></hint></hints></message></messages>";
		
		assertEquals(expected, validate.messagesAsXML());
	}

}
