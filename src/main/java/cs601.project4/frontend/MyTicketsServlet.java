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

/**
 * Servlet to handle my tickets path
 */
public class MyTicketsServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/my_tickets.html";

    /**
     * find all the tickets for a user
     * @param userId the id of the user to find the users for
     * @return the html formatted version of the found events
     * @throws SQLException
     */
    private String queryAllTickets(int userId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userTicketsQueries.get("SELECT"));
        query.setInt(1, userId);
        ResultSet resultSet = query.executeQuery();
        StringBuilder eventsHTML = new StringBuilder();

        while (resultSet.next()) {
            int id = resultSet.getInt("event_id");
            String eventName = resultSet.getString("event_name");
            Event event = new Event(id, eventName);
            eventsHTML.append(event.toHTML("my-tickets"));
        }
        return eventsHTML.toString();

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                writer.println(Utils.readFile(Paths.get(HTMLPATH)));
                writer.println(queryAllTickets(user.getId()));
                writer.println("</table>");
                writer.println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}