package no.difi.vefa.ws.rest.model;

import no.difi.vefa.configuration.Configuration;
import no.difi.vefa.util.PropertiesUtils;
import no.difi.vefa.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to get available version from the configuration files.
 */
public class ListVersions {

    public String baseUri;
    public PropertiesUtils propertiesUtils;

    /**
     * Gets available version from configuration files as string.
     *
     * @throws Exception
     */
    public String getVersions() throws Exception {
        Configuration configuration = new Configuration();
        ArrayList<String> list = new ArrayList<>();
        Document standardConfig = configuration.fileToXMLDOM(propertiesUtils.dataDir + "/STANDARD/config.xml", propertiesUtils);
        this.addVersionToList(standardConfig, list);
        Document customConfig = configuration.fileToXMLDOM(propertiesUtils.dataDir + "/CUSTOM/config.xml", propertiesUtils);
        this.addVersionToList(customConfig, list);
        Set<String> set = new HashSet<>(list);
        List<String> uniqueList = new ArrayList<>(set);
        Collections.sort(uniqueList);
        // Loop and return versions
        String v = "<versions xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
        for (String item : uniqueList) {
            v += "<version xlink:href=\"" + this.baseUri + "/" + item + "\">" + item + "</version>";
        }
        v += "</versions>";

        return v;
    }

    /**
     * Adds version to list.
     *
     * @param document DOM document
     * @param list     List to hold versions as ArrayList
     * @throws Exception
     */
    private void addVersionToList(Document document, ArrayList<String> list) throws Exception {
        // Setup
        XmlUtils xmlUtils = new XmlUtils();

        // Get available versions
        NodeList versions = xmlUtils.xmlDOMXPathQuery(document, "/config/validate[not(@version=preceding-sibling::validate/@version)]/@version");

        // Add versions to list
        for (int i = 0; i < versions.getLength(); i++) {
            list.add(versions.item(i).getNodeValue());
        }
    }
}