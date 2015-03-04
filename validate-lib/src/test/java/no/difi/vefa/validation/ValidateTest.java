package no.difi.vefa.validation;

import no.difi.vefa.message.Hint;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.Messages;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class ValidateTest {

    private Validate validate;
    private Messages messages;
    private String xml;
    private PropertiesFile propFile;
    private String basePath;

    @Before
    public void setUp() throws Exception {
        validate = new Validate();
        messages = new Messages();
        propFile = validate.getPropertiesFile();
        basePath = new java.io.File("src/test/resources/").getCanonicalPath();
    }

    @Test
    public void testNotWellFormedXML() throws Exception {
        validate = new Validate();
        validate.version = "test";
        validate.id = "test";
        validate.xml = "<test>notwellformed</testtest>";
        validate.validate();

        assertEquals(MessageType.Fatal, validate.messages.getMessages().get(0).getMessageType());
    }

    @Test
    public void testIfValidationDefinitionIsNotPresentInConfigurationFile() throws Exception {
        validate = new Validate();
        validate.version = "test";
        validate.id = "test";
        validate.xml = "<test>we do not find any validation definition</test>";
        validate.validate();

        assertEquals(MessageType.Fatal, validate.messages.getMessages().get(0).getMessageType());
    }

    @Test
    public void testAutoDetectionOfVersionAndSchema() throws Exception {
        Scanner scanner = new Scanner(new File(basePath + "/Invoice.xml"));
        xml = scanner.useDelimiter("\\Z").next();

        validate = new Validate();
        validate.autodetectVersionAndIdentifier = true;
        validate.xml = xml;
        validate.validate();

        scanner.close();
    }

    @Test
    public void testFilesFromTestConfiguration() throws Exception {
        Utils utils = new Utils();
        Scanner scanner;

        // Read standard test configuration
        scanner = new Scanner(new File(propFile.dataDir + "/STANDARD/configTestValidation.xml"));
        String standardConfigDoc = scanner.useDelimiter("\\Z").next();
        Document standardXmlDoc = utils.stringToXMLDOM(standardConfigDoc);
        NodeList standardTests = utils.xmlDOMXPathQuery(standardXmlDoc, "/config/test");

        // Read custom test configuration
        scanner = new Scanner(new File(propFile.dataDir + "/CUSTOM/configTestValidation.xml"));
        String customConfigDoc = scanner.useDelimiter("\\Z").next();
        Document customXmlDoc = utils.stringToXMLDOM(customConfigDoc);
        NodeList customTests = utils.xmlDOMXPathQuery(customXmlDoc, "/config/test");

        // Run tests
        this.validateTestFiles(standardTests, standardXmlDoc);
        this.validateTestFiles(customTests, customXmlDoc);

        scanner.close();
    }

    private void validateTestFiles(NodeList tests, Document xmlDoc) throws Exception {
        Utils utils = new Utils();

        // Loop all test cases and perform validation
        for (int i = 0; i < tests.getLength(); i++) {
            Element test = (Element) tests.item(i);
            String id = test.getAttributes().getNamedItem("id").getNodeValue();
            Boolean ignore = "true".equals(test.getAttributes().getNamedItem("ignore").getNodeValue());
            String version = test.getElementsByTagName("version").item(0).getTextContent();
            String schemaId = test.getElementsByTagName("id").item(0).getTextContent();
            String file = test.getElementsByTagName("file").item(0).getTextContent();

            if (ignore == false) {
                System.out.println("\nStarting test of XML file no: " + id);

                Scanner scanner = new Scanner(new File(propFile.dataDir + file));
                xml = scanner.useDelimiter("\\Z").next();
                scanner.close();

                validate = new Validate();
                validate.version = version;
                validate.id = schemaId;
                validate.xml = xml;
                validate.validate();

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
        for (int i = 0; i < validate.messages.getMessages().size(); i++) {
            if (validate.messages.getMessages().get(i).getMessageType() == messageType) {
                tmpMessages.add(validate.messages.getMessages().get(i));
            }
        }

        for (int x = 0; x < tmpMessages.size(); x++) {
            if (tmpMessages.get(x).getMessageType() == messageType) {
                System.out.println("\t\t" + msg + tmpMessages.get(x).getSchematronRuleId());

                Element error = (Element) errors.item(x);
                String schematronrule = error.getTextContent();

                assertEquals(schematronrule, tmpMessages.get(x).getSchematronRuleId());
            }
        }
    }

    private void compareConfigurationWithResult(NodeList errors, String msg, MessageType messageType) {
        List<Message> tmpMessages = new ArrayList<Message>();
        for (int i = 0; i < validate.messages.getMessages().size(); i++) {
            if (validate.messages.getMessages().get(i).getMessageType() == messageType) {
                tmpMessages.add(validate.messages.getMessages().get(i));
            }
        }

        for (int x = 0; x < errors.getLength(); x++) {
            Element error = (Element) errors.item(x);
            String schematronrule = error.getTextContent();

            System.out.println("\t\t" + msg + schematronrule);

            assertEquals(schematronrule, tmpMessages.get(x).getSchematronRuleId());
        }
    }

    @Test
    public void testMessagesAsXML() throws Exception {
        Message message = new Message();
        message.setValidationType(ValidationType.Configuration);
        message.setMessageType(MessageType.Fatal);
        message.setTitle("My test title");
        message.setDescription("My test description");
        message.setSchematronRuleId("ASD-1234-BBB");

        Hint hint = new Hint();
        hint.setTitle("My hint title");
        hint.setDescription("My hint description");

        message.addHint(hint);

        this.messages.addMessage(message);

        validate.version = "1.4";
        validate.id = "testschema";
        validate.messages = this.messages;
        validate.suppressWarnings = false;

        String expected = "<messages id=\"testschema\" valid=\"false\" version=\"1.4\"><message validationType=\"Configuration\">" +
                "<messageType>Fatal</messageType><title>My test title</title><description>My test description</description>" +
                "<schematronRuleId>ASD-1234-BBB</schematronRuleId><hints><hint><title>My hint title</title>" +
                "<description>My hint description</description></hint></hints></message></messages>";

        assertEquals(expected, validate.messagesAsXML());
    }

}
