package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.schema.Schema;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SchemaInformationTest {

    @BeforeClass
    public static void initialize() {
        System.setProperty(DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath());
    }

    @Test
    public void detectSchemaInformation_singleError() throws Exception {
        Messages messages = new Messages();
        Document xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));
        SchemaInformation.detectSchemaInformation(xmlDoc, messages);
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void detectSchemaInformation_twoErrors_missingProfileId() throws Exception {
        Messages messages = new Messages();
        Document xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("InvoiceMissingProfileID.xml"),
                Charsets.UTF_8)));
        SchemaInformation.detectSchemaInformation(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());
    }

    @Test
    public void detectSchemaInformation_twoErrors_missingCustId() throws Exception {
        Messages messages = new Messages();
        Document xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("InvoiceMissingCustomizationID.xml"),
                Charsets.UTF_8)));
        Schema result = SchemaInformation.detectSchemaInformation(xmlDoc, messages);
        assertEquals(2, messages.getMessages().size());
        assertFalse(result.isDetected());
    }

}
