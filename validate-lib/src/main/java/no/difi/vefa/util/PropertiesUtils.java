package no.difi.vefa.util;

import no.difi.vefa.cache.PropertiesFileCache;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * This class can be used to read the application properties file
 */
public class PropertiesUtils {

    private PropertiesFileCache propertiesFileCache;
    public String dataDir;
    public Boolean suppressWarnings;
    public Boolean logStatistics;

    /**
     * Load properties file and set variables.
     *
     * @param propertiesFile Path to properties file as String
     * @throws Exception
     */
    public void main(String propertiesFile) throws Exception {
        // Check if properties is cached
        propertiesFileCache = new PropertiesFileCache();
        Properties configFile = propertiesFileCache.getProperties(propertiesFile);

        if (configFile == null) {
            configFile = new Properties();
            configFile.load(new FileInputStream(propertiesFile));
            propertiesFileCache.addProperties(propertiesFile, configFile);
        }

        this.dataDir = configFile.getProperty("DATA_DIR");
        this.suppressWarnings = "true".equals(configFile.getProperty("SUPPRESS_WARNINGS"));
        this.logStatistics = "true".equals(configFile.getProperty("LOG_STATISTICS"));
    }
}
