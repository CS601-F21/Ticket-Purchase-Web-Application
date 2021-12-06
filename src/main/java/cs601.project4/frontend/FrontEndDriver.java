package cs601.project4.frontend;

import com.google.gson.Gson;
import cs601.project4.frontend.login.LandingServlet;
import cs601.project4.frontend.login.LoginServlet;
import cs601.project4.frontend.login.LogoutServlet;
import cs601.project4.frontend.login.utilities.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;



import java.nio.file.Paths;

public class FrontEndDriver {


    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        Config config = gson.fromJson(Utils.readFile(Paths.get(args[0])), Config.class);
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setAttribute(ServerConstants.CONFIG_KEY, config);
        server.setHandler(handler);
        handler.addServlet(LandingServlet.class, "/");
        handler.addServlet(HomeServlet.class, "/home");
        handler.addServlet(LoginServlet.class, "/login");
        handler.addServlet(LogoutServlet.class, "/logout");
        handler.addServlet(ProfileServlet.class, "/profile");
        handler.addServlet(ListEventsServlet.class, "/list-events");
        handler.addServlet(PurchaseTicketServlet.class, "/purchase/*");
//        handler.addServlet(EventServlet.class, "/event/*");
//        handler.addServlet(EditEventServlet.class, "/edit-event/*");
        handler.addServlet(CreateEventServlet.class, "/create-event");
        handler.addServlet(ViewTransactionsServlet.class, "/view-transactions");

        server.start();
        server.join();
    }

}