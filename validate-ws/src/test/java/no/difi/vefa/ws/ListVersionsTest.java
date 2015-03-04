package no.difi.vefa.ws;

import static org.junit.Assert.*;

import no.difi.vefa.util.PropertiesUtils;
import no.difi.vefa.ws.rest.model.ListVersions;
import org.junit.Before;
import org.junit.Test;

public class ListVersionsTest {

	private ListVersions listVersions;
	
	@Before
	public void setUp() throws Exception {
		String path = new java.io.File("src/test/resources/validator.properties").getCanonicalPath();
		PropertiesUtils propFile = new PropertiesUtils();
		propFile.main(path);
		propFile.dataDir = new java.io.File("src/test/resources/").getCanonicalPath();
		
		listVersions = new ListVersions();
		listVersions.baseUri = "http://www.test.com/validate-ws/";
		listVersions.propertiesUtils = propFile;
	}

	@Test
	public void testGetVersions() throws Exception {
		assertEquals("<versions xmlns:xlink=\"http://www.w3.org/1999/xlink\"><version xlink:href=\"http://www.test.com/validate-ws//1.4\">1.4</version></versions>", listVersions.getVersions());		
	}

}
