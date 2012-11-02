package no.difi.vefa.properties;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * This class can be used to read the application properties file
 */
public class PropertiesFile {	
	/**
	 * Absolute path to data directory.
	 */
	public String dataDir;

	/**
	 * Suppress output warnings.
	 */
	public Boolean suppressWarnings;
	
	/**
	 * Load properties file and set variables.
	 * 
	 * @param propertiesFile Path to properties file as String
	 * @throws Exception
	 */		
	public void main(String propertiesFile) throws Exception {
		Properties configFile = new Properties();
		configFile.load(new FileInputStream(propertiesFile));
		this.dataDir = configFile.getProperty("DATA_DIR");
		this.suppressWarnings = "true".equals(configFile.getProperty("SUPPRESS_WARNINGS"));
	}
}
