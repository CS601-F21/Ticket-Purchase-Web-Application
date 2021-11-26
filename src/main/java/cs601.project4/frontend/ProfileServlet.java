package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.frontend.login.utilities.ClientInfo;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ProfileServlet extends HttpServlet {

    private void formContent(HttpServletResponse resp, String firstName, String lastName) throws IOException {
        resp.getWriter().println("<form action=\"/profile\" method=\"post\">");
        resp.getWriter().println("<label for=\"fname\">First name:</label><br>");
        resp.getWriter().println("<input type=\"text\" id=\"fname\" name=\"fname\" value=\"" + firstName + "\"><br>");
        resp.getWriter().println("<label for=\"lname\">Last name:</label><br>");
        resp.getWriter().println("<input type=\"text\" id=\"lname\" name=\"lname\" value=\"" + lastName + "\"><br>");
        resp.getWriter().println("<input type=\"submit\" value=\"Update!\">\n</form>");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (clientInfoObj == null) {
                resp.sendRedirect("/");
                return;
            }
            ClientInfo clientInfo = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            String firstName = clientInfo.getName().split(" ")[0];
            String lastName = clientInfo.getName().split(" ")[1];
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            formContent(resp, firstName, lastName);
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (clientInfoObj == null) {
                resp.sendRedirect("/");
                return;
            }

            ClientInfo clientInfo = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            String firstName = req.getParameter("fname");
            String lastName = req.getParameter("lname");
            clientInfo.setName(firstName + " " + lastName);
            req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h2> The info was successfully updated!</h2>");
            formContent(resp, firstName, lastName);
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}