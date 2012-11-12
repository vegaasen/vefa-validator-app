package no.difi.vefa.configuration;

import static org.junit.Assert.*;

import no.difi.vefa.properties.PropertiesFile;

import org.junit.Test;
import org.w3c.dom.Document;

public class ConfigurationTest {

	@Test
	public void testFileToXMLDOM() throws Exception {
		String path = new java.io.File("src/test/java/no/difi/vefa/configuration/validator.properties").getCanonicalPath();
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(path);
		propFile.dataDir = new java.io.File(".").getCanonicalPath();
		
		Configuration configuration = new Configuration();
		Document configDoc = configuration.fileToXMLDOM(propFile.dataDir + "/src/test/java/no/difi/vefa/configuration/config.xml", propFile);
				
		assertEquals(configDoc.getElementsByTagName("step").item(0).getAttributes().getNamedItem("id").getNodeValue(), "XSL");
	}
}
