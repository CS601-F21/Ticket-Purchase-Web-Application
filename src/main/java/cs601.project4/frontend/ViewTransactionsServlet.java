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


public class ViewTransactionsServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/view_transactions.html";

    private String queryAllTransactions(User user) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(
                SQLQueries.transactionQueries.get("SELECT_BY_USER_ID"));
        query.setInt(1, user.getId());
        ResultSet resultSet = query.executeQuery();
        StringBuilder transactionsHTML = new StringBuilder();

        while (resultSet.next()) {
            String eventName = resultSet.getString("event_name");
            String transactionType = resultSet.getString("transaction_type");
            String otherUserName = resultSet.getString("other_user");
            User otherUser = new User(otherUserName, null);
            Event event = new Event(eventName);
            Transaction transaction = new Transaction(0, event, user, transactionType, otherUser);
            transactionsHTML.append(transaction.toHTML());
        }
        return transactionsHTML.toString();

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            resp.getWriter().println(Utils.readFile(Paths.get(HTMLPATH)));
            resp.getWriter().println(queryAllTransactions(user));
            resp.getWriter().println("</table> </div>\n" +
                    "</body>\n" +
                    "</html>");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}