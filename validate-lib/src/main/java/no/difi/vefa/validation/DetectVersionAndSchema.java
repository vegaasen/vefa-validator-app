package no.difi.vefa.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;
import no.difi.vefa.xml.Utils.XMLNamespace;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is used to auto detect version and schema identifier.
 */
public class DetectVersionAndSchema {
	/**
	 * Schema to validate XML against.
	 */	
	public String schema;
	
	/**
	 * Version to validate XML against.
	 */
	public String version;	

	/**
	 * Indicates if a schema detection was made.
	 */	
	public Boolean detected = true;

	/**
	 * Tries to detect version and schema identifier from XML.
	 * 
	 * @param xmlDoc XML as Document
	 * @param  messages  List of messages
	 * @throws Exception
	 */
	public void setVersionAndSchemaIdentifier(Document xmlDoc, List<Message> messages) throws Exception {
		this.setSchemaIdentifier(xmlDoc, messages);
		this.setVersionIdentifier(xmlDoc, messages);
	}
		
	/**
	 * Tries to detect schema identifier from XML. This consists of ProfileID and CustomizationID.
	 * 
	 * @param xmlDoc XML as Document
	 * @param  messages  List of messages
	 * @throws Exception
	 */		
	private void setSchemaIdentifier(Document xmlDoc, List<Message> messages) throws Exception {
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
	
	/**
	 * Tries to detect what version in configuration to validate against. Does this by selecting 
	 * all available identifiers (schema) and using the one with highest version number.
	 * 
	 * @param xmlDoc XML as Document
	 * @param  messages  List of messages
	 * @throws Exception
	 */	
	private void setVersionIdentifier(Document xmlDoc, List<Message> messages) throws Exception {
		// Setup
		Configuration configuration = new Configuration();
		Validate validate = new Validate();
		Utils utils = new Utils();
		PropertiesFile propertiesFile = new PropertiesFile();
		propertiesFile = validate.getPropertiesFile();
		
		// Select all schemas in configuration files
		Document standardXmlDoc = configuration.fileToXMLDOM(propertiesFile.dataDir + "/STANDARD/config.xml", propertiesFile);
		Document customXmlDoc = configuration.fileToXMLDOM(propertiesFile.dataDir + "/CUSTOM/config.xml", propertiesFile);
		NodeList standardValidates = utils.xmlDOMXPathQuery(standardXmlDoc, "/config/validate[@id='" + this.schema + "']");
		NodeList customValidates = utils.xmlDOMXPathQuery(customXmlDoc, "/config/validate[@id='" + this.schema + "']");
				
		if (standardValidates.getLength() == 0 && customValidates.getLength() == 0) {
			Message message = new Message();
			message.validationType = ValidationType.Configuration;
			message.messageType = MessageType.Fatal;
			message.title = "No validation definition is found in configuration.";
			message.description = "No entry is found in configuration for identificator '" + this.schema + "', unable to perform validation!";			
			messages.add(message);
			this.detected = false;
		} else {
			// Get highest version number
			ArrayList<String> list = new ArrayList<String>();
			
			for(int i=0; i<standardValidates.getLength(); i++){
				Element v = (Element) standardValidates.item(i);
				list.add(v.getAttribute("version"));
			}
			
			for(int i=0; i<customValidates.getLength(); i++){
				Element v = (Element) customValidates.item(i);
				list.add(v.getAttribute("version"));
			}			
			
			Collections.sort(list, Collections.reverseOrder());
			
			// Set version identifier
			this.version = list.get(0);
		}		
	}
	
}
