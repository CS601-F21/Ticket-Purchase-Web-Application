package cs601.project4.frontend.login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.project4.frontend.Utils;
import org.eclipse.jetty.http.HttpStatus;
import cs601.project4.frontend.login.utilities.Config;
import cs601.project4.frontend.login.utilities.LoginUtilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

/**
 * Landing page that allows a user to request to login with Slack.
 */
public class LandingServlet extends HttpServlet {
    private static final String HTMLPATH = "resources/landing.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object UserObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(UserObj != null) {
            // already authed, no need to log in
            resp.getWriter().println(Utils.PAGE_HEADER);
            resp.getWriter().println("<h1>You have already been authenticated</h1>");
            resp.getWriter().println("<p><a href=\"/home\">Home</a>");
            resp.getWriter().println(Utils.PAGE_FOOTER);
            resp.addHeader("REFRESH", "3;URL=/home");
            return;
        }

        // retrieve the config info from the context
        Config config = (Config) req.getServletContext().getAttribute(LoginServerConstants.CONFIG_KEY);

        /** From the OpenID spec:
         * state
         * RECOMMENDED. Opaque value used to maintain state between the request and the callback.
         * Typically, Cross-Site Request Forgery (CSRF, XSRF) mitigation is done by cryptographically
         * binding the value of this parameter with a browser cookie.
         *
         * Use the session ID for this purpose.
         */

        /** From the Open ID spec:
         * nonce
         * OPTIONAL. String value used to associate a Client session with an ID Token, and to mitigate
         * replay attacks. The value is passed through unmodified from the Authentication Request to
         * the ID Token. Sufficient entropy MUST be present in the nonce values used to prevent attackers
         * from guessing values. For implementation notes, see Section 15.5.2.
         */
        String nonce = LoginUtilities.generateNonce(sessionId);

        // Generate url for request to Slack
        String url = LoginUtilities.generateSlackAuthorizeURL(config.getClient_id(),
                sessionId,
                nonce,
                config.getRedirect_url());

        resp.setStatus(HttpStatus.OK_200);
        PrintWriter writer = resp.getWriter();
        writer.println(Utils.readFile(Paths.get(HTMLPATH)));
        writer.println("<a href=\""+url+"\"><img src=\"" + LoginServerConstants.BUTTON_URL +"\" width=\"300\"/></a>");
        writer.println("<h4>OR</h4>");
        Utils.userInfoformContent(resp, "", "", "/login");
        writer.println(Utils.PAGE_FOOTER);
    }


}
