package no.difi.vefa.util.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * This class can be used to check if an XML string contains
 * valid XML.
 */
public class WellFormed {

	/**
	 * Checks if xml string is valid XML.  
	 * Throws an exception if this is not the case.
	 *
	 * @param  xml  String containing XML
	 * @throws Exception
	 */
	public void main(String xml) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();			

		reader.parse(new InputSource(new StringReader(xml)));
	}
}
