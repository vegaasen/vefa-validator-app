package no.difi.vefa.validation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;

/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 */
public class SchematronTransformation {
	
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

			// Loop result of transformation and add to Message collection
			NodeList failedAsserts = result.getElementsByTagName("svrl:failed-assert");
			
			for(int i=0; i<failedAsserts.getLength(); i++){
				Node failedAssert = failedAsserts.item(i);
				
				// Get SCHEMATRON element (failed-assert)
				String test = failedAssert.getAttributes().getNamedItem("test").getNodeValue();
				String location = failedAssert.getAttributes().getNamedItem("location").getNodeValue();
				String flag = failedAssert.getAttributes().getNamedItem("flag").getNodeValue();
				String text = failedAssert.getTextContent();
				
				// Add failed-assert to message collection
				Message message = new Message();
				message.validationType = ValidationType.XSL;
				if (flag.equals(MessageType.Fatal.toString().toLowerCase())) {
					message.messageType = MessageType.Fatal;					
				}else if (flag.equals(MessageType.Warning.toString().toLowerCase())) {
					message.messageType = MessageType.Warning;
				}								
				message.title = text;
				message.description = "Test: " + test + ", Location: " + location;
				message.schematronRuleId = getSchematronRule(text);				
				messages.add(message);				
			}
			
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

	/**
	 * Tries to extract SCHEMATRON rule id from text
	 * 
	 * @param text Text to search for SCHEMATRON rule
	 * @return SCHEMATRON rule id as String
	 */	
	private String getSchematronRule(String text) {
		String r = "";
		
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(text);
		while(m.find()) {
			r = m.group().replace("[", "").replace("]", "");
		}
		
		return r;
	}
}
