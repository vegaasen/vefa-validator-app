package no.difi.vefa.xml;

import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import no.difi.vefa.cache.XSLTransformerCache;

import org.w3c.dom.Document;


/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 */
public class XmlXslTransformation {
    /**
     * XSL Transformer cache
     */	
	private XSLTransformerCache xslTransformerCache;
	
	/**
	 * Perform transformation of XML with XML.
	 * 
	 * @param xmlDoc XML as Document
	 * @param xslFile Path to XSL file as String
	 * @return Document Result of transformation as Document
	 * @throws Exception
	 */	
	public Document main(Document xmlDoc, String xslFile) throws Exception {
		Utils utils = new Utils();
		StringWriter writer = new StringWriter();
		FileReader readerXsl = new FileReader(xslFile);
        StringReader readerXml = new StringReader(utils.xmlDOMToString(xmlDoc));
        
		// Transform XML
		TransformerFactory tFactory = TransformerFactory.newInstance();	

		// Check if XML transformer is cached		
		xslTransformerCache = new XSLTransformerCache();
        Transformer transformer = xslTransformerCache.getTransformer(xslFile);
        
        if (transformer == null) {
        	transformer = tFactory.newTransformer(new StreamSource(readerXsl));
        	xslTransformerCache.addTransformer( xslFile, transformer );
        }
					
        //Transformer transformer = tFactory.newTransformer(new StreamSource(readerXsl));
		transformer.transform(new StreamSource(readerXml), new StreamResult(writer));

		// Load transformed XML string as XML DOM
		Document doc = utils.stringToXMLDOM(writer.toString());
		
		return doc;
	}
}
