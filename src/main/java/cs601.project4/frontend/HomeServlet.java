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
            Object UserObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (UserObj == null) {
                resp.sendRedirect("/");
                return;
            }
            User User = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            String name = User.getName();
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}