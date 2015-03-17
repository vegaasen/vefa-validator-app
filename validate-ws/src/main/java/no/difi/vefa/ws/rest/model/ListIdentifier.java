package no.difi.vefa.ws.rest.model;

import no.difi.vefa.utils.configuration.ConfigurationUtils;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.utils.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedHashSet;

/**
 * This class is used to get available identifiers from the configuration files.
 */
public class ListIdentifier {

    public String baseUri;
    public String version;
    public PropertiesUtils propertiesUtils;

    /**
     * Gets available schema for current version from configuration files as string.
     *
     * @throws Exception
     */
    public String getIdentifier() throws Exception {
        // Setup
        ConfigurationUtils configurationUtils = new ConfigurationUtils();

        // Hastable to hold schemas
        LinkedHashSet<String[][]> table = new LinkedHashSet<>();

        // Add schema to table from Standard configuration file
        Document standardConfig = configurationUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/STANDARD/config.xml");
        this.addIdentifierToList(standardConfig, table);

        // Add schema to table from Custom configuration file
        Document customConfig = configurationUtils.fileToDocument(PropertiesUtils.INSTANCE.getDataDir() + "/CUSTOM/config.xml");
        this.addIdentifierToList(customConfig, table);

        String v = "<schemas version=\"" + this.version + "\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">";

        for (String[][] result : table) {
            v += result[1][0];
        }

        v += "</schemas>";

        return v;
    }

    /**
     * Adds identifier to list.
     *
     * @param document DOM document
     * @param table    HashTable to hold schemas
     * @throws Exception
     */
    private void addIdentifierToList(Document document, LinkedHashSet<String[][]> table) throws Exception {
        // Setup
        XmlUtils xmlUtils = new XmlUtils();

        // Get available schemas for given version
        NodeList identifiers = xmlUtils.xmlDOMXPathQuery(document, "/config/validate[@version='" + version + "']");

        for (int i = 0; i < identifiers.getLength(); i++) {
            Element identifier = (Element) identifiers.item(i);
            String s = "";
            s += "<schema id=\"" + identifier.getAttribute("id") + "\" xlink:href=\"" + baseUri + identifier.getAttribute("id") + "\"";

            if (identifier.hasAttribute("render"))
                s += " render=\"" + identifier.getAttribute("render") + "\"";

            s += ">";

            NodeList names = identifier.getElementsByTagName("name");

            for (int x = 0; x < names.getLength(); x++) {
                Node lang = names.item(x);
                s += xmlUtils.innerXml(lang);
            }
            s += "</schema>";

            String[][] result = {{identifier.getAttribute("id")}, {s}};

            table.add(result);
        }
    }
}
