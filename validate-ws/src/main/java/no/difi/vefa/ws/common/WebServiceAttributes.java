package no.difi.vefa.ws.common;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public final class WebServiceAttributes {
    
    public static final String NAMESPACE = "urn:no:difi:vefa:soap:soap.validation";

    public static final class Operations {
        public static final String VALIDATION = "validate";
        public static final String PING = "ping";
    }

    public static final class Methods {
        public static final String VALIDATION_RESPONSE = "validateInvoiceResponse";
        public static final String PING_RESPONSE = "pingResponse";
    }

    public static final class Parameters {
        public static final String INVOICE = "invoice";
        public static final String SCHEMA = "schema";
        public static final String SUPPRESS_WARNINGS = "suppressWarnings";
        public static final String VERSION = "version";
        public static final String PING = "ping";
    }

    public static final class Services {
        public static final String VALIDATION = "validationService";
    }

    private WebServiceAttributes() {
    }

}
