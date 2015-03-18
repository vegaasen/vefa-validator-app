package no.difi.vefa.ws.rest.service;

import no.difi.vefa.ws.rest.service.abs.AbstractService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
@Path("/")
public class DefaultService extends AbstractService {

    @GET
    public Response welcome() {
        return Response.ok().entity("welcome").build();
    }


}
