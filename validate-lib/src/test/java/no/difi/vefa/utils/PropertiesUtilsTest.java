package no.difi.vefa.utils;

import no.difi.vefa.common.DifiConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PropertiesUtilsTest {

    @Test
    public void main_loadProperties_normalProcedure() throws Exception {
        System.setProperty(DifiConstants.Properties.PROPERTY_DATA_DIR, ClassLoader.getSystemResource("validator.properties").getPath());
        assertFalse(PropertiesUtils.INSTANCE.getDataDir().isEmpty());
        assertEquals(PropertiesUtils.INSTANCE.isSuppressWarnings(), false);
        assertEquals(PropertiesUtils.INSTANCE.isLogStatistics(), true);
    }
}
