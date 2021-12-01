package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.Transaction;
import cs601.project4.objs.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PurchaseTicketServlet extends HttpServlet {

    private void updateEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("UPDATE"));
        query.setInt(1, event.getAvailable() - 1);
        query.setInt(2, event.getPurchased() + 1);
        query.executeUpdate();
    }

    private void insertIntoTransactionsTable(Transaction transaction) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.transactionQueries.get("PURCHASE_TICKET"));
        query.setInt(1, transaction.getUser().getId());
        query.setInt(2, transaction.getEvent().getId());
        query.executeUpdate();
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
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            return new Event(id, eventName, null, available, purchased);
        }
        return null;
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            int eventId = Integer.parseInt(req.getPathInfo().substring(1));
            Event event = getEvent(eventId);
            if (event != null && event.getAvailable() > 0) {
                updateEventsTable(event);
                Transaction transaction = new Transaction(0, event, user, "purchase", null);
                insertIntoTransactionsTable(transaction);
                resp.getWriter().println("<h1> Ticket purchase was successful! </h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home</a>");
            } else if (event == null) {
                resp.getWriter().println("<h1> Event does not exist! </h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home</a>");
            } else {
                resp.getWriter().println("<h1> There are no available tickets for the event! </h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home</a>");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}