package no.difi.vefa.ws.run.container;

/**
 * _what_
 *
 * @author <a href="mailto:vegard.aasen1@kongsberg.com">vegaraa</a>
 */
public final class ContainerDefaults {

    public static final String[] JETTY_USER_ROLES = new String[]{"user"};
    public static final String[] JETTY_PROTOCOLS = new String[]{"http/1.1"};
    public static final String DEFAULT_PATH = "/";
    public static final String DEFAULT_CONTEXT_PATH = DEFAULT_PATH + "jetty";
    public static final String DEFAULT_WEBAPP_RESOURCE = "/webapp/";
    public static final char SERVLET_MATCHER = '*';
    public static final int DEFAULT_PORT = 7000;
    public static final int DEFAULT_CONTROL_PORT = DEFAULT_PORT + 1;
    public static final int DEFAULT_HTTPS_PORT = DEFAULT_PORT + 443;

    private ContainerDefaults() {
    }

}

