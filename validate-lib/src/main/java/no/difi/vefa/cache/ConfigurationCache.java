package no.difi.vefa.cache;

import com.google.common.base.Strings;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public final class ConfigurationCache {

    private static final Map<String, Document> CACHE = new HashMap<>();

    /**
     * Adds a new Configuration to the Cache
     *
     * @param id       The ID of the widget
     * @param document The Configuration itself
     */
    public static void addConfiguration(String id, Document document) {
        CACHE.put(id, document);
    }

    /**
     * Retrieves a Configuration from the cache
     *
     * @param id The ID of the Configuration to retrieve
     * @return The requested Configuration or null if the Configuration is not in the cache
     */
    public static Document getConfiguration(String id) {
        if (!Strings.isNullOrEmpty(id) && CACHE.containsKey(id)) {
            return CACHE.get(id);
        }
        return null;
    }
}
