package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ProfileServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/profile.html";

    private void updateUsersTable(User user) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("UPDATE"));
        query.setString(1, user.getName());
        query.setString(2, user.getEmail());
        query.setInt(3, user.getId());
        query.executeUpdate();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String name = user.getName();
                String email = user.getEmail();
                resp.setStatus(HttpStatus.OK_200);
                String html = Utils.readFile(Paths.get(HTMLPATH));
                resp.getWriter().println(html);
                Utils.userInfoformContent(resp, name, email, "/profile");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                user = new User(name, email);
                req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, user);
                updateUsersTable(user);
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                writer.println(LoginServerConstants.PAGE_HEADER);
                writer.println("<h2> The info was successfully updated!</h2>");
                Utils.userInfoformContent(resp, name, email, "/profile");
                writer.println(LoginServerConstants.PAGE_FOOTER);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}