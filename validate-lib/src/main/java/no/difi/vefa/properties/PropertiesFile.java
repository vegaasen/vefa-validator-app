package no.difi.vefa.properties;

import java.io.FileInputStream;
import java.util.Properties;

import no.difi.vefa.cache.PropertiesFileCache;

/**
 * This class can be used to read the application properties file
 */
public class PropertiesFile {
    /**
     * Properties file cache
     */	
	private PropertiesFileCache propertiesFileCache;
	
	/**
	 * Absolute path to data directory.
	 */
	public String dataDir;

	/**
	 * Suppress output warnings.
	 */
	public Boolean suppressWarnings;
	
	/**
	 * Enable logging of usage.
	 */	
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
