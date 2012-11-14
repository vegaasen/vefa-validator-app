package no.difi.vefa.ws;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import no.difi.vefa.properties.PropertiesFile;
import no.difi.vefa.validation.Validate;

@Path("/")
@Stateless
public class Ws 
{
	@Context
	UriInfo uri;		
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_XML})
	public String listVersions() throws Exception {
		Validate validate = new Validate();
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(validate.pathToPropertiesFile);
		
		ListVersions listVersions = new ListVersions();
		listVersions.baseUri = uri.getBaseUri().toString();
		listVersions.propertiesFile = propFile;
		
		return listVersions.getVersions();
	}	

	@GET
	@Path("/{version}")
	@Produces({MediaType.APPLICATION_XML})
	public String listSchemasForCurrentVersion(@PathParam("version") String version) throws Exception {
		Validate validate = new Validate();
		PropertiesFile propFile = new PropertiesFile();
		propFile.main(validate.pathToPropertiesFile);

		ListSchemas listSchemas = new ListSchemas();
		listSchemas.version = version;
		listSchemas.baseUri = uri.getBaseUri().toString();
		listSchemas.propertiesFile = propFile;
		
		return listSchemas.getSchemas();
	}
	
	@POST
	@Path("/{version}")
	@Produces({MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_XML})
	public String validateSchemaAuto(@PathParam("version") String version, String xml) throws Exception {
		Validate validate = new Validate();
		validate.autodetectSchema = true;
		validate.version = version;
		validate.xml = xml;
		validate.main();						
		
		return validate.messagesAsXML();
	}

	@POST
	@Path("/{version}/suppresswarnings")
	@Produces({MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_XML})
	public String validateSchemaAutoFilterWarnings(@PathParam("version") String version, String xml) throws Exception {	
		Validate validate = new Validate();
		validate.autodetectSchema = true;
		validate.version = version;
		validate.xml = xml;
		validate.suppressWarnings = true;
		validate.main();						
		
		return validate.messagesAsXML();
	}
	
	@POST
	@Path("/{version}/{schema}")
	@Produces({MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_XML})
	public String validateSchema(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
		Validate validate = new Validate();
		validate.version = version;
		validate.schema = schema;
		validate.xml = xml;
		validate.main();				
		
		return validate.messagesAsXML();		
	}	
	
	@POST
	@Path("/{version}/{schema}/suppresswarnings")
	@Produces({MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_XML})
	public String validateSchemaFilterWarnings(@PathParam("version") String version, @PathParam("schema") String schema, String xml) throws Exception {
		Validate validate = new Validate();
		validate.version = version;
		validate.schema = schema;
		validate.xml = xml;
		validate.suppressWarnings = true;	
		validate.main();				

		return validate.messagesAsXML();		
	}	
}
