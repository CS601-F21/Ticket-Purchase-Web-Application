package cs601.project4.frontend.login;

import cs601.project4.frontend.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles a request to sign out
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // log out by invalidating the session
        req.getSession().invalidate();
        Utils.defaultResponse("<h1>Thanks for your business! Come back soon!</h1>", resp);
    }
}
