package no.difi.vefa.ws.rest;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.xml.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * This class is used to get available identifiers from the configuration files.
 */
public class ListIdentifier {
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
	public String getIdentifier() throws Exception {
		// Setup
		Configuration configuration = new Configuration();
				
		// Hastable to hold schemas
		LinkedHashSet<String[][]> table = new LinkedHashSet<String[][]>();
			
		// Add schema to table from Standard configuration file
		Document standardConfig = configuration.fileToXMLDOM(propertiesFile.dataDir + "/STANDARD/config.xml", propertiesFile);
		this.addIdentifierToList(standardConfig, table);
		
		// Add schema to table from Custom configuration file
		Document customConfig = configuration.fileToXMLDOM(propertiesFile.dataDir + "/CUSTOM/config.xml", propertiesFile);		
		this.addIdentifierToList(customConfig, table);
			
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
	 * Adds identifier to list.
	 * 
	 * @param document DOM document
	 * @param table HashTable to hold schemas
	 * @throws Exception
	 */	
	private void addIdentifierToList(Document document, LinkedHashSet<String[][]> table) throws Exception{
		// Setup
		Utils utils = new Utils();
		
		// Get available schemas for given version
		NodeList identifiers = utils.xmlDOMXPathQuery(document, "/config/validate[@version='" + version + "']");
		
		for(int i=0; i<identifiers.getLength(); i++){						
			Element identifier = (Element) identifiers.item(i);
			String s = "";
			s += "<schema id=\"" + identifier.getAttribute("id") + "\" xlink:href=\"" + baseUri + identifier.getAttribute("id") + "\"";

			if (identifier.hasAttribute("render"))
				s += " render=\"" + identifier.getAttribute("render") + "\"";

			s += ">";
			
			NodeList names = identifier.getElementsByTagName("name");
	
			for(int x=0; x<names.getLength(); x++){
				Node lang = (Node) names.item(x);				
				s += utils.innerXml(lang);							
			}
			s += "</schema>";
			
			String[][] result = {{identifier.getAttribute("id")},{s}};
			
			table.add(result);
		}
	}
}
