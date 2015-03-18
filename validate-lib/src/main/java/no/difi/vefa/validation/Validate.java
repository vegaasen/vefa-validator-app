package no.difi.vefa.validation;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.model.schema.Schema;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.utils.logging.LogStatistics;
import no.difi.vefa.utils.xml.SchemaInformation;
import no.difi.vefa.utils.xml.XmlUtils;
import no.difi.vefa.utils.xml.XslUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
 * <p>
 */
public class Validate {

    private String id;
    private String version;
    private String source;
    private boolean suppressWarnings = false;
    private boolean valid;
    private boolean autodetectVersionAndIdentifier = false;
    private String renderResult;
    private Messages messages = new Messages();

    /**
     * Executes a rendering according to the given configuration,
     * adds messages to the message collection if any and
     * sets renderResult as HTML.
     *
     * @throws Exception
     */
    public void render() throws Exception {
        hideWarnings();
        if (!XmlUtils.isWellFormatted(getSource(), getMessages())) {
            logErrorMessage();
            return;
        }
        Document xmlDoc = XmlUtils.stringToDocument(getSource());
        if (!autodetectVersionAndSchema(xmlDoc)) {
            logErrorMessage();
            return;
        }
        NodeList standardValidates = getStandardValidates();
        NodeList customValidates = getCustomValidates();
        if (!doesConfigurationContainValidationDefinitions(standardValidates, customValidates)) {
            logErrorMessage();
            return;
        }
        renderXML(standardValidates, xmlDoc);
        renderXML(customValidates, xmlDoc);
        setIsValid();
    }

    /**
     * Executes a validation according to the given configuration
     * and adds messages to the message collection.
     *
     * @throws Exception
     */
    public void validate() throws Exception {
        hideWarnings();
        if (!XmlUtils.isWellFormatted(getSource(), getMessages())) {
            logErrorMessage();
            return;
        }
        Document xmlDoc = XmlUtils.stringToDocument(getSource());
        if (!autodetectVersionAndSchema(xmlDoc)) {
            logErrorMessage();
            return;
        }
        NodeList standardValidates = getStandardValidates();
        NodeList customValidates = getCustomValidates();
        if (!doesConfigurationContainValidationDefinitions(standardValidates, customValidates)) {
            logErrorMessage();
            return;
        }
        validation(xmlDoc, standardValidates, customValidates);
        setIsValid();
        logStat();
    }

    public String getRenderResult() {
        return renderResult;
    }

    public void setRenderResult(String renderResult) {
        this.renderResult = renderResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    private void logErrorMessage() {
        Message message = new Message();
        message.setTitle("Missing required parameters");
        message.setDescription("Either id, source or version was nilled. Unable to validate.");
        message.setValidationType(ValidationType.UNKNOWN);
        message.setMessageType(MessageType.Fatal);
        message.setSchematronRuleId("-1");
        getMessages().addMessage(message);
    }

    private NodeList getStandardValidates() throws Exception {
        return getConfigurationValidation(PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
    }

    private NodeList getCustomValidates() throws Exception {
        return getConfigurationValidation(PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");
    }

    /**
     * Checks if supperss warnings is set in properties files.
     * If so...set suppresswarning properties.
     */
    private void hideWarnings() {
        if (PropertiesUtils.INSTANCE.isSuppressWarnings() && !isSuppressWarnings()) {
            setSuppressWarnings(true);
        }
    }

    /**
     * Tries to read the XML file and auto detect version and schema.
     *
     * @return Boolean
     * @throws Exception
     */
    private boolean autodetectVersionAndSchema(Document xmlDoc) throws Exception {
        boolean detected = true;
        if (isAutodetectVersionAndIdentifier()) {
            Schema version = SchemaInformation.detectSchemaInformation(xmlDoc, getMessages());
            setId(version.getId());
            setVersion(version.getVersion());
            if (!version.isDetected()) {
                detected = false;
            }
        }
        return detected;
    }

    /**
     * Read a configuration file and extracts validations as NodeList
     *
     * @return NodeList
     * @throws Exception
     */
    private NodeList getConfigurationValidation(String config) throws Exception {
        return XmlUtils.xmlDOMXPathQuery(
                XmlUtils.fileToDocument(config),
                "/config/validate[@id='" + getId() + "' and @version='" + getVersion() + "']");
    }

    /**
     * Checks if standard and custom validation files contain any validation definitions
     * for given version and schema.
     *
     * @return valid/not valid
     */
    private boolean doesConfigurationContainValidationDefinitions(NodeList standardValidates, NodeList customValidates) {
        if (standardValidates.getLength() == 0 && customValidates.getLength() == 0) {
            Message message = new Message();
            message.setValidationType(ValidationType.Configuration);
            message.setMessageType(MessageType.Fatal);
            message.setTitle("No validation definition is found in configuration.");
            message.setDescription("No entry is found in configuration for version '" + getVersion() + "' and identificator '" + getId() + "', unable to perform validation!");
            getMessages().addMessage(message);
            return false;
        }
        return true;
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
                if (id.equals("XSL")) {
                    final Document transformationResult = XslUtils.transform(
                            xmlDoc,
                            PropertiesUtils.INSTANCE.getDataDir() + file,
                            getMessages());
                    // Get XML utils and return DOM as string
                    if (transformationResult != null) {
                        setRenderResult(XmlUtils.documentToString(transformationResult));
                    }
                }
            }
        }
    }

    /**
     * Perform Difi validation of XML based on Difi configuration.
     *
     * @param nodeLists Nodelist of validation definitions
     * @param xmlDoc    XML as Document
     * @throws Exception
     */
    private void validation(Document xmlDoc, NodeList... nodeLists) throws Exception {
        // Loop NodeList for validation steps
        for (NodeList node : nodeLists) {
            for (int i = 0; i < node.getLength(); i++) {
                Element validate = (Element) node.item(i);
                NodeList steps = validate.getElementsByTagName("step");
                for (int x = 0; x < steps.getLength(); x++) {
                    Node step = steps.item(x);
                    validateNode(xmlDoc, step);
                }
            }
        }
    }

    private void validateNode(Document xmlDoc, Node step) {
        String id = step.getAttributes().getNamedItem("id").getNodeValue();
        String file = step.getAttributes().getNamedItem("file").getNodeValue();
        switch (id) {
            case "XSD":
                // Perform XSD validation
                XmlUtils.validateXmlByXsd(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, getMessages());
                break;
            case "XSL":
                // Perform XSL transformation
                XslUtils.validate(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, getMessages());
                break;
            case "FILTER":
                XslUtils.filterMessages(xmlDoc, PropertiesUtils.INSTANCE.getDataDir() + file, getMessages(), step.getAttributes().getNamedItem("rule").getNodeValue());
                break;
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
        setValid(getMessages().getMessages().isEmpty() || !getMessages().getMessages().parallelStream().findFirst().filter(m -> m.getMessageType() == MessageType.Fatal).isPresent());
    }

    /**
     * Performs stat logging to file
     */
    private void logStat() {
        if (PropertiesUtils.INSTANCE.isLogStatistics()) {
            LogStatistics.log(getId(), getVersion(), isValid(), getMessages());
        }
    }
}
