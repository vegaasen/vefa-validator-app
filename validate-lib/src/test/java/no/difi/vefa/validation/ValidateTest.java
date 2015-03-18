package no.difi.vefa.validation;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.model.message.MessageType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class ValidateTest {

    private Validate validate;

    @BeforeClass
    public static void initialize() {
        System.setProperty(
                DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath());
    }

    @Before
    public void setUp() throws Exception {
        validate = new Validate();
    }

    @Test
    public void testNotWellFormedXML() throws Exception {
        validate = new Validate();
        validate.setVersion("test");
        validate.setId("test");
        validate.setSource("<test>notwellformed</testtest>");
        validate.validate();

        assertEquals(MessageType.Fatal, validate.getMessages().getMessages().get(0).getMessageType());
    }

    @Test
    public void testAutoDetectionOfVersionAndSchema() throws Exception {
        validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(true);
        validate.setSource(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("Invoice.xml"),
                Charsets.UTF_8)));
        validate.validate();
    }

}
