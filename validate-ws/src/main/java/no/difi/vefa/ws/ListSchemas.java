package no.difi.vefa.ws;

import java.util.Iterator;
import java.util.LinkedHashSet;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.properties.PropertiesFile;
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
	 * Properties file.
	 */	
	public PropertiesFile propertiesFile;

	/**
	 * Gets available schema for current version from configuration files as string.
	 * 
	 * @throws Exception
	 */	
	public String getSchemas() throws Exception {
		// Setup
		Configuration configuration = new Configuration();
				
		// Hastable to hold schemas
		LinkedHashSet<String[][]> table = new LinkedHashSet<String[][]>();
			
		// Add schema to table from Standard configuration file
		Document standardConfig = configuration.fileToXMLDOM(propertiesFile.dataDir + "/STANDARD/config.xml", propertiesFile);
		this.addSchemaToList(standardConfig, table);
		
		// Add schema to table from Custom configuration file
		Document customConfig = configuration.fileToXMLDOM(propertiesFile.dataDir + "/CUSTOM/config.xml", propertiesFile);		
		this.addSchemaToList(customConfig, table);
			
		String v = "<schemas version=\"" + this.version + "\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
		
		Iterator<String[][]> itr = table.iterator();
		while(itr.hasNext()) {
			String[][] result = (String[][]) itr.next(); 
			v += result[1][0];
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
	private void addSchemaToList(Document document, LinkedHashSet<String[][]> table) throws Exception{
		// Setup
		Utils utils = new Utils();
		
		// Get available schemas for given version
		NodeList schemas = utils.xmlDOMXPathQuery(document, "/config/validate[@version='" + version + "']");
		
		for(int i=0; i<schemas.getLength(); i++){						
			Element schema = (Element) schemas.item(i);
			String s = "";
			s += "<schema id=\"" + schema.getAttribute("id") + "\" xlink:href=\"" + baseUri + schema.getAttribute("id") + "\">";
			
			NodeList names = schema.getElementsByTagName("name");
	
			for(int x=0; x<names.getLength(); x++){
				Node lang = (Node) names.item(x);				
				s += utils.innerXml(lang);							
			}
			s += "</schema>";
			
			String[][] result = {{schema.getAttribute("id")},{s}};
			
			table.add(result);
		}
	}
}
