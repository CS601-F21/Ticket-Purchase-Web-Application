package cs601.project4.frontend.login;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import cs601.project4.frontend.login.utilities.Config;

import java.io.FileReader;

/**
 * A server to use Jetty to implement login with Slack functionality.
 *
 * To run this example you'll need a publicly-accessible redirect URL.
 * I used ngrok for this purpose: https://ngrok.com/
 * For the free version, each time you restart ngrok you'll get a new URL.
 * You need to configure that URL for your Slack app and make sure to
 * specify it in your config file.
 * For sessions to work correctly, you also must use that URL when
 * testing your program locally. DO NOT USE LOCALHOST!
 *
 */
public class LoginServer {

    public static final int PORT = 8080;
    private static final String configFilename = "config.json";

    public static void main(String[] args) {
        try {
            startup();
        } catch(Exception e) {
            // catch generic Exception as that is what is thrown by server start method
            e.printStackTrace();
        }
    }

    /**
     * A helper method to start the server.
     * @throws Exception -- generic Exception thrown by server start method
     */
    public static void startup() throws Exception {

        // read the client id and secret from a config file
        Gson gson = new Gson();
        Config config = gson.fromJson(new FileReader(configFilename), Config.class);

        // create a new server
        Server server = new Server(PORT);

        // make the config information available across servlets by setting an
        // attribute in the context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute(LoginServerConstants.CONFIG_KEY, config);

        // the default path will direct to a landing page with
        // "Login with Slack" button
        context.addServlet(LandingServlet.class, "/");

        // Once authenticated, Slack will redirect the user
        // back to /login
        context.addServlet(LoginServlet.class, "/login");

        // handle logout
        context.addServlet(LogoutServlet.class, "/logout");

        // start it up!
        server.setHandler(context);
        server.start();
    }
}
