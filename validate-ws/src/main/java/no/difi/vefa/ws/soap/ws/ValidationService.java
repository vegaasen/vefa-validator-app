package no.difi.vefa.ws.soap.ws;

import com.google.common.base.Strings;
import no.difi.vefa.message.Messages;
import no.difi.vefa.validation.Validate;
import no.difi.vefa.ws.model.ErrorCode;
import no.difi.vefa.ws.model.ResponseError;
import no.difi.vefa.ws.model.exception.ValidationSoapServiceException;
import no.difi.vefa.ws.soap.ws.abs.AbstractWebService;
import no.difi.vefa.ws.soap.ws.common.SoapAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
@WebService(targetNamespace = SoapAttributes.NAMESPACE, name = SoapAttributes.Services.VALIDATION)
@SOAPBinding(
        style = SOAPBinding.Style.DOCUMENT,
        use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
)
public class ValidationService extends AbstractWebService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);
    private static final String PING_RESPONSE = "pong";

    @WebMethod(operationName = SoapAttributes.Operations.PING)
    @WebResult(name = SoapAttributes.Methods.PING_RESPONSE, targetNamespace = SoapAttributes.NAMESPACE)
    public String ping(
            @WebParam(name = SoapAttributes.Parameters.PING, targetNamespace = SoapAttributes.NAMESPACE) @XmlElement(required = false) final String ping
    ) {
        return !Strings.isNullOrEmpty(ping) ? ping : PING_RESPONSE;
    }

    @WebMethod(operationName = SoapAttributes.Operations.VALIDATION)
    @WebResult(name = SoapAttributes.Methods.VALIDATION_RESPONSE, targetNamespace = SoapAttributes.NAMESPACE)
    public Messages validateInvoice(
            @WebParam(name = SoapAttributes.Parameters.INVOICE, targetNamespace = SoapAttributes.NAMESPACE) @XmlElement(required = true) final String invoice,
            @WebParam(name = SoapAttributes.Parameters.SCHEMA, targetNamespace = SoapAttributes.NAMESPACE) @XmlElement(required = false) final String schema,
            @WebParam(name = SoapAttributes.Parameters.VERSION, targetNamespace = SoapAttributes.NAMESPACE) @XmlElement(required = false) final String version,
            @WebParam(name = SoapAttributes.Parameters.SUPPRESS_WARNINGS, targetNamespace = SoapAttributes.NAMESPACE) @XmlElement(required = false) final Boolean suppressWarnings)
            throws ValidationSoapServiceException {
        LOG.info(String.format("Validating invoice {%s} by schema {%s} with version {%s} and suppressingWarnings {%s}", invoice, schema, version, suppressWarnings));
        final Validate validate = new Validate();
        if (!Strings.isNullOrEmpty(schema)) {
            validate.id = schema;
        } else {
            validate.autodetectVersionAndIdentifier = true;
        }
        if (!Strings.isNullOrEmpty(version)) {
            validate.version = version;
        }
        validate.suppressWarnings = suppressWarnings == null ? false : suppressWarnings;
        validate.xml = invoice;
        try {
            validate.validate();
            return validate.getMessages();
        } catch (Exception e) {
            throw new ValidationSoapServiceException(ResponseError.create(ErrorCode.NOT_FOUND, String.format("Unable to validate invoice {%s}", invoice)), e);
        }
    }

}
