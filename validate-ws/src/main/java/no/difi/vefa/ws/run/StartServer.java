package no.difi.vefa.ws.run;

import no.difi.vefa.ws.run.container.JettyContainer;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class StartServer {

    private static final int DEFAULT_PORT = 7007;

    public static void main(String... args) {
        JettyContainer.INSTANCE.start((args != null && args.length > 0) ? Integer.parseInt(args[0]) : 7007);
    }

}
