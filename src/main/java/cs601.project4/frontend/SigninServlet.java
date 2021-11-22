package cs601.project4.frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Inspired by http://highaltitudedev.blogspot.com/2013/10/google-oauth2-with-jettyservlets.html
 */
class SigninServlet extends HttpServlet {
    String googleClientId;
    String redirectURI;

    public SigninServlet(String googleClientId, String redirectURI) {
        this.googleClientId = googleClientId;
        this.redirectURI = redirectURI;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String oauthUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + googleClientId +
                "&response_type=code" +
                "&scope=openid%20email" +
                "&redirect_uri=" + redirectURI +
                "&access_type=offline" +
                "&approval_prompt=force";
        resp.sendRedirect(oauthUrl);
    }
}