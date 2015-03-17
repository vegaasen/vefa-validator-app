package no.difi.vefa.ws.run;

import com.google.common.base.Strings;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.ws.run.container.JettyContainer;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class StartServer {

    private static final int DEFAULT_PORT = 7007;

    public static void main(String... args) {
        if (Strings.isNullOrEmpty(System.getProperty(PropertiesUtils.PROPERTY_DATA_DIR))) {
            System.out.println(usage());
            return;
        }
        JettyContainer.INSTANCE.start((args != null && args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT);
    }

    private static String usage() {
        return String.format("Required systemProperty {%s} is missing. Usage: %njava -jar -D%s /path/to/configuration-folder/ <fileName>.jar <(optional) port>", PropertiesUtils.PROPERTY_DATA_DIR, PropertiesUtils.PROPERTY_DATA_DIR);
    }

}
