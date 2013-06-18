package jp.ddo.chiroru.cellar.util;

import java.util.ResourceBundle;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.ExternalResource;

public class ServletContainerResource
        extends ExternalResource {

    private static String home;
    private static String descriptor;
    private static String contextPath;
    private static int port;

    private Server server;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("jetty");
        home = bundle.getString("jetty.home");
        descriptor = bundle.getString("jetty.descriptor");
        contextPath = bundle.getString("jetty.contextPath");
        port = Integer.valueOf(bundle.getString("jetty.port"));
    }

    /* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected void before() throws Throwable {
        server = new Server(port);
        WebAppContext webapp = new WebAppContext();
        webapp.setDescriptor(descriptor);
        webapp.setContextPath(contextPath);
        webapp.setResourceBase(home);
        server.setHandler(webapp);
        server.start();
    }

    @Override
    protected void after() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        ServletContainerResource r = new ServletContainerResource();
        r.before();
        Thread.sleep(5000);
        r.after();
    }
}
