package no.difi.vefa.ws.model;

import com.google.common.base.Verify;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Representation of responseErrors
 *
 * @author <a href="mailto:vegard.aasen1@kongsberg.com">vegaraa</a>
 */
@XmlRootElement(name = "responseError")
public class ResponseError {

    private static final String EMPTY = "";
    private static final int DEFAULT_UNDEFINED = Integer.MAX_VALUE;

    private final int code;
    private final String message;
    private final String description;
    private final String userSessionId;
    private final boolean severe;

    /**
     * Used by JAX-RS
     */
    @SuppressWarnings("unused")
    public ResponseError() {
        code = DEFAULT_UNDEFINED;
        message = EMPTY;
        description = EMPTY;
        severe = false;
        userSessionId = EMPTY;
    }

    private ResponseError(int code, String message, String description, boolean severe, String userSessionId) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.severe = severe;
        this.userSessionId = userSessionId;
    }

    public static ResponseError create(String message) {
        return create(DEFAULT_UNDEFINED, message, EMPTY, false, EMPTY);
    }

    public static ResponseError create(int code, String message) {
        return create(code, message, EMPTY, false, EMPTY);
    }

    public static ResponseError create(int code, String message, String description, boolean severe) {
        return create(code, message, description, severe, EMPTY);
    }

    public static ResponseError create(int code, String message, String description, boolean severe, final String userSessionId) {
        Verify.verifyNotNull(message, "The message cannot be nilled");
        Verify.verify(code > 0, String.format("ErrorCode {%s}, is not > 0", code));
        return new ResponseError(code, message, description, severe, userSessionId);
    }

    @XmlAttribute(name = "generated", required = true)
    public Date getGenerated() {
        return new Date();
    }

    @XmlAttribute(name = "referenceId", required = true)
    public String getUserSessionId() {
        return userSessionId;
    }

    @XmlElement
    public int getCode() {
        return code;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    @XmlElement
    public boolean isSevere() {
        return severe;
    }

}
