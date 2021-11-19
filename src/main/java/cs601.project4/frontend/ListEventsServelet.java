package cs601.project4.frontend;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ListEventsServelet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}