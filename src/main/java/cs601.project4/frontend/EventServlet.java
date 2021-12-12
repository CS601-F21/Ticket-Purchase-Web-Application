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
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet to handle event path
 */
public class EventServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/event.html";

    /**
     * delete the event from the events table
     * @param event the event to delete
     * @throws SQLException
     */
    private void deleteFromEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("DELETE"));
        query.setInt(1, event.getId());
        query.executeUpdate();
    }

    /**
     * update the information fo the event
     * @param event the new event object
     * @throws SQLException
     */
    private void updateEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("UPDATE"));
        query.setString(1, event.getName());
        query.setInt(2, event.getAvailable());
        query.setInt(3, event.getPurchased());
        query.setInt(4, event.getId());
        query.executeUpdate();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                int eventId = Integer.parseInt(req.getPathInfo().substring(1));
                Event event = Utils.getEvent(eventId);
                if (event != null) {
                    resp.setStatus(HttpStatus.OK_200);
                    String html = Utils.readFile(Paths.get(HTMLPATH));
                    resp.getWriter().println(html);
                    if (event.getCreatedBy().getId() == user.getId()) {
                        Utils.eventInfoformContent(resp, event, "/event/" + event.getId(), false);
                    } else {
                        Utils.eventInfoformContent(resp, event, null, true);
                    }
                    resp.getWriter().println(Utils.PAGE_FOOTER);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Utils.defaultResponse("<h1>This event does not exist! </h1>", resp);
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                int eventId = Integer.parseInt(req.getPathInfo().substring(1));
                String message;
                Event event = Utils.getEvent(eventId);
                if (event != null) {
                    String operation = req.getParameter("operation");
                    if (operation.equals("Submit")) {
                        String name = req.getParameter("name");
                        int available = Integer.parseInt(req.getParameter("available"));
                        int purchased = Integer.parseInt(req.getParameter("purchased"));
                        event.setName(name);
                        if (available >= 0) {
                            event.setAvailable(available);
                            if (purchased >= 0) {
                                event.setPurchased(purchased);
                                updateEventsTable(event);
                                resp.setStatus(HttpStatus.OK_200);
                                message = "<h1> Event info successfully updated</h1>";
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                message = "<h1> Number of purchased tickets should be positive or zero</h1>";
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            message = "<h1> Number of available tickets should be positive or zero</h1>";
                        }
                    } else {
                        resp.setStatus(HttpStatus.OK_200);
                        deleteFromEventsTable(event);
                        message = "<h1> Event was successfully deleted!</h1>";
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    message = "<h1> This event does not exist! </h1>";
                }
                Utils.defaultResponse(message, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}