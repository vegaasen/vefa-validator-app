package no.difi.vefa.validation;

import no.difi.vefa.message.Messages;
import no.difi.vefa.util.MessageUtils;

/**
 * This class can be used to check if an XML string is contains
 * valid XML.
 */
public class WellFormed {

    /**
     * Checks if xml string is valid XML.
     * Add a message to message collection if this is not the case.
     *
     * @param xml      String containing XML
     * @param messages List of messages
     * @return boolean Returns true or false
     */
    public boolean main(String xml, Messages messages) {
        try {
            no.difi.vefa.xml.WellFormed wellFormed = new no.difi.vefa.xml.WellFormed();
            wellFormed.main(xml);
            return true;
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e));
        }
        return false;
    }
}
