package no.difi.vefa.ws.rest.service;

import no.difi.vefa.validation.Validate;
import no.difi.vefa.ws.rest.common.RestAttributes;
import no.difi.vefa.ws.rest.service.abs.AbstractService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(RestAttributes.Route.VALIDATION)
public class ValidationService extends AbstractService {

    @POST
    @Path("/")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response validateVersionAndSchemaAuto(String xml) throws Exception {
        Validate validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(true);
        validate.setSource(xml);
        validate.validate();
        return Response.ok().entity(validate.getMessages()).build();
    }

    @POST
    @Path("/suppresswarnings")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response validateVersionAndSchemaAutoFilterWarnings(String xml) throws Exception {
        Validate validate = new Validate();
        validate.setAutodetectVersionAndIdentifier(true);
        validate.setSuppressWarnings(true);
        validate.validate();
        return Response.ok().entity(validate.getMessages()).build();
    }

    @POST
    @Path("/{version}/{schema}")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response validateSchema(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.setVersion(version);
        validate.setId(schema);
        validate.setSource(xml);
        validate.validate();
        return Response.ok().entity(validate.getMessages()).build();
    }

    @POST
    @Path("/{version}/{schema}/suppresswarnings")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response validateSchemaFilterWarnings(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.setVersion(version);
        validate.setId(schema);
        validate.setSource(xml);
        validate.setSuppressWarnings(true);
        validate.validate();
        return Response.ok().entity(validate.getMessages()).build();
    }

    @POST
    @Path("/{version}/{schema}/render")
    @Produces({MediaType.TEXT_HTML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response renderSchema(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.setVersion(version);
        validate.setId(schema);
        validate.setSource(xml);
        validate.render();
        if (validate.getRenderResult() != null) {
            return Response.ok().entity(validate.getRenderResult()).build();
        } else {
            return Response.ok().entity(validate.getMessages()).build();
        }
    }
}
