package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class XmlXslTransformationTest {

    private String xmlFile;

    @Before
    public void setUp() throws Exception {
        InputStream stream = ClassLoader.getSystemResourceAsStream("Invoice.xml");
        xmlFile = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
    }

    @Test
    public void testMain() throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        Document xmlDoc = xmlUtils.stringToXMLDOM(xmlFile);

        no.difi.vefa.utils.xml.XmlXslTransformation xmlXslTransformation = new no.difi.vefa.utils.xml.XmlXslTransformation();
        Document result = xmlXslTransformation.main(xmlDoc, ClassLoader.getSystemResource("Test.xsl").getPath());

        assertEquals("true", result.getElementsByTagName("status").item(0).getTextContent());
    }
}
