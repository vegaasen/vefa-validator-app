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
        public static final String
                KILL_BASE = "kill",
                KILL = Parameter.ME + "/{" + Parameter.PASSCODE + "}",
                KILL_WORD = Parameter.PASSCODE + "/{" + Parameter.SHOW + "}";
    }

    public static final class Parameter {
        public static final String VERSION = "version";
        public static final String SHOW = "show";
        public static final String PASSCODE = "passcode";
        public static final String ME = "me";
        public static final String VERSION_PARAM = L_MUSTACHE + VERSION + R_MUSTACHE;

    }

    private RestAttributes() {
    }

}
