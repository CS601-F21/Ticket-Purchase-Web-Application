package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                SQLQueries.eventQueries.get("SELECT_ALL_WITH_USERS"), Statement.RETURN_GENERATED_KEYS);
        ResultSet resultSet = query.executeQuery();
        StringBuilder eventsHTML = new StringBuilder();

        while (resultSet.next()) {
            String eventName = resultSet.getString("event_name");
            String userName = resultSet.getString("user_name");
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            User user = new User(userName, null);
            Event event = new Event(0, eventName, user, available, purchased);
            eventsHTML.append(event.toHTML("list-events"));
        }
        return eventsHTML.toString();

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
            resp.getWriter().println(queryAllEvents());
            resp.getWriter().println("</table> </div>\n" +
                    "</body>\n" +
                    "</html>");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}