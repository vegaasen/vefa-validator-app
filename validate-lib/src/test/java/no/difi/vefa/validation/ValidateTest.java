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
	
	@Before
	public void setUp() throws Exception {
		validate = new Validate();
		messages = new ArrayList<Message>();
	}
	
	@Test
	public void testMain() throws Exception {
		// Read properties
		PropertiesFile propFile = validate.getPropertiesFile();
		
		// Read test configuration
		String configDoc = new Scanner(new File(propFile.dataDir + "/STANDARD/configTestValidation.xml")).useDelimiter("\\Z").next();
		Utils utils = new Utils();
		Document xmlDoc = utils.stringToXMLDOM(configDoc);
		NodeList tests = utils.xmlDOMXPathQuery(xmlDoc, "/config/test");
		
		// Loop all test cases and perform validation
		for(int i=0; i<tests.getLength(); i++){
			Element test = (Element) tests.item(i);
			String nr = test.getAttributes().getNamedItem("nr").getNodeValue();
			String version = test.getElementsByTagName("version").item(0).getTextContent();
			String schema = test.getElementsByTagName("schema").item(0).getTextContent();
			String file = test.getElementsByTagName("file").item(0).getTextContent();
			
			System.out.println("Starting test of XML file no: " + nr);
			
			xml = new Scanner(new File(propFile.dataDir + file)).useDelimiter("\\Z").next();

			validate.version = version;
			validate.schema = schema;
			validate.xml = xml;
			validate.main();
			
			// Loop all errors in configuration and compare with validation result
			NodeList errors = test.getElementsByTagName("schematronrule");			
			for(int x=0; x<errors.getLength(); x++){
				Element error = (Element) errors.item(x);
				String schematronrule = error.getTextContent();
				
				assertEquals(schematronrule, validate.messages.get(x).schematronRuleId);
			}			
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
