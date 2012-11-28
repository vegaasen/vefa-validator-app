package no.difi.vefa.cache;

import org.w3c.dom.Document;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * This class can be used to cache the configuration xml files
 */
public class ConfigurationCache {
    /**
     * The CacheManager provides us access to individual Cache instances
     */
    private static final CacheManager cacheManager = new CacheManager();

    /**
     * A cache that we're designating to hold Document instances
     */
    private Ehcache configurationCache;

    public ConfigurationCache()
    {
        // Load our Configuration cache:
    	configurationCache = cacheManager.getEhcache( "configuration" );
    }

    /**
     * Adds a new Configuration to the Cache
     *
     * @param id The ID of the widget
     * @param document The Configuration itself
     */
    public void addConfiguration( String id, Document document )
    {
        // Create an EHCache Element to hold the widget
        Element element = new Element( id, document );

        // Add the element to the cache
        configurationCache.put( element );
    }

    /**
     * Retrieves a Configuration from the cache
     *
     * @param id        The ID of the Configuration to retrieve
     * @return          The requested Configuration or null if the Configuration is not in the cache
     */
    public Document getConfiguration( String id )
    {
        // Retrieve the element that contains the requested Configuration
        Element element = configurationCache.get( id );
        if( element != null )
        {
            // Get the value out of the element and cast it to a Configuration.
        	// Using getObjectValue instead of getValue since Configuration is not serializable.
            return ( Document )element.getObjectValue();
        }

        // We don't have the object in the cache so return null
        return null;
    }
}
