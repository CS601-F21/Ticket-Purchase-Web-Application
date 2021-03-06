/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.Transaction;
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
 * Servlet to handle transfer path
 */
public class TransferServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/transfer.html";

    /**
     * check whether the user has the ticket
     * @param event the event object
     * @param user the user object
     * @return whether the user has the ticket
     * @throws SQLException
     */
    private boolean checkUserHasTicket(Event event, User user) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userTicketsQueries.get("HAS_TICKET"));
        query.setInt(1, event.getId());
        query.setInt(2, user.getId());
        ResultSet resultSet = query.executeQuery();
        return resultSet.next();
    }

    /**
     * updates the user_tickets table to remove the ticket from the initiating user and add it to the receiver user
     * @param user the user that initiated the transfer
     * @param otherUser the user to receive the ticket
     * @param eventId the id of the event to transfer
     * @throws SQLException
     */
    private void updateUserTicketsTable(User user, User otherUser, int eventId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userTicketsQueries.get("INSERT"));
        query.setInt(1, otherUser.getId());
        query.setInt(2, eventId);
        query.executeUpdate();
        query = dbManager.getConnection().prepareStatement(SQLQueries.userTicketsQueries.get("DELETE"));
        query.setInt(1, user.getId());
        query.setInt(2, eventId);
        query.executeUpdate();
    }

    /**
     * register the transfer transaction into the transactions table
     * @param transaction the transaction to register
     * @throws SQLException
     */
    private void insertIntoTransactionsTable(Transaction transaction) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.transactionQueries.get("TRANSFER_TICKET"));
        query.setInt(1, transaction.getUser().getId());
        query.setInt(2, transaction.getEvent().getId());
        query.setInt(3, transaction.getOtherUser().getId());
        query.executeUpdate();
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            int eventId = Integer.parseInt(req.getPathInfo().substring(1));
            if (user != null) {
                resp.setStatus(HttpStatus.OK_200);
                String html = Utils.readFile(Paths.get(HTMLPATH));
                resp.getWriter().println(html);
                resp.getWriter().println("<h2> Please enter user email</h2>");
                Utils.userInfoformContent(resp, null, "", "/transfer/" + eventId);
                resp.getWriter().println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            Utils.internalError(resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                String email = req.getParameter("email");
                int eventId = Integer.parseInt(req.getPathInfo().substring(1));
                Event event = new Event(eventId, null);
                if (!checkUserHasTicket(event, user)) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Utils.defaultResponse("<h1> You don't have this ticket! </h1>", resp);
                    return;
                }
                User otherUser = Utils.queryUsersTable(email, 0);
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                writer.println(Utils.PAGE_HEADER);
                //* if all info is correct, update the user_tickets table and the transactions table
                if (otherUser != null) {
                    updateUserTicketsTable(user, otherUser, eventId);
                    Transaction transaction = new Transaction(0, event, user, "transfer", otherUser);
                    insertIntoTransactionsTable(transaction);
                    Utils.defaultResponse("<h1> Ticket transfer was successful! </h1>", resp);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Utils.defaultResponse("<h1> User with email " + email + " does not exist </h1>", resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}