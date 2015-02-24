package no.difi.vefa.soap.ws;

import no.difi.vefa.soap.common.WebServiceAttributes;
import no.difi.vefa.soap.model.ValidationRequest;
import no.difi.vefa.soap.model.ValidationResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
@WebService(
        name = WebServiceAttributes.Services.VALIDATION,
        targetNamespace = WebServiceAttributes.NAMESPACE)
public class ValidationService {

    @WebMethod(operationName = "validate")
    @WebResult(
            name = "validationResponse",
            targetNamespace = WebServiceAttributes.NAMESPACE
    )
    public ValidationResponse validate(
            @WebParam(name = "validationRequest",
                    mode = WebParam.Mode.IN,
                    targetNamespace = WebServiceAttributes.NAMESPACE
            ) final ValidationRequest validationRequest) {
        System.out.println("Heisann");
        return null;
    }

}
