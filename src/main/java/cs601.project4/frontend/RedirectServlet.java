package cs601.project4.frontend;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
/**
 * Inspired by http://highaltitudedev.blogspot.com/2013/10/google-oauth2-with-jettyservlets.html
 */
class RedirectServlet extends HttpServlet {
    String googleClientId;
    String redirectURI;
    String googleClientSecret;

    public RedirectServlet(String googleClientId, String redirectURI, String googleClientSecret) {
        this.googleClientId = googleClientId;
        this.redirectURI = redirectURI;
        this.googleClientSecret = googleClientSecret;
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("error") != null) {
            resp.getWriter().println(req.getParameter("error"));
            return;
        }

        String code = req.getParameter("code");
        Map<String, String> params = Map.ofEntries(
                Map.entry("code", code),
                Map.entry("client_id", googleClientId),
                Map.entry("client_secret", googleClientSecret),
                Map.entry("redirect_uri", redirectURI),
                Map.entry("grant_type", "authorization_code")
        );
        Gson gson = new Gson();
        String body = HTTPFetcher.doPost("https://accounts.google.com/o/oauth2/token", null, gson.toJson(params));


        GoogleResponse googleResponse;
        googleResponse = gson.fromJson(body, GoogleResponse.class);

        String accessToken = googleResponse.getAccess_token();

//        req.getSession().setAttribute("access_token", accessToken);

        String json = HTTPFetcher.doGet("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken);

        resp.getWriter().println(json);
    }
}