package no.difi.vefa.ws;

import java.util.Enumeration;
import java.util.Hashtable;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.validation.Validate;
import no.difi.vefa.xml.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is used to get available schemas from the configuration files.
 */
public class ListSchemas {
	/**
	 * Base uri to webservice as string.
	 */	
	public String baseUri;

	/**
	 * Version to get schemas for
	 */	
	public String version;
	
	/**
	 * Gets available schema for current version from configuration files as string.
	 * 
	 * @throws Exception
	 */	
	public String getSchemas() throws Exception {
		// Setup
		Validate validate = new Validate();		
		Configuration configuration = new Configuration();
		
		// Load properties file
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(validate.pathToPropertiesFile);
		
		// Hastable to hold schemas
		Hashtable<String, String> table = new Hashtable<String, String>();
			
		// Add schema to table from Standard configuration file
		Document standardConfig = configuration.fileToXMLDOM(propFile.dataDir + "/STANDARD/config.xml", propFile);
		this.addSchemaToList(standardConfig, table);
		
		// Add schema to table from Custom configuration file
		Document customConfig = configuration.fileToXMLDOM(propFile.dataDir + "/CUSTOM/config.xml", propFile);		
		this.addSchemaToList(customConfig, table);
			
		String v = "<schemas version=\"" + this.version + "\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
		
		Enumeration<String> e = table.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			v += table.get(key);	
		}
		
		v += "</schemas>";
		
		return v;	
	}
	
	/**
	 * Adds schema to list.
	 * 
	 * @param document DOM document
	 * @param table HashTable to hold schemas
	 * @throws Exception
	 */	
	private void addSchemaToList(Document document, Hashtable<String, String> table) throws Exception{
		// Setup
		Utils utils = new Utils();
		
		// Get available schemas for given version
		NodeList schemas = utils.xmlDOMXPathQuery(document, "/config/validate[@version='" + version + "']");
		
		for(int i=0; i<schemas.getLength(); i++){						
			Element schema = (Element) schemas.item(i);
			String s = "";
			s += "<schema id=\"" + schema.getAttribute("id") + "\" xlink:href=\"" + baseUri + schema.getAttribute("id") + "\">";
			
			NodeList names = schema.getElementsByTagName("name");
	
			s += "<name>";
			for(int x=0; x<names.getLength(); x++){
				Node lang = (Node) names.item(x);				
				s += utils.innerXml(lang);							
			}
			s += "</name>";
			s += "</schema>";
			
			table.put(schema.getAttribute("id"), s);
		}
	}
}
