package no.difi.vefa.ws.run.container;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import no.difi.vefa.ws.soap.ws.ValidationService;
import no.difi.vefa.ws.soap.ws.abs.AbstractWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;

/**
 * This controls the application server itself. It exposes two key methods ; stop() + start().
 *
 * @author <a href="mailto:vegard.aasen1@kongsberg.com">vegaraa</a>
 */
public enum JettyContainer {

    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(JettyContainer.class);
    private static final String SLASH = "/";
    private static final String CONTEXT_PATH = SLASH;
    private static final String PATH_SPEC = "/*";
    private static final String ENDPOINT_URI = "validationService", WEB_SERVICE_URI = "services", REST_SERVICE_URI = "rest";
    private static final String WS_PATH_SPEC = "/" + WEB_SERVICE_URI + "/*";
    private static final String REST_PATH_SPEC = "/" + REST_SERVICE_URI + "/*";

    private Server webServer;
    private Bus bus;

    public void start(int port) {
        try {
            if (port <= 0) {
                port = ContainerDefaults.DEFAULT_PORT;
            }
            webServer = new Server();
            webServer.setConnectors(assembleConnectors(port, webServer));
            final ServletContextHandler applicationContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
            applicationContext.setContextPath(CONTEXT_PATH);
            applicationContext.setSessionHandler(new SessionHandler());
            configureJersey(applicationContext);
            configureCxf(applicationContext);
            webServer.setHandler(applicationContext);
            webServer.start();
            LOG.info(String.format("Server now running on http://localhost:%s", port));
            LOG.info(String.format("Access SOAP-services on http://localhost:%s/%s", port, WEB_SERVICE_URI));
            LOG.info(String.format("Access REST-services on anywhereBut(http://localhost:%s/%s/*)", port, WEB_SERVICE_URI));
        } catch (Exception e) {
            LOG.error("Unable to start webserver", e);
            stop();
            LOG.error("The webServer was not started.");
        }
    }

    public void stop() {
        if (webServer != null) {
            if (!webServer.isRunning()) {
                return;
            }
            try {
                while (!webServer.isStopped()) {
                    webServer.stop();
                }
            } catch (Exception e) {
                LOG.error("Unable to stop the running server.", e);
            }
        }
    }

    protected Connector[] assembleConnectors(final int port, final Server server) {
        if (port == 0) {
            throw new IllegalArgumentException("The arguments is null.");
        }
        final List<Connector> connectors = new ArrayList<>();
        final ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(port);
        connectors.add(httpConnector);
        if (connectors.isEmpty()) {
            throw new RuntimeException("No controllers defined, even though they were expected to be.");
        }
        return connectors.toArray(new Connector[connectors.size()]);
    }

    protected void configureCxf(final ServletContextHandler applicationContext) {
        System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME, CXFBusFactory.class.getName());
        bus = BusFactory.getDefaultBus(true);
        final CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(bus);
        final ServletHolder cxfServletHolder = new ServletHolder(cxfServlet);
        cxfServletHolder.setName(WEB_SERVICE_URI);
        cxfServletHolder.setForcedPath(WEB_SERVICE_URI);
        applicationContext.addServlet(cxfServletHolder, WS_PATH_SPEC);
        LOG.info("Found request listners. Adding to the context.");
        BusFactory.setDefaultBus(bus);
        initializeSoapServices();
    }

    protected void configureJersey(final ServletContextHandler applicationContext) {
        final ServletHolder jerseyServletHolder = new ServletHolder(new ServletContainer());
        jerseyServletHolder.setInitParameter(
                "com.sun.jersey.config.property.packages",
                "no.difi.vefa.ws.rest");
        applicationContext.addServlet(jerseyServletHolder, REST_PATH_SPEC);
    }

    private void initializeSoapServices() {
        final ValidationService validationService = new ValidationService();
        publishService(validationService);
    }

    private void publishService(final AbstractWebService service) {
        Endpoint.publish(String.format("%s%s", SLASH, ENDPOINT_URI), service);
    }

}
