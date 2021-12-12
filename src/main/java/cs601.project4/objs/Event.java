/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.objs;

/**
 * The Event object class
 */
public class Event {
    int id;
    String name;
    User createdBy;
    String description;
    int available;
    int purchased;

    public Event(int id, String name, User createdBy, String description, int available, int purchased) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.description = description;
        this.available = available;
        this.purchased = purchased;
    }

    public Event(String name) {
        this.name = name;
    }

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Formats the event object into a html format that can be shown in a table
     * @param page The page on which the event is intended to be shown
     * @return html version of the event
     */
    public String toHTML(String page) {
        String htm = "<tr><td style=\"text-align:justify\"> <div class= \"menu\"><a href=\"/event/" +
                id + "\" class=\"button\">" + name + "</a></td></div>";
        if (page.equals("list-events")) {
            htm += "<td> " + createdBy.getName() + " </td><td> " + description + " </td><td> " + available + " </td><td> " + purchased  + " </td>";
            htm += "<td style=\"text-align:justify\"> <div class= \"menu\"><a href=\"/purchase/" + id +
                    "\" class=\"button\">Purchase!</a></td></div>\n";
        } else if (page.equals("my-tickets")) {
            htm += "<td style=\"text-align:justify\"> <div class= \"menu\"><a href=\"/transfer/" + id +
                    "\" class=\"button\">Transfer</a></td></div>\n";
        }
        htm += "</tr>";
        return htm;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }
}
