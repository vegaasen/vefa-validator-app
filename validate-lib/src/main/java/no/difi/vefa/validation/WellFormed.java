package no.difi.vefa.validation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;

/**
 * This class can be used to check if an XML string is contains
 * valid XML.
 */
public class WellFormed {
	
	/**
	 * Checks if xml string is valid XML.  
	 * Add a message to message collection if this is not the case.
	 *
	 * @param  xml  String containing XML
	 * @param  messages  List of messages
	 * @return boolean Returns true or false
	 */	
	public boolean main(String xml, List<Message> messages) {
		boolean r = false;
		try {
			no.difi.vefa.xml.WellFormed wellFormed = new no.difi.vefa.xml.WellFormed();		
			wellFormed.main(xml);
			r = true;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			
			Message message = new Message();
			message.validationType = ValidationType.XMLWellFormed;
			message.messageType = MessageType.Fatal;
			message.title = e.getMessage();
			message.description = exceptionAsString;			
			messages.add(message);
			r = false;
		}
		return r;				
	}
}
