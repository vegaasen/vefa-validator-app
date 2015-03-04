package no.difi.vefa.ws.rest.service;

import no.difi.vefa.util.PropertiesUtils;
import no.difi.vefa.validation.Validate;
import no.difi.vefa.ws.rest.common.RestAttributes;
import no.difi.vefa.ws.rest.model.ListIdentifier;
import no.difi.vefa.ws.rest.model.ListVersions;
import no.difi.vefa.ws.rest.service.abs.AbstractService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(RestAttributes.Route.VERSION)
public class VersionService extends AbstractService {

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public String listAllVersions() throws Exception {
        Validate validate = new Validate();
        PropertiesUtils propFile = new PropertiesUtils();
        propFile.main(validate.pathToPropertiesFile);

        ListVersions listVersions = new ListVersions();
        listVersions.baseUri = uri.getBaseUri().toString();
        listVersions.propertiesUtils = propFile;

        return listVersions.getVersions();
    }

    @GET
    @Path(RestAttributes.Parameter.VERSION_PARAM)
    @Produces({MediaType.APPLICATION_XML})
    public String listSchemasForCurrentVersion(
            @PathParam(RestAttributes.Parameter.VERSION) final String version
    ) throws Exception {
        Validate validate = new Validate();
        PropertiesUtils propFile = new PropertiesUtils();
        propFile.main(validate.pathToPropertiesFile);

        ListIdentifier listSchemas = new ListIdentifier();
        listSchemas.version = version;
        listSchemas.baseUri = uri.getBaseUri().toString();
        listSchemas.propertiesUtils = propFile;

        return listSchemas.getIdentifier();
    }

}
