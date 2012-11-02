package no.difi.vefa.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import no.difi.vefa.properties.PropertiesFile;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * This class can be used to check if an XML Document
 * validate against a XSD Document.
 */
public class XSDValidation {

	/**
	 * Perform validation of XML Document with a XSD Document.
	 * 
	 * @param xmlDoc XML as Document
	 * @param xsdFile Path to XSD file as String
	 * @param propertiesFile PropertiesFile
	 * @throws Exception
	 */		
	public void main(Document xmlDoc, String xsdFile, PropertiesFile propertiesFile) throws Exception {
		Utils utils = new Utils();
		FileReader readerXsd = new FileReader(xsdFile);		
		StringReader readerXml = new StringReader(utils.xmlDOMToString(xmlDoc));

		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		ClasspathResourceResolver classPathResourceResolver = new ClasspathResourceResolver();
		classPathResourceResolver.propertiesFile = propertiesFile;
		
		factory.setResourceResolver(classPathResourceResolver);
        
		Schema schema = factory.newSchema(new StreamSource(readerXsd));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(readerXml));
	}
	
	public class ClasspathResourceResolver implements LSResourceResolver {
		
		public PropertiesFile propertiesFile;
		
	    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
	        InputStream stream = null;
			try {
				stream = new FileInputStream(this.propertiesFile.dataDir + systemId);				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	        	    	
			return new Input(publicId, systemId, stream);
	    }
	}
	
	public class Input implements LSInput {

		private String publicId;

		private String systemId;

		public String getPublicId() {
		    return publicId;
		}

		public void setPublicId(String publicId) {
		    this.publicId = publicId;
		}

		public String getBaseURI() {
		    return null;
		}

		public InputStream getByteStream() {
		    return null;
		}

		public boolean getCertifiedText() {
		    return false;
		}

		public Reader getCharacterStream() {
		    return null;
		}

		public String getEncoding() {
		    return null;
		}

		public String getStringData() {
		    synchronized (inputStream) {
		        try {
		            byte[] input = new byte[inputStream.available()];
		            inputStream.read(input);
		            String contents = new String(input);
		            return contents;
		        } catch (IOException e) {
		            e.printStackTrace();
		            System.out.println("Exception " + e);
		            return null;
		        }
		    }
		}

		public void setBaseURI(String baseURI) {
		}

		public void setByteStream(InputStream byteStream) {
		}

		public void setCertifiedText(boolean certifiedText) {
		}

		public void setCharacterStream(Reader characterStream) {
		}

		public void setEncoding(String encoding) {
		}

		public void setStringData(String stringData) {
		}

		public String getSystemId() {
		    return systemId;
		}

		public void setSystemId(String systemId) {
		    this.systemId = systemId;
		}

		public BufferedInputStream getInputStream() {
		    return inputStream;
		}

		public void setInputStream(BufferedInputStream inputStream) {
		    this.inputStream = inputStream;
		}

		private BufferedInputStream inputStream;

		public Input(String publicId, String sysId, InputStream input) {
		    this.publicId = publicId;
		    this.systemId = sysId;
		    this.inputStream = new BufferedInputStream(input);
		}
	}		
}
