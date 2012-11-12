package no.difi.vefa.properties;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertiesFileTest {

	@Test
	public void testMain() throws Exception {
		String path = new java.io.File("src/test/java/no/difi/vefa/properties/test.properties").getCanonicalPath();	
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(path);
		assertEquals(propFile.dataDir, "/etc/opt/VEFAvalidator");
		assertEquals(propFile.suppressWarnings, false);
		assertEquals(propFile.logStatistics, true);
	}
}
