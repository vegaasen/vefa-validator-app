package no.difi.vefa.utils.xml;

import net.sf.saxon.TransformerFactoryImpl;
import no.difi.vefa.cache.TransformerCache;
import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.xml.resolver.XsltURIResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class XslUtils {

    private static final Pattern PATTERN = Pattern.compile("\\[(.*?)\\]");
    private static final String SVRL_FAILED_ASSERT = "svrl:failed-assert";
    private static final String NODE_TEST = "test";
    private static final String NODE_LOCATION = "location";
    private static final String NODE_FLAG = "flag";
    private static final String EMPTY = "";
    private static final String R_BRACKET = "[";
    private static final String L_BRACKET = "]";

    private XslUtils() {
    }

    /**
     * Perform transformation of XML with XML.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     */
    public static void validate(Document xmlDoc, String xslFile, Messages messages) {
        try {
            // Transform XML with XSL
            Document result = transform(xmlDoc, xslFile, messages);
            // Loop result of transformation and add to Message collection
            NodeList failedAsserts = result.getElementsByTagName(SVRL_FAILED_ASSERT);
            for (int i = 0; i < failedAsserts.getLength(); i++) {
                messages.addMessage(assembleMessage(failedAsserts.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSL));
        }
    }

    /**
     * Filter/Validate messages from message collection.
     *
     * @param xmlDoc   XML as Document
     * @param xslFile  Path to XSL file as String
     * @param messages List of messages
     * @param rule     What SCHEMATRON rule to filter as String
     */
    public static void filterMessages(Document xmlDoc, String xslFile, Messages messages, String rule) {
        try {
            // Status
            boolean status = false;
            // Transform XML with XSL and return status of XSL check
            Document result = transform(xmlDoc, xslFile, messages);
            // Get status from XML/XSL transformation
            NodeList statusNodeList = result.getElementsByTagName("status");
            for (int i = 0; i < statusNodeList.getLength(); i++) {
                Node statusNode = statusNodeList.item(i);
                status = Boolean.parseBoolean(statusNode.getTextContent());
            }
            // If result from XSL transformation is true then remove message from message collection where title = rule
            if (status) {
                for (Iterator<Message> iterator = messages.getMessages().iterator(); iterator.hasNext(); ) {
                    Message message = iterator.next();
                    if (message.getSchematronRuleId().equals(rule)) {
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.Filter));
        }
    }

    /**
     * Perform transformation of XML with XSL.
     *
     * @param xmlDoc  XML as Document
     * @param xslFile Path to XSL file as String
     * @return Document Result of transformation as Document
     * @throws Exception
     */
    public static Document transform(Document xmlDoc, String xslFile, Messages messages) throws Exception {
        try {
            return transform(xmlDoc, notCached(xslFile) ? parseAndCache(xslFile) : fetchFromCache(xslFile));
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSL));
        }
        return null;
    }

    /**
     * Tries to extract SCHEMATRON rule id from text
     *
     * @param text Text to search for SCHEMATRON rule
     * @return SCHEMATRON rule id as String
     */
    private static String getSchematronRule(String text) {
        Matcher m = PATTERN.matcher(text);
        while (m.find()) {
            text = m.group().replace(R_BRACKET, EMPTY).replace(L_BRACKET, EMPTY);
        }
        return text;
    }

    private static Message assembleMessage(Node failedAssert) {
        String test = failedAssert.getAttributes().getNamedItem(NODE_TEST).getNodeValue();
        String location = failedAssert.getAttributes().getNamedItem(NODE_LOCATION).getNodeValue();
        String flag = failedAssert.getAttributes().getNamedItem(NODE_FLAG).getNodeValue();
        String text = failedAssert.getTextContent();
        // Add failed-assert to message collection
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        if (flag.equals(MessageType.Fatal.toString().toLowerCase())) {
            message.setMessageType(MessageType.Fatal);
        } else if (flag.equals(MessageType.Warning.toString().toLowerCase())) {
            message.setMessageType(MessageType.Warning);
        }
        message.setTitle(text);
        message.setDescription("Test: " + test + ", Location: " + location);
        message.setSchematronRuleId(getSchematronRule(text));
        return message;
    }

    private static boolean notCached(String xslFile) {
        return !TransformerCache.exists(xslFile);
    }

    private static Templates fetchFromCache(String xslFile) {
        return TransformerCache.getTemplate(xslFile);
    }

    private static Templates parse(String xslFile) throws FileNotFoundException, TransformerConfigurationException {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance(TransformerFactoryImpl.class.getName(), null);
        transformerFactory.setURIResolver(new XsltURIResolver());
        final File file = new File(xslFile);
        if (file.exists()) {
            return transformerFactory.newTemplates(new StreamSource(file));
        }
        throw new FileNotFoundException("File not found");
    }

    private static Document transform(Document xmlDoc, Templates templates) throws Exception {
        try (StringWriter writer = new StringWriter()) {
            templates.newTransformer().transform(new DOMSource(xmlDoc), new StreamResult(writer));
            return XmlUtils.stringToDocument(writer.toString());
        }
    }

    private static Templates parseAndCache(String xslFile) throws TransformerConfigurationException, FileNotFoundException {
        Templates templates = parse(xslFile);
        cache(xslFile, templates);
        return templates;
    }

    private static void cache(String xslFile, Templates templates) {
        TransformerCache.addTemplate(xslFile, templates);
    }

}
