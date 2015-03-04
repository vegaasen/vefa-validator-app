package no.difi.vefa.ws.rest.service.abs;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public abstract class AbstractService {

    @Context
    protected UriInfo uri;

}
