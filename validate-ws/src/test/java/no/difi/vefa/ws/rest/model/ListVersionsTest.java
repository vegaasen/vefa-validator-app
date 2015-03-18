package no.difi.vefa.ws.rest.model;

import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.ws.rest.model.ListVersions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ListVersionsTest {

    private ListVersions listVersions;

    @Before
    public void setUp() throws Exception {
        System.setProperty(
                DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath()
        );
        listVersions = new ListVersions();
        listVersions.baseUri = "http://www.test.com/validate-ws/";
    }

    @Test
    public void getVersions_hasContent() throws Exception {
        assertFalse(listVersions.getVersions().isEmpty());
    }

}
