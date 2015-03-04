package no.difi.vefa.validation;

import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.Messages;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.util.PropertiesUtils;
import no.difi.vefa.util.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class XSDValidationTest {

    private String basePath;
    private XmlUtils xmlUtils;
    private String xml;
    private Document xmlDoc;
    private Messages messages;
    private XSDValidation xsdValidation;
    private PropertiesUtils propFile;
    private Validate validate;

    @Before
    public void setUp() throws Exception {
        basePath = new java.io.File("src/test/resources/").getCanonicalPath();
        xmlUtils = new XmlUtils();
        xsdValidation = new XSDValidation();
        validate = new Validate();
        propFile = validate.getPropertiesUtils();
    }

    @Test
    public void testMain() throws Exception {
        Scanner scanner;

        scanner = new Scanner(new File(basePath + "/TestXSD.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = xmlUtils.stringToXMLDOM(xml);
        messages = new Messages();
        xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(0, messages.getMessages().size());

        scanner = new Scanner(new File(basePath + "/TestXSDWithErrors.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = xmlUtils.stringToXMLDOM(xml);
        messages = new Messages();
        xsdValidation.main(xmlDoc, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());

        messages = new Messages();
        xsdValidation.main(null, basePath + "/TestXSD.xsd", messages, propFile);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());

        scanner.close();
    }

}
