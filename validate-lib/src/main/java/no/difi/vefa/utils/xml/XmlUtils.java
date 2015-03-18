package no.difi.vefa.utils.xml;

import com.google.common.base.Strings;
import no.difi.vefa.cache.ConfigurationCache;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.model.xml.ClassPathResourceResolver;
import no.difi.vefa.model.xml.namespace.SimpleNamespaceContext;
import no.difi.vefa.model.xml.namespace.XMLNamespace;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.PropertiesUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class XmlUtils {

    private static final String REPLACEABLE = "file://";
    private static final String EMPTY = "";
    private static final String INDENT = "no", OMIT_XML = "yes";
    private static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    private XmlUtils() {
    }

    /**
     * Load configuration file into XML DOM
     *
     * @param xmlFile Path to XML as string
     * @return Document XML string as XMLDOM
     * @throws Exception
     */
    public static Document fileToDocument(String xmlFile) throws Exception {
        Document doc = ConfigurationCache.getConfiguration(xmlFile);
        if (doc == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(true);
            factory.setNamespaceAware(true);
            DocumentBuilder parser = factory.newDocumentBuilder();
            parser.setEntityResolver((publicId, systemId) -> {
                String file = PropertiesUtils.INSTANCE.getDataDir() + systemId.replace(REPLACEABLE, EMPTY);
                return new InputSource(file);
            });
            doc = parser.parse(new FileInputStream(xmlFile));
            ConfigurationCache.addConfiguration(xmlFile, doc);
        }
        return doc;
    }

    /**
     * Perform validation of XML Document with a XSD Document.
     *
     * @param xmlDoc  XML as Document
     * @param xsdFile Path to XSD file as String
     */
    public static void validateXmlByXsd(Document xmlDoc, String xsdFile, Messages messages) {
        try (
                FileReader readerXsd = new FileReader(xsdFile);
                StringReader readerXml = new StringReader(XmlUtils.documentToString(xmlDoc))
        ) {
            SchemaFactory factory = SchemaFactory.newInstance(SCHEMA_LANGUAGE);
            factory.setResourceResolver(new ClassPathResourceResolver());
            Schema schema = factory.newSchema(new StreamSource(readerXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(readerXml));
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSD));
        }
    }

    /**
     * Checks if xml string is valid XML.
     * Add a message to message collection if this is not the case.
     *
     * @param xml      String containing XML
     * @param messages List of messages
     * @return boolean Returns true or false
     */
    public static boolean isWellFormatted(String xml, Messages messages) {
        if (Strings.isNullOrEmpty(xml)) {
            messages.addMessage(MessageUtils.translate(new IllegalArgumentException("Required parameter XML missing. Unable to verify if the message is well formatted."), ValidationType.XMLWellFormed));
            return false;
        }
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.parse(new InputSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XMLWellFormed));
        }
        return false;
    }

    /**
     * Returns XML DOM as string.
     *
     * @param document Document
     * @return String XML DOM as string
     * @throws Exception
     */
    public static String documentToString(Document document) throws Exception {
        // Set up a transformer
        Transformer trans = getXmlTransformer();

        // Create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);
        trans.transform(source, result);

        return sw.toString();
    }

    /**
     * Load string into XML DOM
     *
     * @param xml XML as string
     * @return Document XML string as XMLDOM
     * @throws Exception
     */
    public static Document stringToDocument(String xml) throws Exception {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        return docFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Performs XPath Query on XMLDOM
     *
     * @param document XML Document
     * @param xPath    XPath as String
     * @return NodeLIst Result of XPath Query as NodeList
     * @throws Exception
     */
    public static NodeList xmlDOMXPathQuery(Document document, String xPath) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        return (NodeList) XPathFactory.newInstance().newXPath().compile(xPath).evaluate(document, XPathConstants.NODESET);
    }

    /**
     * Performs XPath Query on XMLDOM with XML namespace
     *
     * @param document   XML Document
     * @param xPath      XPath as String
     * @param namespaces List of XML namespaces
     * @return NodeLIst Result of XPath Query as NodeList
     * @throws Exception
     */
    public static NodeList xmlDOMXPathQueryWithNS(Document document, String xPath, final List<XMLNamespace> namespaces) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new SimpleNamespaceContext(namespaces));
        return (NodeList) xpath.compile(xPath).evaluate(document, XPathConstants.NODESET);
    }

    /**
     * Returns innerXML of Node
     *
     * @param node XML Node
     * @return innerXML of Node as String
     * @throws Exception
     */
    public static String innerXml(Node node) throws Exception {
        StringWriter sw = new StringWriter();
        Transformer t = getXmlTransformer();
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }

    private static Transformer getXmlTransformer() throws TransformerConfigurationException {
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, OMIT_XML);
        trans.setOutputProperty(OutputKeys.INDENT, INDENT);
        return trans;
    }
}