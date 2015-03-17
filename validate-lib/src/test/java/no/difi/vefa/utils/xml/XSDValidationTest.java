package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.fail;

public class XSDValidationTest {

    private String xsdFile;
    private String xmlFile;

    @Before
    public void setUp() throws Exception {
        xsdFile = ClassLoader.getSystemResource("TestXSD.xsd").getPath();

        InputStream stream = ClassLoader.getSystemResourceAsStream("TestXSD.xml");
        xmlFile = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
    }

    @Test
    public void testMain() throws Exception {
        try {
            XSDValidation.validate(XmlUtils.stringToDocument(xmlFile), xsdFile, null);
        } catch (Exception e) {
            fail("XML has failed XSD validation");
        }
    }
}
