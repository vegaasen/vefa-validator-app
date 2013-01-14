package no.difi.vefa.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * This class can be used to cache an XSL Transformer
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
     * Adds a new Transformer to the Cache
     *
     * @param id The ID of the widget
     * @param transformer The Transformer itself
     */
    public void addTransformer( String id, SynchronisedTransformer transformer )
    {
        // Create an EHCache Element to hold the widget
        Element element = new Element( id, transformer );

        // Add the element to the cache
        transformerCache.put( element );
    }

    /**
     * Retrieves a Transformer from the cache
     *
     * @param id        The ID of the Transformer to retrieve
     * @return          The requested Transformer or null if the Transformer is not in the cache
     */
    public SynchronisedTransformer getTransformer( String id )
    {
        // Retrieve the element that contains the requested Transformer
        Element element = transformerCache.get( id );
        if( element != null )
        {
            // Get the value out of the element and cast it to a Transformer.
        	// Using getObjectValue instead of getValue since Transformer is not serializable.
            return ( SynchronisedTransformer )element.getObjectValue();
        }

        // We don't have the object in the cache so return null
        return null;
    }
}
