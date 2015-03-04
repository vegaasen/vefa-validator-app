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

@Path(RestAttributes.Route.VALIDATION)
public class ValidationService extends AbstractService {

    @POST
    @Path("/")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public String validateVersionAndSchemaAuto(String xml) throws Exception {
        Validate validate = new Validate();
        validate.autodetectVersionAndIdentifier = true;
        validate.xml = xml;
        validate.validate();

        return validate.messagesAsXML();
    }

    @POST
    @Path("/suppresswarnings")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public String validateVersionAndSchemaAutoFilterWarnings(String xml) throws Exception {
        Validate validate = new Validate();
        validate.autodetectVersionAndIdentifier = true;
        validate.xml = xml;
        validate.suppressWarnings = true;
        validate.validate();

        return validate.messagesAsXML();
    }

    @POST
    @Path("/{version}/{schema}")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public String validateSchema(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.version = version;
        validate.id = schema;
        validate.xml = xml;
        validate.validate();

        return validate.messagesAsXML();
    }

    @POST
    @Path("/{version}/{schema}/suppresswarnings")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public String validateSchemaFilterWarnings(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.version = version;
        validate.id = schema;
        validate.xml = xml;
        validate.suppressWarnings = true;
        validate.validate();

        return validate.messagesAsXML();
    }

    @POST
    @Path("/{version}/{schema}/render")
    @Produces({MediaType.TEXT_HTML})
    @Consumes({MediaType.APPLICATION_XML})
    public String renderSchema(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
        Validate validate = new Validate();
        validate.version = version;
        validate.id = schema;
        validate.xml = xml;
        validate.render();

        if (validate.renderResult != null) {
            return validate.renderResult;
        } else {
            return validate.messagesAsXML();
        }


    }
}
