package no.difi.vefa.validation;

import no.difi.vefa.message.Message;
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

public class FilterMessageTest {

    private String basePath;
    private Messages messages;
    private XmlUtils xmlUtils;

    @Before
    public void setUp() throws Exception {
        basePath = new java.io.File("src/test/resources/").getCanonicalPath();
        xmlUtils = new XmlUtils();
    }

    @Test
    public void testMain() throws Exception {
        Scanner scanner = new Scanner(new File(basePath + "/Invoice.xml"));
        String xml = scanner.useDelimiter("\\Z").next();
        Document xmlDoc = xmlUtils.stringToXMLDOM(xml);
        FilterMessage filterMessage = new FilterMessage();

        this.addMessage();
        filterMessage.main(xmlDoc, basePath + "/TestFilterMessageTrue.xsl", messages, "ASD-1234-BBB");
        assertEquals(0, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, basePath + "/TestFilterMessageTrue.xsl", messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, basePath + "/TestFilterMessageFalse.xsl", messages, "ASD-1234-BBB");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(xmlDoc, basePath + "/TestFilterMessageFalse.xsl", messages, "TEST-1234-TEST");
        assertEquals(1, messages.getMessages().size());

        this.addMessage();
        filterMessage.main(null, basePath + "/TestFilterMessageFalse.xsl", messages, "TEST-1234-TEST");
        assertEquals(2, messages.getMessages().size());

        scanner.close();
    }

    private void addMessage() {
        messages =new Messages();
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        message.setMessageType(MessageType.Fatal);
        message.setTitle("My test title");
        message.setDescription("My test description");
        message.setSchematronRuleId("ASD-1234-BBB");
        messages.addMessage(message);
    }

}
