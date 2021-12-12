/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.objs.Event;
import cs601.project4.objs.Transaction;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet to handle purchase path
 */
public class PurchaseTicketServlet extends HttpServlet {
    /**
     * adjust the number of tickets when a purchase happens
     * @param event the event to adjust the number of tickets for
     * @throws SQLException
     */
    private void updateEventsTable(Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("UPDATE_TICKETS"));
        query.setInt(1, event.getAvailable() - 1);
        query.setInt(2, event.getPurchased() + 1);
        query.setInt(3, event.getId());
        query.executeUpdate();
    }

    /**
     * adds the ticket to the user_tickets table
     * @param user user that has the tickets
     * @param event the event for the ticket
     * @throws SQLException
     */
    private void insertIntoUserTicketsTable(User user, Event event) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userTicketsQueries.get("INSERT"));
        query.setInt(1, user.getId());
        query.setInt(2, event.getId());
        query.executeUpdate();
    }

    /**
     * adds the purchase transaction to the transactions table
     * @param transaction the transaction to add to the table
     * @throws SQLException
     */
    private void insertIntoTransactionsTable(Transaction transaction) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.transactionQueries.get("PURCHASE_TICKET"));
        query.setInt(1, transaction.getUser().getId());
        query.setInt(2, transaction.getEvent().getId());
        query.executeUpdate();
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                int eventId = Integer.parseInt(req.getPathInfo().substring(1));
                Event event = Utils.getEvent(eventId);
                if (event != null && event.getAvailable() > 0) {
                    updateEventsTable(event);
                    Transaction transaction = new Transaction(0, event, user, "purchase", null);
                    insertIntoTransactionsTable(transaction);
                    insertIntoUserTicketsTable(user, event);
                    resp.setStatus(HttpStatus.OK_200);
                    Utils.defaultResponse("<h1> Ticket purchase was successful! </h1>", resp);
                } else if (event == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Utils.defaultResponse("<h1> Event does not exist! </h1>", resp);
                } else {
                    Utils.defaultResponse("<h1> There are no available tickets for the event! </h1>", resp);
                }
            }
        } catch (Exception e) {
            Utils.internalError(resp);
            e.printStackTrace();
        }
    }
}