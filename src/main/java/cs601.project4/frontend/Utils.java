/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.frontend;

import cs601.project4.backend.DBManager;
import cs601.project4.backend.SQLQueries;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "</div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * reads a file from path
     * @param path the path to read the file from
     * @return the string content of the file
     */
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

    /**
     * The default page that is shown when we want to show a quick message and redirect the user to home
     * @param message the message to be shown on the page
     * @param resp the response object to write the page to
     */
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

    /**
     * The page to show when an internal server error happens
     * @param resp the response object to write to
     */
    public static void internalError(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Utils.defaultResponse("<h1>Something went wrong!</h1>", resp);
    }

    /**
     * utility function to make a form for taking user information
     * @param resp response object to write the form to
     * @param name the name of the user
     * @param email the email of the user
     * @param path the path the form direct to after the submit button is pressed
     * @throws IOException
     */
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

    /**
     * utility function to make a form for taking event information
     * @param resp response object to write the form to
     * @param event the event to use for the form content
     * @param path the path the form direct to after the submit button is pressed
     * @param readOnly whether the text boxes should be editable or not
     * @throws IOException
     */
    public static void eventInfoFormContent(HttpServletResponse resp, Event event, String path, boolean readOnly) throws IOException {
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

    /**
     * check if the user is logged in otherwise redirect to landing page. Used almost on all servlets
     * so that the user is forced to log in before accessing these servlets
     * @param req the http request
     * @param resp the http response
     * @return the user object if they are logged in
     */
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

    /**
     * from a user id or email find the user in the database
     * @param email the email of the user, if null, the search happens based on the user id
     * @param userId the user id
     * @return the user object
     * @throws SQLException
     */
    public static User queryUsersTable(String email, int userId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query;
        if (email != null) {
            query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("SELECT_BY_EMAIL"));
            query.setString(1, email);
        } else {
            query = dbManager.getConnection().prepareStatement(SQLQueries.userQueries.get("SELECT_BY_ID"));
            query.setInt(1, userId);
        }
        ResultSet resultSet = query.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            email = resultSet.getString("email");
            return new User(name, email, id);
        }
        else {
            return null;
        }
    }

    /**
     * from an event id find the event from the database
     * @param eventId the ide of the event to find
     * @return the found event object
     * @throws SQLException
     */
    public static Event getEvent(int eventId) throws SQLException {
        DBManager dbManager = DBManager.getInstance();
        assert dbManager != null;
        PreparedStatement query = dbManager.getConnection().prepareStatement(SQLQueries.eventQueries.get("SELECT"));
        query.setInt(1, eventId);
        ResultSet resultSet = query.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String eventName = resultSet.getString("event_name");
            String description = resultSet.getString("description");
            int available = resultSet.getInt("available");
            int purchased = resultSet.getInt("purchased");
            User user = queryUsersTable(null, resultSet.getInt("created_by"));
            return new Event(id, eventName, user, description, available, purchased);
        }
        return null;
    }
}
