package no.difi.vefa.soap.run;

import no.difi.vefa.soap.run.container.JettyContainer;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class StartServer {

    public static void main(String... args) {
        JettyContainer.INSTANCE.start(0);
    }

}
