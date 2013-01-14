package no.difi.vefa.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import javax.xml.transform.Templates;

/**
 * This class can be used to cache an XSL Templates object
 * The XSL Templates object is thread safe by design and can be used to
 * obtain a new instance of a Transformer which is not thread safe.
 */
public class XSLTransformerCache {
    /**
     * The CacheManager provides us access to individual Cache instances
     */
    private static final CacheManager cacheManager = new CacheManager();

    /**
     * A cache that we're designating to hold Transformer instances
     */
    private Ehcache transformerCache;

    public XSLTransformerCache()
    {
        // Load our Transformer cache:
    	transformerCache = cacheManager.getEhcache( "transformer" );
    }

    /**
     * Adds a new Templates object to the Cache
     *
     * @param xslFileName The name of the xsl file
     * @param templates The thread safe templates object which contains the parsed version of the xslFile
     */
    public void addTemplate(String xslFileName, Templates templates)
    {
        // Create an EHCache Element to hold the widget
        Element templatesElement = new Element( xslFileName, templates );

        // Add the element to the cache
        transformerCache.put( templatesElement );
    }

    /**
     * Retrieves a Templates object from the cache
     *
     * @param xslFileName   The name of the xsl file to retrieve
     * @return The Templates object containing the parsed xslFile, or null if not in the cache.
     */
    public Templates getTemplate(String xslFileName) {
        // Retrieve the element that contains the requested Templates object
        Element element = transformerCache.get( xslFileName );
        if( element != null )
        {
        	// Using getObjectValue instead of getValue since Templates object is not serializable.
            Templates objectValue = (Templates) element.getObjectValue();
            return objectValue;
        }

        // We don't have the object in the cache so return null
        return null;
    }
}
