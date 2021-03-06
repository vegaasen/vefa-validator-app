package no.difi.vefa.validation;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.common.DifiConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class KdaValidateTest {

    private Validate validate;

    @Before
    public void setUp() throws Exception {
        System.setProperty(DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath());
        validate = new Validate();
    }

    @Test
    public void faktura_gyldig() throws Exception {
        validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(false);
        validate.setSource(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("kda/faktura-gyldig.xml"),
                Charsets.UTF_8)));
        validate.setId("urn:www.cenbii.eu:profile:bii05:ver2.0#urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0");
        validate.setVersion("2.0");
        validate.validate();
        validate.getMessages();
        assertEquals(1, validate.getMessages().getMessages().size());
    }

    @Test
    public void kreditnota_gyldig() throws Exception {
        validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(false);
        validate.setSource(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("kda/kreditnota-gyldig.xml"),
                Charsets.UTF_8)));
        validate.setId("urn:www.cenbii.eu:profile:bii05:ver2.0#urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:kreditnota:ver2.0");
        validate.setVersion("2.0");
        validate.validate();
        validate.getMessages();
        assertTrue(validate.getMessages().getMessages().isEmpty());
    }

    @Test
    public void test_missing() throws Exception {
        validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(false);
        validate.setSource(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("kda/invoice-test-missing.xml"),
                Charsets.UTF_8)));
        validate.setId("urn:www.cenbii.eu:profile:bii05:ver2.0#urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0");
        validate.setVersion("2.0");
        validate.validate();
        validate.getMessages();
    }

}
