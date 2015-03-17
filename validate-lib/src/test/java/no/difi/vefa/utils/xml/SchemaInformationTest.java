package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.utils.xml.SchemaInformation;
import no.difi.vefa.utils.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class SchemaInformationTest {

    private SchemaInformation detectIdentifier;

    @Before
    public void setUp() throws Exception {
        detectIdentifier = new SchemaInformation();
    }

    @Test
    public void testSetVersionAndIdentifier() throws Exception {
        Document xmlDoc;
        Messages messages = new Messages();
        XmlUtils xmlUtils = new XmlUtils();

        xmlDoc = xmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));
        detectIdentifier.detectSchemaInformation(xmlDoc, messages);
        assertEquals(1, messages.getMessages().size());

        xmlDoc = xmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("InvoiceMissingProfileID.xml"),
                Charsets.UTF_8)));
        messages = new Messages();
        detectIdentifier.detectSchemaInformation(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());

        xmlDoc = xmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("InvoiceMissingCustomizationID.xml"),
                Charsets.UTF_8)));
        messages = new Messages();
        detectIdentifier.detectSchemaInformation(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());
    }

}
