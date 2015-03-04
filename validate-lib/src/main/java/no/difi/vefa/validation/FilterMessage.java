package no.difi.vefa.validation;

import java.util.Iterator;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.Messages;
import no.difi.vefa.util.MessageUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class can be used to filter messages from message collection. The filtering
 * is based on an XSL transformation with the XML document.
 */
public class FilterMessage {

	/**
	 * Filter messages from message collection.
	 * 
	 * @param xmlDoc XML as Document
	 * @param xslFile Path to XSL file as String
	 * @param  messages  List of messages
	 * @param  rule  What SCEMATRON rule to filter as String
	 */
	public void main(Document xmlDoc, String xslFile, Messages messages, String rule) {
		try {
			// Status
			Boolean status = false;
			
			// Transform XML with XSL and return status of XSL check		
			no.difi.vefa.util.xml.XmlXslTransformation xmlXslTransformation = new no.difi.vefa.util.xml.XmlXslTransformation();
			Document result = xmlXslTransformation.main(xmlDoc, xslFile);		
								
			// Get status from XML/XSL transformation			
			NodeList statusNodeList = result.getElementsByTagName("status");
			for(int i=0; i<statusNodeList.getLength(); i++){
				Node statusNode = statusNodeList.item(i);
				status = "true".equals(statusNode.getTextContent());	
			}						
						
			// If result from XSL transformation is true then remove message from message collection where title = rule
			if (status) {
			    for (Iterator<Message> iterator = messages.getMessages().iterator(); iterator.hasNext();) {
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
