package no.difi.vefa.utils.xml;

import com.google.common.base.Strings;
import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.model.schema.Schema;
import no.difi.vefa.model.xml.namespace.XMLNamespace;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.PropertiesUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
public class SchemaInformation {

    private static final Logger LOG = LogManager.getLogger(SchemaInformation.class.getName());

    private static final String EMPTY = "";
    private static final String CBC_PROFILE_ID = "*/cbc:ProfileID";
    private static final String CBC_CUSTOMIZATION_ID = "*/cbc:CustomizationID";
    private static final String NAMESPACE_URI_OASIS = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
    private static final String PREFIX_CBC = "cbc";

    private SchemaInformation() {
    }

    /**
     * Tries to detect version and validation identifier from XML.
     *
     * @param xmlDoc   XML as Document
     * @param messages List of messages
     */
    public static Schema detectSchemaInformation(Document xmlDoc, Messages messages) {
        final String identifier = getIdentifier(xmlDoc, messages);
        if (Strings.isNullOrEmpty(identifier)) {
            return null;
        }
        final String version = getVersion(identifier, messages);
        return Schema.create(identifier, version, Strings.isNullOrEmpty(version) || !Strings.isNullOrEmpty(identifier));
    }

    /**
     * Tries to detect validation identifier from XML. This consists of ProfileID and CustomizationID.
     *
     * @param xmlDoc   XML as Document
     * @param messages List of messages
     */
    private static String getIdentifier(Document xmlDoc, Messages messages) {
        try {
            // Setup
            String profileId = EMPTY;
            String customizationId = EMPTY;

            // Add XML Namespace
            List<XMLNamespace> namespaces = new ArrayList<>();
            XMLNamespace ns = new XMLNamespace(PREFIX_CBC, NAMESPACE_URI_OASIS);
            namespaces.add(ns);

            // Get ProfileID and CustomizationID from xml
            Node pId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc, CBC_PROFILE_ID, namespaces).item(0);
            Node cId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc, CBC_CUSTOMIZATION_ID, namespaces).item(0);

            if (pId == null) {
                Message message = new Message();
                message.setValidationType(ValidationType.XMLWellFormed);
                message.setMessageType(MessageType.Fatal);
                message.setTitle("ProfileID is missing from XML document. Unable to choose validation profile.");
                message.setDescription(EMPTY);
                messages.addMessage(message);
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
            } else {
                customizationId = cId.getTextContent();
            }
            // Build schema id
            return !Strings.isNullOrEmpty(profileId) || !Strings.isNullOrEmpty(customizationId) ?
                    produceIdentifier(profileId, customizationId) :
                    EMPTY;
        } catch (Exception e) {
            MessageUtils.translate(e, ValidationType.XSD);
            LOG.error(e);
        }
        return EMPTY;
    }

    private static String produceIdentifier(String profileId, String customizationId) {
        return (profileId + "#" + customizationId).replaceAll("\n", "").replaceAll("\\s", "");
    }

    /**
     * Tries to detect what version in configuration to validate against. Does this by selecting
     * all available identifiers (schema) and using the one with highest version number.
     *
     * @param messages List of messages
     */
    private static String getVersion(final String id, Messages messages) {
        try {
            // Select all schemas in configuration files
            Document standardXmlDoc = XmlUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
            Document customXmlDoc = XmlUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");
            NodeList standardValidates = XmlUtils.xmlDOMXPathQuery(standardXmlDoc, "/config/validate[@id='" + id + "']");
            NodeList customValidates = XmlUtils.xmlDOMXPathQuery(customXmlDoc, "/config/validate[@id='" + id + "']");
            if (standardValidates.getLength() == 0 && customValidates.getLength() == 0) {
                Message message = new Message();
                message.setValidationType(ValidationType.Configuration);
                message.setMessageType(MessageType.Fatal);
                message.setTitle("No validation definition is found in configuration.");
                message.setDescription("No entry is found in configuration for identificator '" + id + "', unable to perform validation!");
                messages.addMessage(message);
                return EMPTY;
            } else {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < standardValidates.getLength(); i++) {
                    Element v = (Element) standardValidates.item(i);
                    list.add(v.getAttribute("version"));
                }
                for (int i = 0; i < customValidates.getLength(); i++) {
                    Element v = (Element) customValidates.item(i);
                    list.add(v.getAttribute("version"));
                }
                if (!list.isEmpty()) {
                    Collections.sort(list, Collections.reverseOrder());
                    return list.get(0);
                }
            }
        } catch (Exception e) {
            MessageUtils.translate(e, ValidationType.XSD);
            LOG.error(e);
        }
        return EMPTY;
    }

}
