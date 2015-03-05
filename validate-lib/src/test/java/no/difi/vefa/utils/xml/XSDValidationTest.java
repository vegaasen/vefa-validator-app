package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import no.difi.vefa.utils.PropertiesUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.fail;

public class XSDValidationTest {

    private String xsdFile;
    private String xmlFile;
    private PropertiesUtils propFile;

    @Before
    public void setUp() throws Exception {
        xsdFile = ClassLoader.getSystemResource("TestXSD.xsd").getPath();

        InputStream stream = ClassLoader.getSystemResourceAsStream("TestXSD.xml");
        xmlFile = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);

        propFile = new PropertiesUtils();
        propFile.main(ClassLoader.getSystemResource("validator.properties").getPath());
        propFile.dataDir = new java.io.File(".").getCanonicalPath();
    }

    @Test
    public void testMain() throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        Document xmlDoc = xmlUtils.stringToXMLDOM(xmlFile);

        no.difi.vefa.utils.xml.XSDValidation xsdValidation = new no.difi.vefa.utils.xml.XSDValidation();

        try {
            xsdValidation.main(xmlDoc, xsdFile, propFile);
        } catch (Exception e) {
            fail("XML has failed XSD validation");
        }
    }
}
