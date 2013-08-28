package no.difi.vefa.validation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.w3c.dom.Document;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;

/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 */
public class RenderXsl {
	public Document result;
	
	/**
	 * Perform transformation of XML with XML.
	 * 
	 * @param xmlDoc XML as Document
	 * @param xslFile Path to XSL file as String
	 * @param  messages  List of messages
	 * @return Document Result of transformation as Document
	 */		
	public void main(Document xmlDoc, String xslFile, List<Message> messages) {
		try {
			// Transform XML with XSL			
			no.difi.vefa.xml.XmlXslTransformation xmlXslTransformation = new no.difi.vefa.xml.XmlXslTransformation();
			Document result = xmlXslTransformation.main(xmlDoc, xslFile);
			this.result = result;			
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			
			Message message = new Message();
			message.validationType = ValidationType.XSL;
			message.messageType = MessageType.Fatal;
			message.title = e.getMessage();
			message.description = exceptionAsString;			
			messages.add(message);
		}
	}
}
