package no.difi.vefa.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertiesUtilsTest {

    @Test
    public void main_loadProperties_normalProcedure() throws Exception {
        String path = ClassLoader.getSystemResource("validator.properties").getPath();
        PropertiesUtils propFile = new PropertiesUtils();
        propFile.main(path);
        assertEquals(propFile.dataDir, "/etc/opt/VEFAvalidator");
        assertEquals(propFile.suppressWarnings, false);
        assertEquals(propFile.logStatistics, true);
    }
}
