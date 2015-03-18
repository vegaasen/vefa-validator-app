package no.difi.vefa.ws.run;

import com.google.common.base.Strings;
import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.ws.run.container.JettyContainer;

/**
 * Simple servers-starter for the DIFI WS application.
 * <p>
 * Usage is quite simple and straight forward. It requires that you have the data-dir property as a param. Similar to this:
 * <p>
 * java -jar -Dno.difi.vefa.validation.configuration.datadir=/Users/vegardaasen/develop/github/vefa-validator-app/validate-lib/src/test/resources/validator.properties (file).jar
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 * @version 1.0
 * @see DifiConstants.Properties
 * @since 18.3.2015
 */
public class StartServer {

    private static final int DEFAULT_PORT = 7007;

    public static void main(String... args) {
        if (Strings.isNullOrEmpty(System.getProperty(DifiConstants.Properties.PROPERTY_DATA_DIR))) {
            System.out.println(usage());
            return;
        }
        JettyContainer.INSTANCE.start((args != null && args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT);
    }

    private static String usage() {
        return String.format("Required systemProperty {%s} is missing. Usage: %njava -jar -D%s=/path/to/configuration-folder/ <fileName>.jar <(optional) port>", DifiConstants.Properties.PROPERTY_DATA_DIR, DifiConstants.Properties.PROPERTY_DATA_DIR);
    }

}
