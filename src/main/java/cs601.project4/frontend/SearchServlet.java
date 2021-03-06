/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet to handle search path
 */
public class SearchServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/search.html";
    private static final String HTMLPATH_RESULT = "resources/search_results.html";

    /**
     * search the events table for some info
     * @param phrase the phrase in the event name or description
     * @param createdBy the user who created the event
     * @param availableString the string version of the number of available tickets
     * @param purchasedString the string version of the number of purchased tickets
     * @return html formatted version of the found events
     * @throws SQLException
     */
    private String search(String phrase, String createdBy, String availableString, String purchasedString) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        String phraseSQL = "%" + phrase + "%";
        String createdBySQL = "%" + createdBy + "%";
        String availableSQL = "%";
        String purchasedSQL = "%";
        if (!availableString.equals("")) {
            availableSQL = availableString;
        }
        if (!purchasedString.equals("")) {
            purchasedSQL = purchasedString;
        }
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("SEARCH"));
        query.setString(1, phraseSQL);
        query.setString(2, phraseSQL);
        query.setString(3, createdBySQL);
        query.setString(4, availableSQL);
        query.setString(5, purchasedSQL);
        ResultSet resultSet = query.executeQuery();
        StringBuilder eventsHTML = new StringBuilder();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String eventName = resultSet.getString("event_name");
            String userName = resultSet.getString("user_name");
            String description = resultSet.getString("description");
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            User user = new User(userName, null);
            Event event = new Event(id, eventName, user, description, available, purchased);
            eventsHTML.append(event.toHTML("list-events"));
        }
        return eventsHTML.toString();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String phrase = req.getParameter("phrase");
                String createdBy = req.getParameter("created-by");
                String availableString = req.getParameter("available");
                String purchasedString = req.getParameter("purchased");
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                writer.println(Utils.readFile(Paths.get(HTMLPATH_RESULT)));
                writer.println(search(phrase, createdBy, availableString, purchasedString));
                writer.println("</table>");
                writer.println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}