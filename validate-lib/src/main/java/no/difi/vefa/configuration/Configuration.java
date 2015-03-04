package no.difi.vefa.configuration;

import no.difi.vefa.cache.ConfigurationCache;
import no.difi.vefa.util.PropertiesUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;

/**
 * This class can be used to prosess validator configuration XML files.
 */
public class Configuration {

    /**
     * Load configuration file into XML DOM
     *
     * @param xmlFile         Path to XML as string
     * @param propertiesUtils PropertiesFile
     * @return Document XML string as XMLDOM
     * @throws Exception
     */
    public Document fileToXMLDOM(String xmlFile, final PropertiesUtils propertiesUtils) throws Exception {
        ConfigurationCache configurationCache = new ConfigurationCache();
        Document doc = configurationCache.getConfiguration(xmlFile);
        if (doc == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(true);
            factory.setNamespaceAware(true);
            DocumentBuilder parser = factory.newDocumentBuilder();
            parser.setEntityResolver((publicId, systemId) -> {
                String file = propertiesUtils.dataDir + systemId.replace("file://", "");
                return new InputSource(file);
            });
            doc = parser.parse(new FileInputStream(xmlFile));
            configurationCache.addConfiguration(xmlFile, doc);
        }
        return doc;
    }
}
