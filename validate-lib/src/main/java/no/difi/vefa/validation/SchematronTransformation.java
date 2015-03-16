package no.difi.vefa.validation;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.MessageUtils;
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

    /**
     * Perform transformation of XML with XML.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     */
    public void main(Document xmlDoc, String xslFile, Messages messages) {
        try {
            // Transform XML with XSL
            no.difi.vefa.utils.xml.XmlXslTransformation xmlXslTransformation = new no.difi.vefa.utils.xml.XmlXslTransformation();
            Document result = xmlXslTransformation.main(xmlDoc, xslFile);

            // Loop result of transformation and add to Message collection
            NodeList failedAsserts = result.getElementsByTagName("svrl:failed-assert");

            for (int i = 0; i < failedAsserts.getLength(); i++) {
                Node failedAssert = failedAsserts.item(i);

                // Get SCHEMATRON element (failed-assert)
                String test = failedAssert.getAttributes().getNamedItem("test").getNodeValue();
                String location = failedAssert.getAttributes().getNamedItem("location").getNodeValue();
                String flag = failedAssert.getAttributes().getNamedItem("flag").getNodeValue();
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
                messages.addMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSL));
        }
    }

    /**
     * Tries to extract SCHEMATRON rule id from text
     *
     * @param text Text to search for SCHEMATRON rule
     * @return SCHEMATRON rule id as String
     */
    private String getSchematronRule(String text) {
        Matcher m = PATTERN.matcher(text);
        while (m.find()) {
            text = m.group().replace("[", "").replace("]", "");
        }
        return text;
    }
}
