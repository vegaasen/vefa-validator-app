package no.difi.vefa.ws.rest.model;

import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.ws.rest.model.ListIdentifier;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ListIdentifierTest {

    private ListIdentifier listIdentifier;

    @Before
    public void setUp() throws Exception {
        System.setProperty(
                DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath()
        );

        listIdentifier = new ListIdentifier();
        listIdentifier.version = "1.4";
        listIdentifier.baseUri = "http://www.test.com/validate-ws/";
    }

    @Test
    public void getIdentifier_foundConfiguration() throws Exception {
        assertFalse(listIdentifier.getIdentifier().isEmpty());
    }

}
