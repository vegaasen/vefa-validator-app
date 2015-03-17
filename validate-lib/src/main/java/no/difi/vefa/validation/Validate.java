package no.difi.vefa.validation;

import no.difi.vefa.model.message.Hint;
import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.utils.configuration.ConfigurationUtils;
import no.difi.vefa.utils.logging.StatLogger;
import no.difi.vefa.utils.xml.WellFormatted;
import no.difi.vefa.utils.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class is used to validate a xml document according to the
 * given configuration.
 * <p>
 * Properties:
 * - Id: to validate XML against. Corresponds to attribute "id" given in the configuration files. This
 * "id" consists of a combination of "ProfileID" and "CustomizationID", and is used to uniquely identify
 * the correct validation configuration.
 * - Version: to validate XML against. Corresponds to attribute "version" given in the configuration files.
 * - XML: to validate.
 * - suppressWarnings: Should the current validation suppress warnings from output? Default false.
 * - autodetectVersionAndIdentifier: Should we try to autodetect what version to validate against? If set to true,
 * validator will try to autodetect version and identifier based on xml content. Default false;
 */
public class Validate {

    public String id;
    public String version;
    public String xml;
    public boolean suppressWarnings = false;
    public boolean valid;
    public boolean autodetectVersionAndIdentifier = false;
    public String renderResult;

    private Messages messages = new Messages();

    /**
     * Executes a rendering according to the given configuration,
     * adds messages to the message collection if any and
     * sets renderResult as HTML.
     *
     * @throws Exception
     */
    public void render() throws Exception {
        // Setup
        XmlUtils xmlUtils = new XmlUtils();
        ConfigurationUtils configurationUtils = new ConfigurationUtils();

        // Always hide warnings?
        this.hideWarnings();

        // Check if XML string is well formed
        if (!this.isXMLWellFormed()) {
            return;
        }

        // Load XML string as XML DOM		
        Document xmlDoc = xmlUtils.stringToXMLDOM(this.xml);

        // Autodetect version and schema?
        if (!this.tryToAutodetectVersionAndSchema(xmlDoc)) {
            return;
        }

        // Get validations from config files	
        NodeList standardValidates = this.getConfigurationValidation(configurationUtils, xmlUtils, PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
        NodeList customValidates = this.getConfigurationValidation(configurationUtils, xmlUtils, PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");

        // We have not found anything in configuration to validate against
        if (!this.doesConfigurationContainValidationDefinitions(standardValidates, customValidates)) {
            return;
        }

        // Perform validation
        this.renderXML(standardValidates, xmlDoc);
        this.renderXML(customValidates, xmlDoc);

        // Set valid attribute
        this.setIsValid();

        // Log statistics
        //this.logStat();
    }

    /**
     * Perform Difi rendering of XML based on Difi configuration.
     *
     * @param validates Nodelist of validation definitions
     * @param xmlDoc    XML as Document
     * @throws Exception
     */
    private void renderXML(NodeList validates, Document xmlDoc) throws Exception {
        // Loop NodeList for validation steps
        for (int i = 0; i < validates.getLength(); i++) {
            Element validate = (Element) validates.item(i);
            NodeList steps = validate.getElementsByTagName("render");

            for (int x = 0; x < steps.getLength(); x++) {
                Node step = steps.item(x);
                String id = step.getAttributes().getNamedItem("id").getNodeValue();
                String file = step.getAttributes().getNamedItem("file").getNodeValue();

                //System.out.println(id + " " + file);

                if (id.equals("XSL")) {
                    // Perform XSL transformation
                    RenderXsl xmlXslTransformation = new RenderXsl();
                    xmlXslTransformation.main(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, this.messages);

                    // Get XML utils and return DOM as string
                    if (xmlXslTransformation.result != null) {
                        XmlUtils xmlUtils = new XmlUtils();
                        this.renderResult = xmlUtils.xmlDOMToString(xmlXslTransformation.result);
                    }
                }
            }
        }
    }

    /**
     * Executes a validation according to the given configuration
     * and adds messages to the message collection.
     *
     * @throws Exception
     */
    public void validate() throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        ConfigurationUtils configurationUtils = new ConfigurationUtils();
        hideWarnings();
        if (!isXMLWellFormed()) {
            return;
        }
        Document xmlDoc = xmlUtils.stringToXMLDOM(this.xml);
        if (!tryToAutodetectVersionAndSchema(xmlDoc)) {
            return;
        }
        NodeList standardValidates = getConfigurationValidation(configurationUtils, xmlUtils, PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
        NodeList customValidates = getConfigurationValidation(configurationUtils, xmlUtils, PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");

        // We have not found anything in configuration to validate against
        if (!this.doesConfigurationContainValidationDefinitions(standardValidates, customValidates)) {
            return;
        }
        // Perform validation
        validation(standardValidates, xmlDoc);
        validation(customValidates, xmlDoc);

        setIsValid();

        // Log statistics
        this.logStat();
    }

    /**
     * Checks if supperss warnings is set in properties files.
     * If so...set suppresswarning properties.
     */
    private void hideWarnings() {
        if (PropertiesUtils.INSTANCE.isSuppressWarnings()) {
            this.suppressWarnings = true;
        }
    }

    /**
     * Checks if XML is well formed.
     *
     * @return Boolean
     */
    private Boolean isXMLWellFormed() {
        return WellFormatted.isValidXml(xml, messages);
    }

    /**
     * Tries to read the XML file and auto detect version and schema.
     *
     * @return Boolean
     * @throws Exception
     */
    private Boolean tryToAutodetectVersionAndSchema(Document xmlDoc) throws Exception {
        Boolean r = true;
        if (this.autodetectVersionAndIdentifier) {
            DetectVersionAndIdentifier detectVersionAndSchema = new DetectVersionAndIdentifier();
            detectVersionAndSchema.setVersionAndIdentifier(xmlDoc, this.messages);
            this.id = detectVersionAndSchema.id;
            this.version = detectVersionAndSchema.version;

            // No version or schema is detected
            if (!detectVersionAndSchema.detected) {
                r = false;
            }
        }
        return r;
    }

    /**
     * Read a configuration file and extracts validations as NodeList
     *
     * @return NodeList
     * @throws Exception
     */
    private NodeList getConfigurationValidation(ConfigurationUtils configurationUtils, XmlUtils xmlUtils, String config) throws Exception {
        return xmlUtils.xmlDOMXPathQuery(
                configurationUtils.fileToDocument(config),
                "/config/validate[@id='" + this.id + "' and @version='" + this.version + "']");
    }

    /**
     * Checks if standard and custom validation files contain any validation definitions
     * for given version and schema.
     *
     * @return Boolean
     */
    private Boolean doesConfigurationContainValidationDefinitions(NodeList standardValidates, NodeList customValidates) {
        Boolean r = true;

        if (standardValidates.getLength() == 0 && customValidates.getLength() == 0) {
            Message message = new Message();
            message.setValidationType(ValidationType.Configuration);
            message.setMessageType(MessageType.Fatal);
            message.setTitle("No validation definition is found in configuration.");
            message.setDescription("No entry is found in configuration for version '" + this.version + "' and identificator '" + this.id + "', unable to perform validation!");
            this.messages.addMessage(message);
            r = false;
        }

        return r;
    }

    /**
     * Perform Difi validation of XML based on Difi configuration.
     *
     * @param validates Nodelist of validation definitions
     * @param xmlDoc    XML as Document
     * @throws Exception
     */
    private void validation(NodeList validates, Document xmlDoc) throws Exception {
        // Loop NodeList for validation steps
        for (int i = 0; i < validates.getLength(); i++) {
            Element validate = (Element) validates.item(i);
            NodeList steps = validate.getElementsByTagName("step");

            for (int x = 0; x < steps.getLength(); x++) {
                Node step = steps.item(x);
                String id = step.getAttributes().getNamedItem("id").getNodeValue();
                String file = step.getAttributes().getNamedItem("file").getNodeValue();

                System.out.println(id + " " + file);

                switch (id) {
                    case "XSD":
                        // Perform XSD validation
                        XSDValidation xsdValidation = new XSDValidation();
                        xsdValidation.main(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, this.messages);
                        break;
                    case "XSL":
                        // Perform XSL transformation
                        SchematronTransformation xmlXslTransformation = new SchematronTransformation();
                        xmlXslTransformation.main(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, this.messages);
                        break;
                    case "FILTER":
                        String rule = step.getAttributes().getNamedItem("rule").getNodeValue();
                        FilterMessage filterMessage = new FilterMessage();
                        filterMessage.main(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, this.messages, rule);
                        break;
                }
            }
        }
    }

    /**
     * Sets attribute valid. That is if the current XML is valid.
     * Does this by looping the message collection and checking for
     * messages with fatal message type.
     *
     * @throws Exception
     */
    private void setIsValid() throws Exception {
        valid = true;
        for (Message message : this.messages.getMessages()) {
            if (message.getMessageType() == MessageType.Fatal) {
                this.valid = false;
                return;
            }
        }
    }

    /**
     * Performs stat logging to file
     */
    private void logStat() {
        if (PropertiesUtils.INSTANCE.isLogStatistics()) {
            // Set path where to place log files
            System.setProperty("no.difi.vefa.validation.logging.filepath", PropertiesUtils.INSTANCE.getDataDir() + "/LOG");

            // Perform logging
            StatLogger statLogger = new StatLogger();
            statLogger.logStats(this.id, this.version, this.valid, this.messages);
        }
    }

    /**
     * Returns the validation message collection as XML.
     *
     * @return String Messages as XML
     * @throws Exception
     */
    public String messagesAsXML() throws Exception {
        // Build XML document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("messages");
        doc.appendChild(rootElement);

        // Add message attribute id
        rootElement.setAttribute("id", this.id);

        // Add message attribute valid
        rootElement.setAttribute("valid", Boolean.toString(this.valid));

        // Add message attribute version
        rootElement.setAttribute("version", this.version);

        // Iterate messages
        for (Message message : this.messages.getMessages()) {
            if (this.suppressWarnings && message.getMessageType() == MessageType.Warning) {
            } else {
                // Create message
                Element msg = doc.createElement("message");
                rootElement.appendChild(msg);

                // Add message attribute version
                msg.setAttribute("validationType", message.getValidationType().toString());

                // Add messagetype
                Element messageType = doc.createElement("messageType");
                messageType.appendChild(doc.createTextNode(message.getMessageType().toString()));
                msg.appendChild(messageType);

                // Add title
                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(message.getTitle()));
                msg.appendChild(title);

                // Add description
                Element desc = doc.createElement("description");
                desc.appendChild(doc.createTextNode(message.getDescription()));
                msg.appendChild(desc);

                // Add schematron rule id
                Element schematronRuleId = doc.createElement("schematronRuleId");
                schematronRuleId.appendChild(doc.createTextNode(message.getSchematronRuleId()));
                msg.appendChild(schematronRuleId);

                // Add hints to message
                if (message.getHints() != null) {
                    // Create hints
                    Element hints = doc.createElement("hints");

                    for (Hint h : message.getHints()) {
                        // Create hint
                        Element hint = doc.createElement("hint");

                        // Add title
                        Element hintTitle = doc.createElement("title");
                        hintTitle.appendChild(doc.createTextNode(h.getTitle()));
                        hint.appendChild(hintTitle);

                        // Add description
                        Element hintDesc = doc.createElement("description");
                        hintDesc.appendChild(doc.createTextNode(h.getDescription()));
                        hint.appendChild(hintDesc);

                        // Add hint
                        hints.appendChild(hint);
                    }

                    // Add hints
                    msg.appendChild(hints);
                }
            }
        }

        // Get XML utils and return DOM as string
        XmlUtils xmlUtils = new XmlUtils();
        return xmlUtils.xmlDOMToString(doc);
    }

    public Messages getMessages() {
        return messages;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isSuppressWarnings() {
        return suppressWarnings;
    }

    public void setSuppressWarnings(boolean suppressWarnings) {
        this.suppressWarnings = suppressWarnings;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isAutodetectVersionAndIdentifier() {
        return autodetectVersionAndIdentifier;
    }

    public void setAutodetectVersionAndIdentifier(boolean autodetectVersionAndIdentifier) {
        this.autodetectVersionAndIdentifier = autodetectVersionAndIdentifier;
    }

}
