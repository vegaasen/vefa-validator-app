package no.difi.vefa.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.Properties;

/**
 * This class can be used to cache the properties file
 */
public class PropertiesFileCache {
    /**
     * The CacheManager provides us access to individual Cache instances
     */
    private static final CacheManager cacheManager = new CacheManager();

    /**
     * A cache that we're designating to hold Properties instances
     */
    private Ehcache propertiesCache;

    public PropertiesFileCache() {
        // Load our properties cache:
        propertiesCache = cacheManager.getEhcache("properties");
    }

    /**
     * Adds a new Properties to the Cache
     *
     * @param id         The ID of the widget
     * @param properties The Properties itself
     */
    public void addProperties(String id, Properties properties) {
        // Create an EHCache Element to hold the widget
        Element element = new Element(id, properties);

        // Add the element to the cache
        propertiesCache.put(element);
    }

    /**
     * Retrieves a Properties from the cache
     *
     * @param id The ID of the Properties to retrieve
     * @return The requested Properties or null if the Properties is not in the cache
     */
    public Properties getProperties(String id) {
        // Retrieve the element that contains the requested Properties
        Element element = propertiesCache.get(id);
        if (element != null) {
            // Get the value out of the element and cast it to a Properties.
            return (Properties) element.getValue();
        }

        // We don't have the object in the cache so return null
        return null;
    }
}
