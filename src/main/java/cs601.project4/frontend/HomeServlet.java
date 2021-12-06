package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;


public class HomeServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/home.html";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}