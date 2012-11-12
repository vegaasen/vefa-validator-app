package no.difi.vefa.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class contain XML utility routines.
 */
public class Utils {

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
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		
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
	 * @param String XML as string
	 * @return Document XML string as XMLDOM
	 * @throws Exception
	 */	
	public Document stringToXMLDOM(String xml) throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
		
		return doc;		
	}

	/**
	 * Performs XPath Query on XMLDOM
	 * 
	 * @param document XML Document
	 * @param xPath XPath as String
	 * @return NodeLIst Result of XPath Query as NodeList
	 * @throws Exception
	 */	
	public NodeList xmlDOMXPathQuery(Document document, String xPath) throws Exception {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); 
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(xPath);
		Object result = expr.evaluate(document, XPathConstants.NODESET);
		NodeList nodeList = (NodeList) result;
		
		return nodeList;
	}

	/**
	 * Performs XPath Query on XMLDOM with XML namespace
	 * 
	 * @param document XML Document
	 * @param xPath XPath as String
	 * @param namespaces List of XML namespaces
	 * @return NodeLIst Result of XPath Query as NodeList
	 * @throws Exception
	 */	
	public NodeList xmlDOMXPathQueryWithNS(Document document, String xPath, final List<XMLNamespace> namespaces) throws Exception {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); 
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Set namespaces
		xpath.setNamespaceContext(new NamespaceContext() {
			
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
		});
		
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(xPath);
		Object result = expr.evaluate(document, XPathConstants.NODESET);
		NodeList nodeList = (NodeList) result;
		
		return nodeList;
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
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));	    
		return sw.toString(); 
	}	
	
	/**
	 * This class is used to store an XML namespace element.
	 */	
	public class XMLNamespace {
		/**
		 * XML namespace prefix.
		 */		
		public String prefix;
		
		/**
		 * XML namespace URL
		 */		
		public String url;
	}	
}