package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import no.difi.vefa.model.xml.namespace.XMLNamespace;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XmlUtilsTest {

    private String xmlTestString;
    private String xmlTestInvoice;

    @Before
    public void setUp() throws Exception {
        InputStream stream = ClassLoader.getSystemResourceAsStream("config.xml");
        xmlTestString = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
        stream = ClassLoader.getSystemResourceAsStream("Invoice.xml");
        xmlTestInvoice = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
    }

    @Test
    public void testXmlDOMToString() throws Exception {
        Document xmlDoc = XmlUtils.stringToDocument("<data><test>This is a test</test></data>");

        String expected = "<data><test>This is a test</test></data>";
        String result = XmlUtils.documentToString(xmlDoc);

        assertEquals(expected, result);
    }

    @Test
    public void testStringToXMLDOM() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        Document xmlDoc2 = XmlUtils.stringToDocument(xmlTestString);
        assertEquals(XmlUtils.documentToString(xmlDoc1), XmlUtils.documentToString(xmlDoc2));
    }

    @Test
    public void testXmlDOMXPathQuery() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        NodeList validate = XmlUtils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']");
        assertEquals(1, validate.getLength());
    }

    @Test
    public void testXmlDOMXPathQueryWithNS() throws Exception {
        List<XMLNamespace> namespaces = new ArrayList<>();
        XMLNamespace ns = new XMLNamespace();
        ns.setPrefix("cbc");
        ns.setUrl("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
        namespaces.add(ns);

        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestInvoice);
        Node pId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:ProfileID", namespaces).item(0);
        Node cId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:CustomizationID", namespaces).item(0);
        assertEquals("urn:www.cenbii.eu:profile:bii04:ver1.0", pId.getTextContent());
        assertEquals("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1", cId.getTextContent());
    }

    @Test
    public void testInnerXml() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        Node name = XmlUtils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']/name").item(0);

        String myXML = "<name xmlns:xi=\"http://www.w3.org/2001/XInclude\">\n" +
                "\t\t\t<en>EHF invoice in Norway, profile invoice only</en>\n" +
                "\t\t\t<no>EHF faktura i Norge, profil kun faktura</no>\n" +
                "\t\t</name>";

        assertEquals(myXML, XmlUtils.innerXml(name));
    }
}
