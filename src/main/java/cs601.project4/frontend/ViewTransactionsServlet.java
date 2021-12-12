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
import java.sql.Statement;

/**
 * Servlet to handle view transactions path
 */
public class ViewTransactionsServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/view_transactions.html";

    /**
     * Query the transactions of the user
     * @param user the user to find the transactions for
     * @return a html formatted version of transactions
     * @throws SQLException
     */
    private String queryAllTransactions(User user) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(
                SQLQueries.transactionQueries.get("SELECT_BY_USER_ID"));
        query.setInt(1, user.getId());
        ResultSet resultSet = query.executeQuery();
        StringBuilder transactionsHTML = new StringBuilder();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("event_id");
            String eventName = resultSet.getString("event_name");
            String transactionType = resultSet.getString("transaction_type");
            String otherUserName = resultSet.getString("other_user");
            User otherUser = new User(otherUserName, null);
            Event event = new Event(eventId, eventName);
            Transaction transaction = new Transaction(0, event, user, transactionType, otherUser);
            transactionsHTML.append(transaction.toHTML());
        }
        return transactionsHTML.toString();

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = Utils.checkLoggedIn(req, resp);
            if (user != null) {
                resp.setStatus(HttpStatus.OK_200);
                PrintWriter writer = resp.getWriter();
                writer.println(Utils.readFile(Paths.get(HTMLPATH)));
                writer.println(queryAllTransactions(user));
                writer.println("</table>");
                writer.println(Utils.PAGE_FOOTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.internalError(resp);
        }
    }
}