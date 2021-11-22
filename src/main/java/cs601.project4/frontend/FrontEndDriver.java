package cs601.project4.frontend;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import java.nio.file.Paths;

public class FrontEndDriver {


    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        FrontEndConfig config = gson.fromJson(Utils.readFile(Paths.get(args[0])), FrontEndConfig.class);
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);
        handler.addServlet(ListEventsServelet.class, "/list-events");
        handler.addServlet(new ServletHolder(new SigninServlet(config.getGoogleClientId(), config.getRedirectURI())), "/login");
        handler.addServlet(new ServletHolder(new RedirectServlet(config.getGoogleClientId(), config.getRedirectURI(), config.getGoogleClientSecret())), "/home");

        server.start();
        server.join();
    }

}