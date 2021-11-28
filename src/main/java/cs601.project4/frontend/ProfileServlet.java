package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.frontend.login.utilities.ClientInfo;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProfileServlet extends HttpServlet {

    private void updateUsersTable() {

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (clientInfoObj == null) {
                resp.sendRedirect("/");
                return;
            }
            ClientInfo clientInfo = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            String name = clientInfo.getName();
            String email = clientInfo.getEmail();
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            Utils.formContent(resp, name, email, "/profile");
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

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            ClientInfo clientInfo = new ClientInfo(name, email);
            req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h2> The info was successfully updated!</h2>");
            Utils.formContent(resp, name, email, "/profile");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}