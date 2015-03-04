package no.difi.vefa.validation;

import no.difi.vefa.model.message.Messages;
import no.difi.vefa.utils.xml.XmlUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

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
        Messages messages = new Messages();
        XmlUtils xmlUtils = new XmlUtils();
        Scanner scanner;

        scanner = new Scanner(new File(basePath + "/Invoice.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = xmlUtils.stringToXMLDOM(xml);
        detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);
        assertEquals(1, messages.getMessages().size());

        scanner = new Scanner(new File(basePath + "/InvoiceMissingProfileID.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = xmlUtils.stringToXMLDOM(xml);
        messages = new Messages();
        detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());

        scanner = new Scanner(new File(basePath + "/InvoiceMissingCustomizationID.xml"));
        xml = scanner.useDelimiter("\\Z").next();
        xmlDoc = xmlUtils.stringToXMLDOM(xml);
        messages = new Messages();
        detectIdentifier.setVersionAndIdentifier(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());

        scanner.close();
    }

}
