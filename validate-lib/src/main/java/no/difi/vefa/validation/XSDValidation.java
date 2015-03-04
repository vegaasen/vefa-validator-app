package no.difi.vefa.validation;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.Messages;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.util.MessageUtils;
import org.w3c.dom.Document;

import java.util.List;

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
     * @param propertiesFile PropertiesFile
     */
    public void main(Document xmlDoc, String xsdFile, Messages messages, PropertiesFile propertiesFile) {
        try {
            no.difi.vefa.xml.XSDValidation xsdValidation = new no.difi.vefa.xml.XSDValidation();
            xsdValidation.main(xmlDoc, xsdFile, propertiesFile);
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e));
        }
    }
}
