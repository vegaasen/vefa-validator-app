package no.difi.vefa.validation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;

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
	 * @param xmlDoc XML as Document
	 * @param xsdFile Path to XSD file as String
	 * @param  messages  List of messages
	 * @param propertiesFile PropertiesFile
	 * @param  messages  Set of messages
	 */	
	public void main(Document xmlDoc, String xsdFile, List<Message> messages, PropertiesFile propertiesFile) {
		try {
			no.difi.vefa.xml.XSDValidation xsdValidation = new no.difi.vefa.xml.XSDValidation();
			xsdValidation.main(xmlDoc, xsdFile, propertiesFile);			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			
			Message message = new Message();
			message.validationType = ValidationType.XSD;
			message.messageType = MessageType.Fatal;
			message.title = e.getMessage();
			message.description = exceptionAsString;			
			messages.add(message);
		}
	}
}
