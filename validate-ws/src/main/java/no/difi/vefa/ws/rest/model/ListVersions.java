package no.difi.vefa.ws.rest.model;

import no.difi.vefa.utils.configuration.ConfigurationUtils;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.utils.xml.XmlUtils;
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

    /**
     * Gets available version from configuration files as string.
     *
     * @throws Exception
     */
    public String getVersions() throws Exception {
        ConfigurationUtils configurationUtils = new ConfigurationUtils();
        ArrayList<String> list = new ArrayList<>();
        Document standardConfig = configurationUtils.fileToXMLDOM(PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
        this.addVersionToList(standardConfig, list);
        Document customConfig = configurationUtils.fileToXMLDOM(PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");
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
