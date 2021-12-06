package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    public static String readFile(Path path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader inBuffer = Files.newBufferedReader(path)){
            String line;
            while((line = inBuffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void userInfoformContent(HttpServletResponse resp, String name, String email, String path) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<form action=\"" + path +"\" method=\"POST\">");
        writer.println("<p><label for=\"name\">Name:</label><br>");
        writer.println("<input type=\"text\" id=\"name\" name=\"name\" value=\"" + name + "\"><br></p>");
        writer.println("<p><label for=\"email\">Email:</label><br>");
        writer.println("<input type=\"text\" id=\"email\" name=\"email\" value=\"" + email + "\"><br></p>");
        writer.println("<input type=\"submit\" value=\"Submit!\">\n</form>");
    }

    public static User checkLoggedIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if (user == null) {
            resp.sendRedirect("/");
            return null;
        } else {
            return user;
        }
    }
}
