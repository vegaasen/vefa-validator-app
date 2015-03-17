package no.difi.vefa.utils.xml;

import net.sf.saxon.TransformerFactoryImpl;
import no.difi.vefa.cache.XSLTransformerCache;
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

    private final XSLTransformerCache xslTransformerCache;
    private Templates templates;


    public XmlXslTransformation() {
        this.xslTransformerCache = new XSLTransformerCache();
    }

    /**
     * Perform transformation of XML with XSL.
     *
     * @param xmlDoc  XML as Document
     * @param xslFile Path to XSL file as String
     * @return Document Result of transformation as Document
     * @throws Exception
     */
    public Document main(Document xmlDoc, String xslFile) throws Exception {

        // Check if the xslFile Templates object is cached
        if (notCached(xslFile)) {
            parseAndCache(xslFile);
        }

        return transform(xmlDoc);
    }

    private boolean notCached(String xslFile) {
        templates = fetchFromCache(xslFile);
        return templates == null;
    }

    private Templates fetchFromCache(String xslFile) {
        return xslTransformerCache.getTemplate(xslFile);
    }

    private void parseAndCache(String xslFile) throws TransformerConfigurationException, FileNotFoundException {
        templates = parse(xslFile);
        cache(xslFile, templates);
    }

    private Templates parse(String xslFile) throws FileNotFoundException, TransformerConfigurationException {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance(TransformerFactoryImpl.class.getName(), null);
        transformerFactory.setURIResolver(new XsltURIResolver());
        final File file = new File(xslFile);
        if (file.exists()) {
            return transformerFactory.newTemplates(new StreamSource(file));
        }
        throw new FileNotFoundException("File not found");
    }

    private void cache(String xslFile, Templates templates) {
        xslTransformerCache.addTemplate(xslFile, templates);
    }

    private Document transform(Document xmlDoc) throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        StringWriter writer = new StringWriter();

        templates.newTransformer().transform(new DOMSource(xmlDoc), new StreamResult(writer));

        // The writer contains the result of the transformation.
        return xmlUtils.stringToXMLDOM(writer.toString());
    }

}
