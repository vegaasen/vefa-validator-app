package no.difi.vefa.ws.run;

import com.google.common.base.Strings;
import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.ws.rest.common.ApplicationContext;
import no.difi.vefa.ws.run.container.JettyContainer;
import no.difi.vefa.ws.utils.StopUtils;

/**
 * Simple servers-starter for the DIFI WS application.
 * <p>
 * Usage is quite simple and straight forward. It requires that you have the data-dir property as a param. Similar to this:
 * <p>
 * java -jar -Dno.difi.vefa.validation.configuration.datadir=/Users/vegardaasen/develop/github/vefa-validator-app/validate-lib/src/test/resources/validator.properties (file).jar <(required) start,stop>
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 * @version 1.1
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
        if (args.length == 0) {
            System.out.println(usage());
            return;
        }
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        final String operation = args[0];
        switch (operation) {
            case "start":
                ApplicationContext.INSTANCE.generateAndStoreKillWord();
                System.out.println(String.format("Application can be killed using {%s}", ApplicationContext.INSTANCE.getKillWord()));
                JettyContainer.INSTANCE.start(port);
                break;
            case "stop":
                StopUtils.INSTANCE.kill(port);
                break;
            default:
                System.out.println(String.format("Unable to accept operation {%s}", operation));
                System.out.println(usage());
                break;
        }
    }

    private static String usage() {
        return String.format("Required systemProperty {%s} is missing. Usage: %njava -jar -D%s=/path/to/configuration-folder/ <fileName>.jar <(required) start,stop> <(optional) port>", DifiConstants.Properties.PROPERTY_DATA_DIR, DifiConstants.Properties.PROPERTY_DATA_DIR);
    }

}
