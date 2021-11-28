package cs601.project4.frontend.login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
import org.eclipse.jetty.http.HttpStatus;
import cs601.project4.frontend.login.utilities.ClientInfo;
import cs601.project4.frontend.login.utilities.Config;
import cs601.project4.frontend.login.utilities.HTTPFetcher;
import cs601.project4.frontend.login.utilities.LoginUtilities;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 */
public class LoginServlet extends HttpServlet {

    private int insertIntoUsersTable(ClientInfo clientInfo) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("INSERT"), Statement.RETURN_GENERATED_KEYS);
        query.setString(1, clientInfo.getName());
        query.setString(2, clientInfo.getEmail());
        return query.executeUpdate();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>You have already been authenticated</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            return;
        }

        // retrieve the config info from the context
        Config config = (Config) req.getServletContext().getAttribute(LoginServerConstants.CONFIG_KEY);

        // retrieve the code provided by Slack
        String code = req.getParameter(LoginServerConstants.CODE_KEY);

        // generate the url to use to exchange the code for a token:
        // After the user successfully grants your app permission to access their Slack profile,
        // they'll be redirected back to your service along with the typical code that signifies
        // a temporary access code. Exchange that code for a real access token using the
        // /openid.connect.token method.
        String url = LoginUtilities.generateSlackTokenURL(config.getClient_id(), config.getClient_secret(), code, config.getRedirect_url());

        // Make the request to the token API
        String responseString = HTTPFetcher.doGet(url, null);
        Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);

        ClientInfo clientInfo = LoginUtilities.verifyTokenResponse(response, sessionId);

        if (clientInfo == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Oops, login unsuccessful</h1>");
        } else {
            try {
                int id = insertIntoUsersTable(clientInfo);
                clientInfo.setId(id);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Hello, " + clientInfo.getName() + "</h1>");
            resp.getWriter().println("<h2>You will be automatically redirected to the Homepage if not, click on the link below</h2>");
            resp.getWriter().println("<p><a href=\"/home\">Home</a>");

        }
        resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        resp.addHeader("REFRESH", "5;URL=/home");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>You have already been authenticated</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            return;
        }
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        ClientInfo clientInfo = new ClientInfo(name, email);
        req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1>Hello, " + clientInfo.getName() + "</h1>");
        resp.getWriter().println("<h2>You will be automatically redirected to the Homepage if not, click on the link below</h2>");
        resp.getWriter().println("<p><a href=\"/home\">Home</a>");
        resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        resp.addHeader("REFRESH", "5;URL=/home");
    }
}
