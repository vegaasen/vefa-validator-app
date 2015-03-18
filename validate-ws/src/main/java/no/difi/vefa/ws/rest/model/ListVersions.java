package no.difi.vefa.ws.rest.model;

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
        Set<String> set = new HashSet<>();
        Document standardConfig = XmlUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
        addVersionToList(standardConfig, set);
        Document customConfig = XmlUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");
        addVersionToList(customConfig, set);
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
    private void addVersionToList(Document document, Set<String> list) throws Exception {
        // Get available versions
        NodeList versions = XmlUtils.xmlDOMXPathQuery(document, "/config/validate[not(@version=preceding-sibling::validate/@version)]/@version");
        // Add versions to list
        for (int i = 0; i < versions.getLength(); i++) {
            list.add(versions.item(i).getNodeValue());
        }
    }
}
