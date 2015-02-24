package no.difi.vefa.soap.run.container;

import no.difi.vefa.soap.ws.ValidationService;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public enum JettyContainer {

    INSTANCE;

    private static final int DEFAULT_PORT = 7001;
    private static final int DEFAULT_MAX_IDLE_TIME = (int) TimeUnit.HOURS.toMillis(5);
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final String WEB_APP_ROOT_CONTEXT = "/";
    private static final String WEB_APP_ROOT_CONTEXT_MAPPER = WEB_APP_ROOT_CONTEXT + '*';
    private static final String UTF_8 = "UTF-8";
    private static final String SOAP = WEB_APP_ROOT_CONTEXT + "soap";

    private Server webAppServer;
    private Bus bus;

    public void start(final int port) {
        webAppServer = new Server();
        webAppServer.addConnector(initiateConnectors(webAppServer, (port <= 0) ? DEFAULT_PORT : port));
        removeJettyServerHeaders(webAppServer);
        try {
            webAppServer.setHandler(configuration());
            webAppServer.start();
            ValidationService implementor = new ValidationService();
            BusFactory.setDefaultBus(bus);
            Endpoint.publish(SOAP + "/validation", implementor);
            webAppServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebAppContext configuration() throws IOException {
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        WebAppContext webAppContext = new WebAppContext();
        configureInitParams(webAppContext);
        webAppContext.setClassLoader(new WebAppClassLoader(webAppContext));
        webAppContext.setParentLoaderPriority(false);
        webAppContext.setBaseResource(Resource.newClassPathResource("/webapp", true, false));
        webAppContext.setContextPath(WEB_APP_ROOT_CONTEXT);
        final CXFNonSpringServlet servlet = new CXFNonSpringServlet();
        bus = servlet.getBus();
        ServletHolder holder = new ServletHolder(servlet);
        holder.setName("soap");
        holder.setForcedPath("soap");
        webAppContext.addServlet(holder, SOAP);
        return webAppContext;
    }

    private static void configureInitParams(final WebAppContext context) {
        context.setInitParameter("org.eclipse.jetty.server.Request.queryEncoding", UTF_8);
        context.setInitParameter("org.eclipse.jetty.util.URI.charset", UTF_8);
    }

    private static ServerConnector initiateConnectors(final Server webAppServer, final int port) {
        ServerConnector connector = new ServerConnector(webAppServer);
        connector.setPort(port);
        connector.setHost(DEFAULT_HOST);
        connector.setIdleTimeout(DEFAULT_MAX_IDLE_TIME);
        return connector;
    }

    protected final void removeJettyServerHeaders(final Server server) {
        for (final Connector connector : server.getConnectors()) {
            for (final ConnectionFactory connectionFactory : connector.getConnectionFactories()) {
                if (connectionFactory instanceof HttpConnectionFactory) {
                    ((HttpConnectionFactory) connectionFactory).getHttpConfiguration().setSendServerVersion(false);
                    ((HttpConnectionFactory) connectionFactory).getHttpConfiguration().setSendXPoweredBy(false);
                }
            }
        }
    }

}
