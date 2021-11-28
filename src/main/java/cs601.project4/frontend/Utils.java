package cs601.project4.frontend;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
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

    public static void formContent(HttpServletResponse resp, String name, String email, String path) throws IOException {
        resp.getWriter().println("<form action=\"" + path +"\" method=\"post\">");
        resp.getWriter().println("<label for=\"name\">Name:</label><br>");
        resp.getWriter().println("<input type=\"text\" id=\"name\" name=\"name\" value=\"" + name + "\"><br>");
        resp.getWriter().println("<label for=\"email\">Email:</label><br>");
        resp.getWriter().println("<input type=\"text\" id=\"email\" name=\"email\" value=\"" + email + "\"><br>");
        resp.getWriter().println("<input type=\"submit\" value=\"Submit!\">\n</form>");
    }
}
