package no.difi.vefa.validation;

import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.Messages;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.util.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class SchematronTransformationTest {

    private String basePath;
    private XmlUtils xmlUtils;
    private SchematronTransformation schematronTransformation;

    @Before
    public void setUp() throws Exception {
        basePath = new java.io.File("src/test/resources/").getCanonicalPath();
        xmlUtils = new XmlUtils();
        schematronTransformation = new SchematronTransformation();
    }

    @Test
    public void testMain() throws Exception {
        Scanner scanner = new Scanner(new File(basePath + "/Invoice.xml"));
        String xml = scanner.useDelimiter("\\Z").next();
        Document xmlDoc = xmlUtils.stringToXMLDOM(xml);

        Messages messages = new Messages();
        schematronTransformation.main(xmlDoc, basePath + "/UBL-T10-EUgen.xsl", messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals("EUGEN-T10-R024", messages.getMessages().get(0).getSchematronRuleId());

        messages = new Messages();
        schematronTransformation.main(null, basePath + "/UBL-T10-EUgen.xsl", messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSL, messages.getMessages().get(0).getValidationType());

        scanner.close();
    }

}
