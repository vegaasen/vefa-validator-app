package no.difi.vefa.ws.model.exception;

import no.difi.vefa.ws.model.ResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.SOAPException;

/**
 * @author <a href="mailto:vegard.aasen1@kongsberg.com">vegaraa</a>
 */
public final class ValidationSoapServiceException extends SOAPException {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationSoapServiceException.class);

    private ResponseError responseError;

    public ValidationSoapServiceException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ValidationSoapServiceException(ResponseError responseError, Throwable cause) {
        super(cause);
        LOG.warn("Unable to perform serviceRequest", cause);
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
