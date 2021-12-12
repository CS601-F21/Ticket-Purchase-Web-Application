package cs601.project4.frontend;

import cs601.project4.frontend.login.LoginServerConstants;
import cs601.project4.objs.Event;
import cs601.project4.objs.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "</div>\n" +
            "</body>\n" +
            "</html>";

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

    public static void defaultResponse(String message, HttpServletResponse resp) {
        try {
            String imgURL = "https://i.ibb.co/8rGxZWy/Screen-Shot-2021-11-27-at-9-58-28-PM-removebg-preview.png";
            String result = readFile(Path.of("resources/default.html")) +
                    message +
                    "<p><a href=\"/home\"><img src=\"" + imgURL + "\" width=\"200\"/></a>" +
                    Utils.PAGE_FOOTER;
            resp.getWriter().println(result);
            resp.addHeader("REFRESH", "2;URL=/home");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void internalError(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Utils.defaultResponse("<h1>Something went wrong!</h1>", resp);
    }

    public static void userInfoformContent(HttpServletResponse resp, String name, String email, String path) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<form action=\"" + path +"\" method=\"POST\">");
        if (!path.contains("transfer")) {
            writer.println("<p><label for=\"name\">Name:</label><br>");
            writer.println("<input type=\"text\" id=\"name\" name=\"name\" value=\"" + name + "\"><br></p>");
        }
        writer.println("<p><label for=\"email\">Email:</label><br>");
        writer.println("<input type=\"text\" id=\"email\" name=\"email\" value=\"" + email + "\"><br></p>");
        writer.println("<input type=\"submit\" value=\"Submit!\">\n</form>");
    }

    public static void eventInfoformContent(HttpServletResponse resp, Event event, String path, boolean readOnly) throws IOException {
        PrintWriter writer = resp.getWriter();
        String readOnlyArgument = "";
        if (readOnly) {
            readOnlyArgument = "readonly";
        }
        writer.println("<form action=\"" + path +"\" method=\"POST\">");
        writer.println("<p><label for=\"name\">Event Name:</label><br>");
        writer.println("<input type=\"text\" id=\"name\" name=\"name\" value=\"" + event.getName() + "\"" + readOnlyArgument + "><br></p>");
        writer.println("<p><label for=\"created-by\">Created By:</label><br>");
        writer.println("<input type=\"text\" id=\"created-by\" name=\"created-by\" value=\"" + event.getCreatedBy().getName() + "\" readonly><br></p>");
        writer.println("<p><label for=\"available\">Available Tickets</label><br>");
        writer.println("<input type=\"number\" id=\"available\" name=\"available\" value=\"" + event.getAvailable() + "\"" + readOnlyArgument + "><br></p>");
        writer.println("<p><label for=\"purchased\">Purchased Tickets</label><br>");
        writer.println("<input type=\"number\" id=\"purchased\" name=\"purchased\" value=\"" + event.getPurchased() + "\"" + readOnlyArgument + "><br></p>");
        if (!readOnly) {
            writer.println("<input name=\"operation\" type=\"submit\" value=\"Submit\">");
            writer.println("<input name=\"operation\" type=\"submit\" value=\"Delete\">");
        }
        writer.println("</form>");
    }

    public static User checkLoggedIn(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
            if (user == null) {
                resp.sendRedirect("/");
                return null;
            } else {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
