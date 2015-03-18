package no.difi.vefa.utils.xml;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.model.xml.namespace.XMLNamespace;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlUtilsTest {

    private String xmlTestString;
    private String xmlTestInvoice;
    private String xsdFile;
    private String xmlFile;

    @BeforeClass
    public static void initialize() {
        System.setProperty(
                DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath()
        );
    }

    @Before
    public void setUp() throws Exception {
        InputStream stream = ClassLoader.getSystemResourceAsStream("config.xml");
        xmlTestString = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
        stream = ClassLoader.getSystemResourceAsStream("Invoice.xml");
        xmlTestInvoice = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
        xsdFile = ClassLoader.getSystemResource("TestXSD.xsd").getPath();
        stream = ClassLoader.getSystemResourceAsStream("TestXSD.xml");
        xmlFile = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        Closeables.closeQuietly(stream);
    }

    @Test
    public void validateXmlByXsd_normalProcedure() throws Exception {
        XmlUtils.validateXmlByXsd(XmlUtils.stringToDocument(xmlFile), xsdFile, null);
    }

    @Test
    public void validateXmlByXsd_normalProcedure_fail() throws Exception {
        Messages messages = new Messages();
        XmlUtils.validateXmlByXsd(XmlUtils.stringToDocument(xmlFile), null, messages);
        assertFalse(messages.getMessages().isEmpty());
        assertEquals(1, messages.getMessages().size());
    }

    @Test
    public void stringToDocument_normalProcedure() throws Exception {
        Document xmlDoc = XmlUtils.stringToDocument("<data><test>This is a test</test></data>");

        String expected = "<data><test>This is a test</test></data>";
        String result = XmlUtils.documentToString(xmlDoc);

        assertEquals(expected, result);
    }

    @Test
    public void stringToDocument_normalProcedure_twoSimilarDocuments() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        Document xmlDoc2 = XmlUtils.stringToDocument(xmlTestString);
        assertEquals(XmlUtils.documentToString(xmlDoc1), XmlUtils.documentToString(xmlDoc2));
    }

    @Test
    public void xmlDOMXPathQuery_findNodeListByXPath_singleMatch() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        NodeList validate = XmlUtils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']");
        assertEquals(1, validate.getLength());
    }

    @Test
    public void xmlDOMXPathQueryWithNS_verifyXpathQueries() throws Exception {
        List<XMLNamespace> namespaces = new ArrayList<>();
        XMLNamespace ns = new XMLNamespace();
        ns.setPrefix("cbc");
        ns.setUrl("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
        namespaces.add(ns);

        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestInvoice);
        Node pId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:ProfileID", namespaces).item(0);
        Node cId = XmlUtils.xmlDOMXPathQueryWithNS(xmlDoc1, "*/cbc:CustomizationID", namespaces).item(0);
        assertEquals("urn:www.cenbii.eu:profile:bii04:ver1.0", pId.getTextContent().trim());
        assertEquals("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1", cId.getTextContent().trim());
    }

    @Test
    public void innerXml_normalProcedure() throws Exception {
        Document xmlDoc1 = XmlUtils.stringToDocument(xmlTestString);
        Node name = XmlUtils.xmlDOMXPathQuery(xmlDoc1, "/config/validate[@id='urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1' and @version='1.4']/name").item(0);

        String myXML = "<name>\n" +
                "            <en>EHF invoice in Norway, profile invoice only</en>\n" +
                "            <no>EHF faktura i Norge, profil kun faktura</no>\n" +
                "        </name>";

        assertEquals(myXML, XmlUtils.innerXml(name));
    }

    @Test
    public void isWellFormatted_normalProcedure() throws Exception {
        Messages messages = new Messages();
        XmlUtils.isWellFormatted("<data><test>Hello world</test></data>", messages);
        assertEquals(0, messages.getMessages().size());
    }

    @Test
    public void isWellFormatted_normalProcedure_failed() throws Exception {
        Messages messages = new Messages();
        XmlUtils.isWellFormatted("<data><test>Hello world</test></data>error", messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XMLWellFormed, messages.getMessages().get(0).getValidationType());
    }

    @Test
    public void validateXmlByXsd_normalProcedure_various() throws Exception {
        Document xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("TestXSD.xml"),
                Charsets.UTF_8)));
        Messages messages = new Messages();
        XmlUtils.validateXmlByXsd(xmlDoc, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(0, messages.getMessages().size());

        xmlDoc = XmlUtils.stringToDocument(CharStreams.toString(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("TestXSDWithErrors.xml"),
                Charsets.UTF_8)));
        messages = new Messages();
        XmlUtils.validateXmlByXsd(xmlDoc, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());

        messages = new Messages();
        XmlUtils.validateXmlByXsd(null, ClassLoader.getSystemResource("TestXSD.xsd").getPath(), messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XSD, messages.getMessages().get(0).getValidationType());
    }

    @Test
    public void fileToDocument_convertXmlToDocument() throws Exception {
        Document configDoc = XmlUtils.fileToDocument(
                ClassLoader.getSystemResource("config.xml").getPath()
        );
        assertEquals(configDoc.getElementsByTagName("step").item(0).getAttributes().getNamedItem("id").getNodeValue(), "XSL");
    }


}
