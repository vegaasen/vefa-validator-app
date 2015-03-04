package no.difi.vefa.validation;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.xml.XmlXslTransformation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

/**
 * This class can be used to filter messages from message collection. The filtering
 * is based on an XSL transformation with the XML document.
 */
public class FilterMessage {

    /**
     * Filter messages from message collection.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     * @param rule     What SCEMATRON rule to filter as String
     */
    public void main(Document xmlDoc, String xslFile, Messages messages, String rule) {
        try {
            // Status
            Boolean status = false;

            // Transform XML with XSL and return status of XSL check
            XmlXslTransformation xmlXslTransformation = new XmlXslTransformation();
            Document result = xmlXslTransformation.main(xmlDoc, xslFile);

            // Get status from XML/XSL transformation
            NodeList statusNodeList = result.getElementsByTagName("status");
            for (int i = 0; i < statusNodeList.getLength(); i++) {
                Node statusNode = statusNodeList.item(i);
                status = "true".equals(statusNode.getTextContent());
            }

            // If result from XSL transformation is true then remove message from message collection where title = rule
            if (status) {
                for (Iterator<Message> iterator = messages.getMessages().iterator(); iterator.hasNext(); ) {
                    Message message = iterator.next();

                    if (message.getSchematronRuleId().equals(rule)) {
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e));
        }
    }
}
