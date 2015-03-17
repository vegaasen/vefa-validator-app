package no.difi.vefa.utils.xml;

import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.MessageUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

public class WellFormatted {

    private WellFormatted() {
    }

    /**
     * Checks if xml string is valid XML.
     * Add a message to message collection if this is not the case.
     *
     * @param xml      String containing XML
     * @param messages List of messages
     * @return boolean Returns true or false
     */
    public static boolean isValidXml(String xml, Messages messages) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.parse(new InputSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XMLWellFormed));
        }
        return false;
    }

}
