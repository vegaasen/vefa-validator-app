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

public class SchematronTransformationTest {

    private XmlUtils xmlUtils;
    private SchematronTransformation schematronTransformation;

    @Before
    public void setUp() throws Exception {
        xmlUtils = new XmlUtils();
        schematronTransformation = new SchematronTransformation();
    }

    @Test
    public void testMain() throws Exception {
        Document xmlDoc = xmlUtils.stringToXMLDOM(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));

        Messages messages = new Messages();
        schematronTransformation.main(xmlDoc, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals("EUGEN-T10-R024", messages.getMessages().get(0).getSchematronRuleId());

        messages = new Messages();
        schematronTransformation.main(null, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSL, messages.getMessages().get(0).getValidationType());
    }

}
