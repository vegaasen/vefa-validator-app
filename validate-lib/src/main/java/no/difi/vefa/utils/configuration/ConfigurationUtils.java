package no.difi.vefa.utils.configuration;

import no.difi.vefa.cache.ConfigurationCache;
import no.difi.vefa.utils.PropertiesUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;

public class ConfigurationUtils {

    private static final String REPLACEABLE = "file://";
    private static final String EMPTY = "";

    /**
     * Load configuration file into XML DOM
     *
     * @param xmlFile Path to XML as string
     * @return Document XML string as XMLDOM
     * @throws Exception
     */
    public static Document fileToDocument(String xmlFile) throws Exception {
        Document doc = ConfigurationCache.getConfiguration(xmlFile);
        if (doc == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(true);
            factory.setNamespaceAware(true);
            DocumentBuilder parser = factory.newDocumentBuilder();
            parser.setEntityResolver((publicId, systemId) -> {
                String file = PropertiesUtils.INSTANCE.getDataDir() + systemId.replace(REPLACEABLE, EMPTY);
                return new InputSource(file);
            });
            doc = parser.parse(new FileInputStream(xmlFile));
            ConfigurationCache.addConfiguration(xmlFile, doc);
        }
        return doc;
    }
}
