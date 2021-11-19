package cs601.project4.frontend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class FrontEndDriver {
    public static void main(String args[]) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);
        handler.addServlet(ListEventsServelet.class, "/list-events");

        server.start();
        server.join();
    }
}