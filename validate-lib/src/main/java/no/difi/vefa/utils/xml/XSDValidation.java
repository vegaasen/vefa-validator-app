package no.difi.vefa.utils.xml;

import no.difi.vefa.utils.PropertiesUtils;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * This class can be used to check if an XML Document
 * validate against a XSD Document.
 */
public class XSDValidation {

    /**
     * Perform validation of XML Document with a XSD Document.
     *
     * @param xmlDoc          XML as Document
     * @param xsdFile         Path to XSD file as String
     * @param propertiesUtils PropertiesFile
     * @throws Exception
     */
    public void main(Document xmlDoc, String xsdFile, PropertiesUtils propertiesUtils) throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        FileReader readerXsd = new FileReader(xsdFile);
        StringReader readerXml = new StringReader(xmlUtils.xmlDOMToString(xmlDoc));

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        ClasspathResourceResolver classPathResourceResolver = new ClasspathResourceResolver();
        classPathResourceResolver.propertiesUtils = propertiesUtils;

        factory.setResourceResolver(classPathResourceResolver);

        Schema schema = factory.newSchema(new StreamSource(readerXsd));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(readerXml));
    }

    public class ClasspathResourceResolver implements LSResourceResolver {

        public PropertiesUtils propertiesUtils;

        @Override
        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(this.propertiesUtils.dataDir + systemId);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new Input(publicId, systemId, stream);
        }
    }

    public class Input implements LSInput {

        private final BufferedInputStream inputStream;
        private String publicId;
        private String systemId;

        public Input(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {
            return null;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        @Override
        public String getStringData() {
            synchronized (inputStream) {
                try {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    return new String(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Exception " + e);
                    return null;
                }
            }
        }

    }
}
