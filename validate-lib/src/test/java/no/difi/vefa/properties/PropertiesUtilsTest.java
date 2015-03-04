package no.difi.vefa.properties;

import static org.junit.Assert.*;

import no.difi.vefa.util.PropertiesUtils;
import org.junit.Test;

public class PropertiesUtilsTest {

	@Test
	public void testMain() throws Exception {
		String path = new java.io.File("src/test/resources/validator.properties").getCanonicalPath();	
		PropertiesUtils propFile = new PropertiesUtils();
		propFile.main(path);
		assertEquals(propFile.dataDir, "/etc/opt/VEFAvalidator");
		assertEquals(propFile.suppressWarnings, false);
		assertEquals(propFile.logStatistics, true);
	}
}
