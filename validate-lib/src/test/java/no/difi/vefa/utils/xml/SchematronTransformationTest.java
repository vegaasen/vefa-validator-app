package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class SchematronTransformationTest {

    @Test
    public void testMain() throws Exception {
        Document xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));

        Messages messages = new Messages();
        SchematronTransformation.validate(xmlDoc, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals("EUGEN-T10-R024", messages.getMessages().get(0).getSchematronRuleId());

        messages = new Messages();
        SchematronTransformation.validate(null, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSL, messages.getMessages().get(0).getValidationType());
    }

}
