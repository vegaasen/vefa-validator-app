package no.difi.vefa.validation;

import java.util.ArrayList;
import java.util.List;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.xml.Utils;
import no.difi.vefa.xml.Utils.XMLNamespace;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class is used to auto detect schema identifier.
 */
public class DetectSchema {
	/**
	 * Schema to validate XML against.
	 */	
	public String schema;

	/**
	 * Indicates if a schema detection was made.
	 */	
	public Boolean detected = true;
	
	/**
	 * Tries to detect schema identifier from XML. This consists of ProfileID and CustomizationID.
	 * 
	 * @param xmlDoc XML as Document
	 * @param version Version as String
	 * @param  messages  List of messages
	 * @return Schema identifier as String
	 * @throws Exception
	 */		
	public void setSchemaIdentifier(Document xmlDoc, String version, List<Message> messages) throws Exception {
		// Setup
		Utils utils = new Utils();
		String profileId = "";
		String customizationId = "";
				
		// Add XML Namespace
		List<XMLNamespace> namespaces = new ArrayList<XMLNamespace>();		
		XMLNamespace ns = utils.new XMLNamespace();
		ns.prefix = "cbc";
		ns.url = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
		namespaces.add(ns);
		
		// Get ProfileID and CustomizationID from xml
		Node pId = utils.xmlDOMXPathQueryWithNS(xmlDoc, "*/cbc:ProfileID", namespaces).item(0);
		Node cId = utils.xmlDOMXPathQueryWithNS(xmlDoc, "*/cbc:CustomizationID", namespaces).item(0);

		if (pId == null) {
			Message message = new Message();
			message.validationType = ValidationType.XMLWellFormed;
			message.messageType = MessageType.Fatal;
			message.title = "ProfileID is missing from XML document. Unable to choose validation profile.";
			message.description = "";			
			messages.add(message);
			this.detected = false;
		} else {
			profileId = pId.getTextContent();
		}
		
		if (cId == null) {
			Message message = new Message();
			message.validationType = ValidationType.XMLWellFormed;
			message.messageType = MessageType.Fatal;
			message.title = "CustomizationID is missing from XML document. Unable to choose validation profile.";
			message.description = "";			
			messages.add(message);
			this.detected = false;
		} else {
			customizationId = cId.getTextContent();
		}
		
		// Build schema id				
		this.schema = profileId + "#" + customizationId;				
	}	
}
