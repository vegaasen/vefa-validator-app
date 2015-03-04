package no.difi.vefa.ws.run;

import no.difi.vefa.ws.run.container.JettyContainer;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class StartServer {

    public static void main(String... args) {
        JettyContainer.INSTANCE.start(7007);
    }

}
