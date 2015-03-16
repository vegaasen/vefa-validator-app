package no.difi.vefa.validation;

import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.MessageUtils;
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
     * @param xmlDoc   XML as Document
     * @param xsdFile  Path to XSD file as String
     * @param messages List of messages
     */
    public void main(Document xmlDoc, String xsdFile, Messages messages) {
        try {
            no.difi.vefa.utils.xml.XSDValidation xsdValidation = new no.difi.vefa.utils.xml.XSDValidation();
            xsdValidation.main(xmlDoc, xsdFile);
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSD));
        }
    }
}
