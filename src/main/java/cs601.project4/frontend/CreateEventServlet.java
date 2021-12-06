package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;


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
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("INSERT"), Statement.RETURN_GENERATED_KEYS);
        query.setString(1, event.getName());
        query.setInt(2, event.getCreatedBy().getId());
        query.setInt(3, event.getAvailable());
        query.executeUpdate();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String eventName = req.getParameter("event-name");
                int available = Integer.parseInt(req.getParameter("available"));
                insertIntoEventsTable(new Event(0, eventName, user, available, 0));
                resp.getWriter().println("<h1> Event Created! </h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home</a>");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}