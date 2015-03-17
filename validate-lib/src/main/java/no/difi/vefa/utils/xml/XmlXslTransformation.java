package no.difi.vefa.utils.xml;

import net.sf.saxon.TransformerFactoryImpl;
import no.difi.vefa.cache.XSLTransformerCache;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.utils.xml.resolver.XsltURIResolver;
import org.w3c.dom.Document;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;


/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 * <p>
 * This class is <b>not</b> thread safe and a new instance should be created for each transformation.
 * The parsed Templates object representing the xslFile will be fetched from the cache if it has been
 * used before so to improve performance.
 */
public class XmlXslTransformation {

    /**
     * Perform transformation of XML with XSL.
     *
     * @param xmlDoc  XML as Document
     * @param xslFile Path to XSL file as String
     * @return Document Result of transformation as Document
     * @throws Exception
     */
    public static Document transform(Document xmlDoc, String xslFile, Messages messages) throws Exception {
        try {
            return transform(xmlDoc, notCached(xslFile) ? parseAndCache(xslFile) : fetchFromCache(xslFile));
        } catch (Exception e) {
            messages.addMessage(MessageUtils.translate(e));
        }
        return null;
    }

    private static boolean notCached(String xslFile) {
        return !XSLTransformerCache.exists(xslFile);
    }

    private static Templates fetchFromCache(String xslFile) {
        return XSLTransformerCache.getTemplate(xslFile);
    }

    private static Templates parse(String xslFile) throws FileNotFoundException, TransformerConfigurationException {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance(TransformerFactoryImpl.class.getName(), null);
        transformerFactory.setURIResolver(new XsltURIResolver());
        final File file = new File(xslFile);
        if (file.exists()) {
            return transformerFactory.newTemplates(new StreamSource(file));
        }
        throw new FileNotFoundException("File not found");
    }

    private static Document transform(Document xmlDoc, Templates templates) throws Exception {
        try (StringWriter writer = new StringWriter()) {
            templates.newTransformer().transform(new DOMSource(xmlDoc), new StreamResult(writer));
            return XmlUtils.stringToDocument(writer.toString());
        }
    }

    private static Templates parseAndCache(String xslFile) throws TransformerConfigurationException, FileNotFoundException {
        Templates templates = parse(xslFile);
        cache(xslFile, templates);
        return templates;
    }

    private static void cache(String xslFile, Templates templates) {
        XSLTransformerCache.addTemplate(xslFile, templates);
    }

}
