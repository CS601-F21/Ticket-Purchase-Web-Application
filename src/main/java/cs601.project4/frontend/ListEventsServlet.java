package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ListEventsServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/list_events.html";

    private String queryAllEvents() throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(
                SQLQueries.eventQueries.get("SELECT_ALL_WITH_USERS"));
        ResultSet resultSet = query.executeQuery();
        StringBuilder eventsHTML = new StringBuilder();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String eventName = resultSet.getString("event_name");
            String userName = resultSet.getString("user_name");
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            User user = new User(userName, null);
            Event event = new Event(id, eventName, user, available, purchased);
            eventsHTML.append(event.toHTML());
        }
        return eventsHTML.toString();

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                PrintWriter writer = resp.getWriter();
                writer.println(Utils.readFile(Paths.get(HTMLPATH)));
                writer.println(queryAllEvents());
                writer.println("</table>");
                writer.println(LoginServerConstants.PAGE_FOOTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}