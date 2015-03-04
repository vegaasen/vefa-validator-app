package no.difi.vefa.configuration;

import static org.junit.Assert.*;

import no.difi.vefa.util.PropertiesUtils;

import org.junit.Test;
import org.w3c.dom.Document;

public class ConfigurationTest {

	@Test
	public void testFileToXMLDOM() throws Exception {
		String path = new java.io.File("src/test/resources/validator.properties").getCanonicalPath();
		PropertiesUtils propFile = new PropertiesUtils();
		propFile.main(path);
		propFile.dataDir = new java.io.File(".").getCanonicalPath();
		
		Configuration configuration = new Configuration();
		Document configDoc = configuration.fileToXMLDOM(propFile.dataDir + "/src/test/resources/config.xml", propFile);
				
		assertEquals(configDoc.getElementsByTagName("step").item(0).getAttributes().getNamedItem("id").getNodeValue(), "XSL");
	}
}
