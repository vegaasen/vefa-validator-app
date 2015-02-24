package no.difi.vefa.validation;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

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
        Scanner scanner;

        scanner = new Scanner(new File(basePath + "/TestXSD.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = utils.stringToXMLDOM(xml);
        messages = new ArrayList<Message>();
        xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(0, messages.size());

        scanner = new Scanner(new File(basePath + "/TestXSDWithErrors.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = utils.stringToXMLDOM(xml);
        messages = new ArrayList<Message>();
        xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(1, messages.size());
        assertEquals(MessageType.Fatal, messages.get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.get(0).getValidationType());

        messages = new ArrayList<Message>();
        xsdValidation.main(null, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(1, messages.size());
        assertEquals(MessageType.Fatal, messages.get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.get(0).getValidationType());

        scanner.close();
    }

}
