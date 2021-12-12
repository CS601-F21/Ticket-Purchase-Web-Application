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


public class EventServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/event.html";

    private void deleteFromEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("DELETE"));
        query.setInt(1, event.getId());
        query.executeUpdate();
    }

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

    private User queryUsersTable(int userId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("SELECT_BY_ID"));
        query.setInt(1, userId);
        ResultSet resultSet = query.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            return new User(name, email, id);
        }
        else {
            return null;
        }
    }

    private Event getEvent(int eventId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("SELECT"));
        query.setInt(1, eventId);
        ResultSet resultSet = query.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String eventName = resultSet.getString("event_name");
            String description = resultSet.getString("description");
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            User user = queryUsersTable(resultSet.getInt("created_by"));
            return new Event(id, eventName, user, description, available, purchased);
        }
        return null;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                int eventId = Integer.parseInt(req.getPathInfo().substring(1));
                Event event = getEvent(eventId);
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
                Event event = getEvent(eventId);
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