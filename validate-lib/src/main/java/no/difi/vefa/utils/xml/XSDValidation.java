package no.difi.vefa.utils.xml;

import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.model.xml.ClassPathResourceResolver;
import no.difi.vefa.utils.MessageUtils;
import org.w3c.dom.Document;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileReader;
import java.io.StringReader;

/**
 * This class can be used to check if an XML Document
 * validate against a XSD Document.
 */
public class XSDValidation {

    public static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    /**
     * Perform validation of XML Document with a XSD Document.
     *
     * @param xmlDoc  XML as Document
     * @param xsdFile Path to XSD file as String
     */
    public static void validate(Document xmlDoc, String xsdFile, Messages messages) {
        try (
                FileReader readerXsd = new FileReader(xsdFile);
                StringReader readerXml = new StringReader(XmlUtils.documentToString(xmlDoc))
        ) {
            SchemaFactory factory = SchemaFactory.newInstance(SCHEMA_LANGUAGE);
            factory.setResourceResolver(new ClassPathResourceResolver());
            Schema schema = factory.newSchema(new StreamSource(readerXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(readerXml));
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e, ValidationType.XSD));
        }
    }

}
