package no.difi.vefa.utils.configuration;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;

public class ConfigurationUtilsTest {

    @Test
    public void testFileToXMLDOM() throws Exception {
        ConfigurationUtils configurationUtils = new ConfigurationUtils();
        Document configDoc = configurationUtils.fileToXMLDOM(
                ClassLoader.getSystemResource("config.xml").getPath()
        );

        assertEquals(configDoc.getElementsByTagName("step").item(0).getAttributes().getNamedItem("id").getNodeValue(), "XSL");
    }
}
