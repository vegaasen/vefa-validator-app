package no.difi.vefa.validation;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class FilterMessageTest {

    public static final String TEST_FILTER_MESSAGE_FALSE_XSL = "TestFilterMessageFalse.xsl";
    public static final String TEST_FILTER_MESSAGE_TRUE_XSL = "TestFilterMessageTrue.xsl";
    private Messages messages;
    private XmlUtils xmlUtils;

    @Before
    public void setUp() throws Exception {
        xmlUtils = new XmlUtils();
    }

    @Test
    public void testMain() throws Exception {
        Document xmlDoc = xmlUtils.stringToXMLDOM(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));
        FilterMessage filterMessage = new FilterMessage();

        this.addMessage();
        filterMessage.main(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_TRUE_XSL).getPath(), messages, "ASD-1234-BBB");
        assertEquals(0, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_TRUE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "ASD-1234-BBB");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(null, ClassLoader.getSystemResource(TEST_FILTER_MESSAGE_FALSE_XSL).getPath(), messages, "TEST-1234-TEST");
        assertEquals(2, messages.getMessages().size());

    }

    private void addMessage() {
        messages = new Messages();
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        message.setMessageType(MessageType.Fatal);
        message.setTitle("My test title");
        message.setDescription("My test description");
        message.setSchematronRuleId("ASD-1234-BBB");
        messages.addMessage(message);
    }

}
