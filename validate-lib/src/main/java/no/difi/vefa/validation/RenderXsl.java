package no.difi.vefa.validation;

import no.difi.vefa.message.Message;
import no.difi.vefa.util.MessageUtils;
import no.difi.vefa.xml.XmlXslTransformation;
import org.w3c.dom.Document;

import java.util.List;

/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 */
public class RenderXsl {
    public Document result;

    /**
     * Perform transformation of XML with XML.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     */
    public void main(Document xmlDoc, String xslFile, List<Message> messages) {
        try {
            XmlXslTransformation xmlXslTransformation = new XmlXslTransformation();
            this.result = xmlXslTransformation.main(xmlDoc, xslFile);
        } catch (Exception e) {
            messages.add(MessageUtils.translate(e));
        }
    }
}
