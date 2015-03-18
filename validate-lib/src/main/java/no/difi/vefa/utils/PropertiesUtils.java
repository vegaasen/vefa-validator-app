package no.difi.vefa.utils;

import no.difi.vefa.common.DifiConstants;

import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public enum PropertiesUtils {

    INSTANCE;

    private static final String DATA_DIR = "DATA_DIR";
    private static final String SUPPRESS_WARNINGS = "SUPPRESS_WARNINGS";
    private static final String LOG_STATISTICS = "LOG_STATISTICS";

    private Properties properties = new Properties();

    static {
        System.setProperty(DifiConstants.Properties.LOGGING, INSTANCE.getDataDir() + "/LOG");
    }

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
        String vefaVaidatorPropertiesFile = System.getProperty(DifiConstants.Properties.PROPERTY_DATA_DIR);
        try {
            if (Files.exists(Paths.get(vefaVaidatorPropertiesFile))) {
                properties.load(new FileInputStream(vefaVaidatorPropertiesFile));
            } else {
                throw new RuntimeException(String.format("VEFAValidator file candidate {%s} not found. Verify the path", vefaVaidatorPropertiesFile));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Missing required system property {%s}", DifiConstants.Properties.PROPERTY_DATA_DIR));
        }
    }
}
