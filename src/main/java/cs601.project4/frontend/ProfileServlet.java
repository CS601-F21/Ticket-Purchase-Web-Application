package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.User;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProfileServlet extends HttpServlet {

    private void updateUsersTable() {

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Object UserObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (UserObj == null) {
                resp.sendRedirect("/");
                return;
            }
            User User = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            String name = User.getName();
            String email = User.getEmail();
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
            Object UserObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (UserObj == null) {
                resp.sendRedirect("/");
                return;
            }

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            User User = new User(name, email);
            req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, User);
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