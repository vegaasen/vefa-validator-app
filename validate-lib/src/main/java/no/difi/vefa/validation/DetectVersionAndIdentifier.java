package no.difi.vefa.validation;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.Messages;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;
import no.difi.vefa.xml.Utils.XMLNamespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to auto detect version and validation identifier.
 */
public class DetectVersionAndIdentifier {
    public static final String EMPTY = "";
    /**
     * Id to validate XML against.
     */
    public String id;

    /**
     * Version to validate XML against.
     */
    public String version;

    /**
     * Indicates if a schema detection was made.
     */
    public Boolean detected = true;

    /**
     * Tries to detect version and validation identifier from XML.
     *
     * @param xmlDoc   XML as Document
     * @param messages List of messages
     * @throws Exception
     */
    public void setVersionAndIdentifier(Document xmlDoc, Messages messages) throws Exception {
        this.setIdentifier(xmlDoc, messages);
        this.setVersion(messages);
    }

    /**
     * Tries to detect validation identifier from XML. This consists of ProfileID and CustomizationID.
     *
     * @param xmlDoc   XML as Document
     * @param messages List of messages
     * @throws Exception
     */
    private void setIdentifier(Document xmlDoc, Messages messages) throws Exception {
        // Setup
        Utils utils = new Utils();
        String profileId = EMPTY;
        String customizationId = EMPTY;

        // Add XML Namespace
        List<XMLNamespace> namespaces = new ArrayList<>();
        XMLNamespace ns = utils.new XMLNamespace();
        ns.prefix = "cbc";
        ns.url = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
        namespaces.add(ns);

        // Get ProfileID and CustomizationID from xml
        Node pId = utils.xmlDOMXPathQueryWithNS(xmlDoc, "*/cbc:ProfileID", namespaces).item(0);
        Node cId = utils.xmlDOMXPathQueryWithNS(xmlDoc, "*/cbc:CustomizationID", namespaces).item(0);

        if (pId == null) {
            Message message = new Message();
            message.setValidationType(ValidationType.XMLWellFormed);
            message.setMessageType(MessageType.Fatal);
            message.setTitle("ProfileID is missing from XML document. Unable to choose validation profile.");
            message.setDescription(EMPTY);
            messages.addMessage(message);
            this.detected = false;
        } else {
            profileId = pId.getTextContent();
        }

        if (cId == null) {
            Message message = new Message();
            message.setValidationType(ValidationType.XMLWellFormed);
            message.setMessageType(MessageType.Fatal);
            message.setTitle("CustomizationID is missing from XML document. Unable to choose validation profile.");
            message.setDescription(EMPTY);
            messages.addMessage(message);
            this.detected = false;
        } else {
            customizationId = cId.getTextContent();
        }

        // Build schema id
        this.id = profileId + "#" + customizationId;
    }

    /**
     * Tries to detect what version in configuration to validate against. Does this by selecting
     * all available identifiers (schema) and using the one with highest version number.
     *
     * @param messages List of messages
     * @throws Exception
     */
    private void setVersion(Messages messages) throws Exception {
        // Setup
        Configuration configuration = new Configuration();
        Validate validate = new Validate();
        Utils utils = new Utils();
        PropertiesFile propertiesFile = validate.getPropertiesFile();

        // Select all schemas in configuration files
        Document standardXmlDoc = configuration.fileToXMLDOM(propertiesFile.dataDir + "/STANDARD/config.xml", propertiesFile);
        Document customXmlDoc = configuration.fileToXMLDOM(propertiesFile.dataDir + "/CUSTOM/config.xml", propertiesFile);
        NodeList standardValidates = utils.xmlDOMXPathQuery(standardXmlDoc, "/config/validate[@id='" + this.id + "']");
        NodeList customValidates = utils.xmlDOMXPathQuery(customXmlDoc, "/config/validate[@id='" + this.id + "']");

        if (standardValidates.getLength() == 0 && customValidates.getLength() == 0) {
            Message message = new Message();
            message.setValidationType(ValidationType.Configuration);
            message.setMessageType(MessageType.Fatal);
            message.setTitle("No validation definition is found in configuration.");
            message.setDescription("No entry is found in configuration for identificator '" + this.id + "', unable to perform validation!");
            messages.addMessage(message);
            this.detected = false;
        } else {
            // Get highest version number
            ArrayList<String> list = new ArrayList<>();

            for (int i = 0; i < standardValidates.getLength(); i++) {
                Element v = (Element) standardValidates.item(i);
                list.add(v.getAttribute("version"));
            }

            for (int i = 0; i < customValidates.getLength(); i++) {
                Element v = (Element) customValidates.item(i);
                list.add(v.getAttribute("version"));
            }

            Collections.sort(list, Collections.reverseOrder());

            // Set version identifier
            this.version = list.get(0);
        }
    }

}
