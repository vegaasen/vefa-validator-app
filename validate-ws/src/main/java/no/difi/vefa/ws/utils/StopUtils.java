package no.difi.vefa.ws.utils;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import no.difi.vefa.ws.rest.common.RestAttributes;
import no.difi.vefa.ws.run.container.JettyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Simple utilities which helps in the process of killing running instance on a given port
 *
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public enum StopUtils {

    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(StopUtils.class);

    public void kill(int port) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) requestPasscodeUrl(port).openConnection();
            if (urlConnection != null) {
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    if (inputStream != null) {
                        final String passCode = CharStreams.toString(new InputStreamReader(inputStream));
                        urlConnection.disconnect();
                        if (!Strings.isNullOrEmpty(passCode)) {
                            LOG.info(String.format("Using {%s} as passCode", passCode));
                            HttpURLConnection shutdownConnection = (HttpURLConnection) requestShutdown(port, passCode).openConnection();
                            LOG.warn(String.format("Shutdown succeeded. Service {%s} will be offline in a few moments", shutdownConnection.getURL().toString()));
                            shutdownConnection.getInputStream();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.warn(
                    String.format("Did the shutdown succeed? Unexpected end of initiation. Is the server even running on port {%s}?", port),
                    e);
        }
    }

    private URL requestShutdown(int port, String passcode) throws MalformedURLException {
        return UriBuilder
                .fromUri(String.format("http://localhost:%s/%s", port, JettyContainer.REST_SERVICE_URI))
                .path(RestAttributes.Route.KILL_BASE)
                .path(RestAttributes.Parameter.ME)
                .path(passcode)
                .build().toURL();
    }

    private URL requestPasscodeUrl(int port) throws MalformedURLException {
        return UriBuilder
                .fromUri(String.format("http://localhost:%s/%s", port, JettyContainer.REST_SERVICE_URI))
                .path(RestAttributes.Route.KILL_BASE)
                .path(RestAttributes.Parameter.PASSCODE)
                .path(RestAttributes.Parameter.SHOW)
                .build().toURL();
    }

}
