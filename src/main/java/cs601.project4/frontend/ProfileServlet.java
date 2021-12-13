/**
 * Author: Firoozeh Kaveh
 */
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

/**
 * Servlet to handle profile path
 */
public class ProfileServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/profile.html";

    /**
     * update the users table based on the new info
     * @param user the new user object
     * @throws SQLException
     */
    private void updateUsersTable(User user) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("UPDATE"));
        query.setString(1, user.getName());
        query.setString(2, user.getEmail());
        query.setInt(3, user.getId());
        query.executeUpdate();
    }

    /**
     * Forming the profile info form that is editable
     * @param req
     * @param resp
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String name = user.getName();
                String email = user.getEmail();
                resp.setStatus(HttpStatus.OK_200);
                String html = Utils.readFile(Paths.get(HTMLPATH));
                resp.getWriter().println(html);
                resp.getWriter().println("<h2>Modify Your Profile Info </h2><br>");
                Utils.userInfoformContent(resp, name, email, "/profile");
                resp.getWriter().println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            Utils.internalError(resp);
        }
    }

    /**
     * After the new profile info is submitted, this is called to update the info
     * @param req
     * @param resp
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //* if the user is already logged in, then proceed otherwise redirect to landing page
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                user = new User(name, email, user.getId());
                //* store the updated user object in the session
                req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, user);
                //* update the users table with the new info
                updateUsersTable(user);
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                String html = Utils.readFile(Paths.get(HTMLPATH));
                writer.println(html);
                //* show a success message and still let the user update the info
                writer.println("<h2> The info was successfully updated!</h2>");
                Utils.userInfoformContent(resp, name, email, "/profile");
                writer.println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            Utils.internalError(resp);
        }
    }
}