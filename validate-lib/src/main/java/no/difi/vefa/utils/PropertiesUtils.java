package no.difi.vefa.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public enum PropertiesUtils {

    INSTANCE;

    public static final String PROPERTY_DATA_DIR = "no.difi.vefa.validation.configuration.datadir";

    private static final String DATA_DIR = "DATA_DIR";
    private static final String SUPPRESS_WARNINGS = "SUPPRESS_WARNINGS";
    private static final String LOG_STATISTICS = "LOG_STATISTICS";

    private Properties properties = new Properties();

    public String getProperty(final String key) {
        return getProperties().getProperty(key);
    }

    public String getDataDir() {
        return getProperty(DATA_DIR);
    }

    public boolean isSuppressWarnings() {
        return Boolean.parseBoolean(getProperty(SUPPRESS_WARNINGS));
    }

    public boolean isLogStatistics() {
        return Boolean.parseBoolean(getProperty(LOG_STATISTICS));
    }

    private Properties getProperties() {
        if (properties.isEmpty()) {
            loadProperties();
        }
        return properties;
    }

    private void loadProperties() {
        String vefaValidatorDir = System.getProperty(PROPERTY_DATA_DIR);
        vefaValidatorDir = vefaValidatorDir.contains("properties") ? vefaValidatorDir : vefaValidatorDir + "/validator.properties";
        try {
            properties.load(new FileInputStream(vefaValidatorDir));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Missing required system property {%s}", PROPERTY_DATA_DIR));
        }
    }
}
