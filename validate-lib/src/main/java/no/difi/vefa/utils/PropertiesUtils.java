package no.difi.vefa.utils;

import no.difi.vefa.cache.PropertiesFileCache;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static final String DATA_DIR = "DATA_DIR";
    private static final String SUPPRESS_WARNINGS = "SUPPRESS_WARNINGS";
    private static final String LOG_STATISTICS = "LOG_STATISTICS";

    public String dataDir;
    public boolean suppressWarnings;
    public boolean logStatistics;

    /**
     * Load properties file and set variables.
     *
     * @param propertiesFile Path to properties file as String
     * @throws Exception
     */
    public void main(String propertiesFile) throws Exception {
        PropertiesFileCache propertiesFileCache = new PropertiesFileCache();
        Properties configFile = propertiesFileCache.getProperties(propertiesFile);
        if (configFile == null) {
            configFile = new Properties();
            configFile.load(new FileInputStream(propertiesFile));
            propertiesFileCache.addProperties(propertiesFile, configFile);
        }
        this.dataDir = configFile.getProperty(DATA_DIR);
        this.suppressWarnings = Boolean.parseBoolean(configFile.getProperty(SUPPRESS_WARNINGS));
        this.logStatistics = Boolean.parseBoolean(configFile.getProperty(LOG_STATISTICS));
    }
}
