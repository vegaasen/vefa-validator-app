package no.difi.vefa.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.validation.Validate;
import no.difi.vefa.xml.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * This class is used to get available version from the configuration files.
 */
public class ListVersions {
	/**
	 * Base uri to webservice as string.
	 */	
	public String baseUri;

	/**
	 * Gets available version from configuration files as string.
	 * 
	 * @throws Exception
	 */
	public String getVersions() throws Exception  {
		// Setup
		Validate validate = new Validate();
		PropertiesFile propFile = new PropertiesFile();
		Configuration configuration = new Configuration();
		
		// Load properties file		
		propFile.main(validate.pathToPropertiesFile);
		
		// List to hold versions
		ArrayList<String> list = new ArrayList<String>();
		
		// Add versions to list from Standard configuration file
		Document standardConfig = configuration.fileToXMLDOM(propFile.dataDir + "/STANDARD/config.xml", propFile);
		this.addVersionToList(standardConfig, list);
		
		// Add versions to list from Custom configuration file
		Document customConfig = configuration.fileToXMLDOM(propFile.dataDir + "/CUSTOM/config.xml", propFile);
		this.addVersionToList(customConfig, list);

		// Convert list to set (to get unique values)
		Set<String> set = new HashSet<String>(list);
		
		// Convert set to list
		ArrayList<String> uniqueList = new ArrayList<String>(set);

		// Sort the list
		Collections.sort(uniqueList);
		
		// Loop and return versions
		String v = "<versions xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
		for (Object version : uniqueList) {
			String item = (String) version;
			v += "<version xlink:href=\"" + this.baseUri + item + "\">" + item + "</version>";
		}
		v += "</versions>";
				
		return v;
	}	

	/**
	 * Adds version to list.
	 * 
	 * @param document DOM document
	 * @param list List to hold versions as ArrayList
	 * @throws Exception
	 */
	private void addVersionToList(Document document, ArrayList<String> list) throws Exception {
		// Setup
		Utils utils = new Utils();		
		
		// Get available versions
		NodeList versions = utils.xmlDOMXPathQuery(document, "/config/validate[not(@version=preceding-sibling::validate/@version)]/@version");
		
		// Add versions to list
		for(int i=0; i<versions.getLength(); i++){
			list.add(versions.item(i).getNodeValue());
		}		
	}
}
