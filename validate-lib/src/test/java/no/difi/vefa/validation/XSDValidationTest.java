package no.difi.vefa.validation;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class XSDValidationTest {

    private XmlUtils xmlUtils;
    private Document xmlDoc;
    private Messages messages;
    private XSDValidation xsdValidation;
    private Validate validate;

    @Before
    public void setUp() throws Exception {
        System.setProperty("no.difi.vefa.validation.configuration.datadir", ClassLoader.getSystemResource("validator.properties").getPath());
        xmlUtils = new XmlUtils();
        xsdValidation = new XSDValidation();
        validate = new Validate();
    }

    @Test
    public void testMain() throws Exception {
        xmlDoc = xmlUtils.stringToXMLDOM(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("TestXSD.xml"),
                Charsets.UTF_8)));
        messages = new Messages();
        xsdValidation.main(xmlDoc, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(0, messages.getMessages().size());

        xmlDoc = xmlUtils.stringToXMLDOM(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("TestXSDWithErrors.xml"),
                Charsets.UTF_8)));
        messages = new Messages();
        xsdValidation.main(xmlDoc, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());

        messages = new Messages();
        xsdValidation.main(null, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());
    }

}
