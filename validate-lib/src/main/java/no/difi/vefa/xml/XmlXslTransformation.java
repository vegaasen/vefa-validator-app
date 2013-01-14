package no.difi.vefa.xml;

import no.difi.vefa.cache.SynchronisedTransformer;
import no.difi.vefa.cache.XSLTransformerCache;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;


/**
 * This class can be used to perform transformation of XML Document
 * with an XSL Document.
 *
 * This class is not thread safe.
 */
public class XmlXslTransformation {
    /**
     * XSL Transformer cache
     */	
	private final XSLTransformerCache xslTransformerCache;
    private SynchronisedTransformer synchronisedTransformer;


    public XmlXslTransformation() {
        this.xslTransformerCache = new XSLTransformerCache();
    }

    /**
	 * Perform transformation of XML with XML.
	 * 
	 * @param xmlDoc XML as Document
	 * @param xslFile Path to XSL file as String
	 * @return Document Result of transformation as Document
	 * @throws Exception
	 */	
	public Document main(Document xmlDoc, String xslFile) throws Exception {

        // Check if XML transformer is cached
        if (notCached(xslFile)) {
            synchronisedTransformer = createAndCacheTransformer(xslFile);
        }

        return transform(xmlDoc);
	}

    private boolean notCached(String xslFile) {
        synchronisedTransformer = fetchFromCache(xslFile);
        return synchronisedTransformer == null;
    }

    private Document transform(Document xmlDoc) throws Exception {
        Utils utils = new Utils();
        StringWriter writer = new StringWriter();

        synchronisedTransformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));

        // Load transformed XML string as XML DOM
        return utils.stringToXMLDOM(writer.toString());
    }

    private SynchronisedTransformer fetchFromCache(String xslFile) {
        return xslTransformerCache.getTransformer(xslFile);
    }

    private SynchronisedTransformer createAndCacheTransformer(String xslFile) throws TransformerConfigurationException, FileNotFoundException {
        SynchronisedTransformer synchronisedTransformer = createTransformer(xslFile);
        xslTransformerCache.addTransformer(xslFile, synchronisedTransformer);
        return synchronisedTransformer;
    }

    private SynchronisedTransformer createTransformer(String xslFile) throws FileNotFoundException, TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        FileReader readerXsl = new FileReader(xslFile);
        Transformer transformer = tFactory.newTransformer(new StreamSource(readerXsl));
        return new SynchronisedTransformer(transformer);
    }
}
