package no.difi.vefa.ws.rest.service;

import com.google.common.base.Strings;
import no.difi.vefa.ws.rest.common.ApplicationContext;
import no.difi.vefa.ws.rest.common.RestAttributes;
import no.difi.vefa.ws.rest.service.abs.AbstractService;
import no.difi.vefa.ws.run.container.JettyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * This service is used in regards to issue kill-requests to the running application server, nothing more.
 * todo: the service dies before it actually is able to succeed streaming the result back to the client. Add some kind of delay?
 *
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
@Path(RestAttributes.Route.KILL_BASE)
public class KillService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(KillService.class);

    @GET
    @Path(RestAttributes.Route.KILL_WORD)
    public Response getKillPassphrase(
            @PathParam(RestAttributes.Parameter.SHOW) final String show
    ) {
        if (Strings.isNullOrEmpty(show)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Missing required attribute").build();
        }
        try {
            if (show.equals(RestAttributes.Parameter.SHOW)) {
                return Response.ok().entity(ApplicationContext.INSTANCE.getKillWord()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Wrong passcode request").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(RestAttributes.Route.KILL)
    public Response kill(
            @PathParam(RestAttributes.Parameter.PASSCODE) final String passcode
    ) {
        if (Strings.isNullOrEmpty(passcode)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Unable to trigger shutdown. No passcode supplied").build();
        }
        if (passcode.equals(ApplicationContext.INSTANCE.getKillWord())) {
            LOG.warn("********** Shutdown initiating now ************");
            new Thread(JettyContainer.INSTANCE::stop).run();
            return Response.ok().entity(ApplicationContext.INSTANCE.getKillWord()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(String.format("{%s} is not a valid passcode", passcode)).build();
    }

}
