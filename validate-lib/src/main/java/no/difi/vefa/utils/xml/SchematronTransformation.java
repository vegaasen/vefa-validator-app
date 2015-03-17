package no.difi.vefa.utils.xml;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.xml.XmlXslTransformation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 */
public class SchematronTransformation {

    private static final Pattern PATTERN = Pattern.compile("\\[(.*?)\\]");
    private static final String SVRL_FAILED_ASSERT = "svrl:failed-assert";
    private static final String NODE_TEST = "test";
    private static final String NODE_LOCATION = "location";
    private static final String NODE_FLAG = "flag";
    private static final String EMPTY = "";
    private static final String R_BRACKET = "[";
    private static final String L_BRACKET = "]";

    /**
     * Perform transformation of XML with XML.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     */
    public static void validate(Document xmlDoc, String xslFile, Messages messages) {
        try {
            // Transform XML with XSL
            Document result = XmlXslTransformation.transform(xmlDoc, xslFile, messages);
            // Loop result of transformation and add to Message collection
            NodeList failedAsserts = result.getElementsByTagName(SVRL_FAILED_ASSERT);
            for (int i = 0; i < failedAsserts.getLength(); i++) {
                messages.addMessage(assembleMessage(failedAsserts.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSL));
        }
    }

    private static Message assembleMessage(Node failedAssert) {
        String test = failedAssert.getAttributes().getNamedItem(NODE_TEST).getNodeValue();
        String location = failedAssert.getAttributes().getNamedItem(NODE_LOCATION).getNodeValue();
        String flag = failedAssert.getAttributes().getNamedItem(NODE_FLAG).getNodeValue();
        String text = failedAssert.getTextContent();
        // Add failed-assert to message collection
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        if (flag.equals(MessageType.Fatal.toString().toLowerCase())) {
            message.setMessageType(MessageType.Fatal);
        } else if (flag.equals(MessageType.Warning.toString().toLowerCase())) {
            message.setMessageType(MessageType.Warning);
        }
        message.setTitle(text);
        message.setDescription("Test: " + test + ", Location: " + location);
        message.setSchematronRuleId(getSchematronRule(text));
        return message;
    }

    /**
     * Tries to extract SCHEMATRON rule id from text
     *
     * @param text Text to search for SCHEMATRON rule
     * @return SCHEMATRON rule id as String
     */
    private static String getSchematronRule(String text) {
        Matcher m = PATTERN.matcher(text);
        while (m.find()) {
            text = m.group().replace(R_BRACKET, EMPTY).replace(L_BRACKET, EMPTY);
        }
        return text;
    }
}
