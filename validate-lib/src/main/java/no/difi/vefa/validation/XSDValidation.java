package no.difi.vefa.validation;

import no.difi.vefa.message.Messages;
import no.difi.vefa.util.PropertiesUtils;
import no.difi.vefa.util.MessageUtils;
import org.w3c.dom.Document;

/**
 * This class can be used to check if an XML Document
 * validate against a XSD Document.
 */
public class XSDValidation {

    /**
     * Perform validation of XML Document with a XSD Document.
     * Add a message to message collection if this is not the case.
     *
     * @param xmlDoc         XML as Document
     * @param xsdFile        Path to XSD file as String
     * @param messages       List of messages
     * @param propertiesUtils PropertiesFile
     */
    public void main(Document xmlDoc, String xsdFile, Messages messages, PropertiesUtils propertiesUtils) {
        try {
            no.difi.vefa.util.xml.XSDValidation xsdValidation = new no.difi.vefa.util.xml.XSDValidation();
            xsdValidation.main(xmlDoc, xsdFile, propertiesUtils);
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e));
        }
    }
}
