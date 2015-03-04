package no.difi.vefa.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

/**
 * This class contain XML utility routines.
 */
public class XmlUtils {

    /**
     * Returns XML DOM as string.
     *
     * @param document Document
     * @return String XML DOM as string
     * @throws Exception
     */
    public String xmlDOMToString(Document document) throws Exception {
        // Set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

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
    public Document stringToXMLDOM(String xml) throws Exception {
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
    public NodeList xmlDOMXPathQuery(Document document, String xPath) throws Exception {
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
    public NodeList xmlDOMXPathQueryWithNS(Document document, String xPath, final List<XMLNamespace> namespaces) throws Exception {
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
    public String innerXml(Node node) throws Exception {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "no");
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }

    private static final class SimpleNamespaceContext implements NamespaceContext {

        private final List<XMLNamespace> namespaces;

        public SimpleNamespaceContext(List<XMLNamespace> namespaces) {
            this.namespaces = namespaces;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return null;
        }

        @Override
        public String getNamespaceURI(String prefix) {
            for (XMLNamespace ns : namespaces) {
                if (ns.prefix.equals(prefix)) {
                    return ns.url;
                }
            }
            return null;
        }
    }

    public final class XMLNamespace {
        public String prefix;
        public String url;
    }
}