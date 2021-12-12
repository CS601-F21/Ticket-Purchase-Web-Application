package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateEventServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/create_event.html";

    private void insertIntoEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("INSERT"));
        query.setString(1, event.getName());
        query.setInt(2, event.getCreatedBy().getId());
        query.setString(3, event.getDescription());
        query.setInt(4, event.getAvailable());
        query.executeUpdate();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
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
                String eventName = req.getParameter("event-name");
                String description = req.getParameter("description");
                String availableString = req.getParameter("available");
                String message;
                if (!availableString.equals("")) {
                    int available = Integer.parseInt(availableString);
                    if (!eventName.equals("")) {
                        resp.setStatus(HttpStatus.OK_200);
                        insertIntoEventsTable(new Event(0, eventName, user, description, available, 0));
                        message = "<h1>Event successfully created!</h1>";
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        message = "<h1>Event name can not be empty!</h1>";
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    message = "<h1>Number of available tickets can not be empty</h1>";
                }
                Utils.defaultResponse(message, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}