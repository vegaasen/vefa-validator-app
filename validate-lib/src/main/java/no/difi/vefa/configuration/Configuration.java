package no.difi.vefa.configuration;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import no.difi.vefa.properties.PropertiesFile;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class can be used to prosess validator configuration XML files.
 */
public class Configuration {
	/**
	 * Load configuration file into XML DOM
	 * 
	 * @param xmlFile Path to XML as string
	 * @param propertiesFile PropertiesFile
	 * @return Document XML string as XMLDOM
	 * @throws Exception
	 */	
	public Document fileToXMLDOM(String xmlFile, final PropertiesFile propertiesFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setXIncludeAware(true);
		factory.setNamespaceAware(true);
		DocumentBuilder parser = factory.newDocumentBuilder();
		
        parser.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            	String file = propertiesFile.dataDir + systemId.replace("file://", "");
            	return new InputSource(file);
            }
        });
		
		Document doc= parser.parse(new FileInputStream(xmlFile));
		
		return doc;
	}
}
