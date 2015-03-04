package no.difi.vefa.ws.rest.common;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public final class RestAttributes {

    private static final String L_MUSTACHE = "{", R_MUSTACHE = "}";

    public static final class Route {
        public static final String
                VALIDATION = "validation",
                VERSION = "version";
    }

    public static final class Parameter {
        public static final String VERSION = "version";
        public static final String VERSION_PARAM = L_MUSTACHE + VERSION + R_MUSTACHE;

    }

    private RestAttributes() {
    }

}
