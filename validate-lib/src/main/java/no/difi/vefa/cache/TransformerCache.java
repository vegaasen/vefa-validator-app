package no.difi.vefa.cache;

import com.google.common.base.Strings;

import javax.xml.transform.Templates;
import java.util.HashMap;
import java.util.Map;

public final class TransformerCache {

    private static final Map<String, Templates> CACHE = new HashMap<>();

    private TransformerCache() {
    }

    /**
     * Adds a new Templates object to the Cache
     *
     * @param xslFileName The name of the xsl file
     * @param templates   The thread safe templates object which contains the parsed version of the xslFile
     */
    public static void addTemplate(String xslFileName, Templates templates) {
        CACHE.put(xslFileName, templates);
    }

    /**
     * Retrieves a Templates object from the cache
     *
     * @param xslFileName The name of the xsl file to retrieve
     * @return The Templates object containing the parsed xslFile, or null if not in the cache.
     */
    public static Templates getTemplate(String xslFileName) {
        if (!Strings.isNullOrEmpty(xslFileName) && CACHE.containsKey(xslFileName)) {
            return CACHE.get(xslFileName);
        }
        return null;
    }

    public static boolean exists(String key) {
        return CACHE.containsKey(key);
    }

    public static void clear() {
        CACHE.clear();
    }

}
