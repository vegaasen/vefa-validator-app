package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import no.difi.vefa.cache.TransformerCache;
import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class XslUtilsTest {

    private static final String TEST_FILTER_MESSAGE_FALSE_XSL = "TestFilterMessageFalse.xsl";
    private static final String TEST_FILTER_MESSAGE_TRUE_XSL = "TestFilterMessageTrue.xsl";

    private Document xmlDoc;
    private String xmlFile;

    @Before
    public void setUp() throws Exception {
        xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));
        InputStream stream = ClassLoader.getSystemResourceAsStream("Invoice.xml");
        xmlFile = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
    }

    @Test
    public void validate_normalProcedure() throws Exception {
        Messages messages = new Messages();
        XslUtils.validate(xmlDoc, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals("EUGEN-T10-R024", messages.getMessages().get(0).getSchematronRuleId());

        TransformerCache.clear();
        messages = new Messages();
        XslUtils.validate(null, ClassLoader.getSystemResource("UBL-T10-EUgen.xsl").getPath(), messages);
        assertEquals(0, messages.getMessages().size());
    }

    @Test
    public void filterMessages_normalProcedure_ok() throws Exception {
        Messages messages = addMessage();
        XslUtils.filterMessages(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_TRUE_XSL).getPath(), messages, "ASD-1234-BBB");
        assertEquals(0, messages.getMessages().size());
    }

    @Test
    public void filterMessages_normalProcedure_notFound() throws Exception {
        Messages messages = addMessage();
        XslUtils.filterMessages(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_TRUE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void filterMessages_normalProcedure_notFound_2() throws Exception {
        Messages messages = addMessage();
        XslUtils.filterMessages(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "ASD-1234-BBB");
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void filterMessages_normalProcedure_notFound_3() throws Exception {
        Messages messages = addMessage();
        XslUtils.filterMessages(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void filterMessages_normalProcedure_notFound_4() throws Exception {
        Messages messages = addMessage();
        XslUtils.filterMessages(null, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void testMain() throws Exception {
        Document xmlDoc = XmlUtils.stringToDocument(xmlFile);

        Document result = XslUtils.transform(xmlDoc, ClassLoader.getSystemResource("Test.xsl").getPath(), null);

        assertEquals("true", result.getElementsByTagName("status").item(0).getTextContent());
    }

    private Messages addMessage() {
        Messages messages = new Messages();
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        message.setMessageType(MessageType.Fatal);
        message.setTitle("My test title");
        message.setDescription("My test description");
        message.setSchematronRuleId("ASD-1234-BBB");
        messages.addMessage(message);
        return messages;
    }

}
