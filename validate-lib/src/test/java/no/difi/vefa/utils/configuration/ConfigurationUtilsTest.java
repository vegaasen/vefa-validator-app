package no.difi.vefa.utils.configuration;

import no.difi.vefa.utils.PropertiesUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ConfigurationUtilsTest {

    @Test
    public void testFileToXMLDOM() throws Exception {
        String path = ClassLoader.getSystemResource("validator.properties").getPath();
        PropertiesUtils propFile = new PropertiesUtils();
        propFile.main(path);
        propFile.dataDir = new File(".").getCanonicalPath();

        ConfigurationUtils configurationUtils = new ConfigurationUtils();
        Document configDoc = configurationUtils.fileToXMLDOM(ClassLoader.getSystemResource("config.xml").getPath(), propFile);

        assertEquals(configDoc.getElementsByTagName("step").item(0).getAttributes().getNamedItem("id").getNodeValue(), "XSL");
    }
}
