package no.difi.vefa.ws;

import static org.junit.Assert.*;

import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.ws.rest.ListVersions;
import org.junit.Before;
import org.junit.Test;

public class ListVersionsTest {

	private ListVersions listVersions;
	
	@Before
	public void setUp() throws Exception {
		String path = new java.io.File("src/test/resources/validator.properties").getCanonicalPath();
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(path);
		propFile.dataDir = new java.io.File("src/test/resources/").getCanonicalPath();
		
		listVersions = new ListVersions();
		listVersions.baseUri = "http://www.test.com/validate-ws/";
		listVersions.propertiesFile = propFile;
	}

	@Test
	public void testGetVersions() throws Exception {
		assertEquals("<versions xmlns:xlink=\"http://www.w3.org/1999/xlink\"><version xlink:href=\"http://www.test.com/validate-ws//1.4\">1.4</version></versions>", listVersions.getVersions());		
	}

}
