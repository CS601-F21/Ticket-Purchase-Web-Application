package cs601.project4.objs;

public class Event {
    int id;
    String name;
    User createdBy;
    int available;
    int purchased;

    public Event(int id, String name, User createdBy, int available, int purchased) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.available = available;
        this.purchased = purchased;
    }

    public Event(String name) {
        this.name = name;
    }

    public String toHTML(String page) {
        String htm = "<tr><td> " + name + "    </td><td> " + createdBy.getName() +
                "    </td><td> " + available + "    </td><td> " + purchased  + "    </td><td style=\"text-align:justify\"> ";
        if (page.equals("list-events")) {
            htm += "<div class= \"menu\"><form action=\"/purchase/" + id + " method=\"GET\"><button name=\"purchase\" value=\"" + id +
                    "\" type=\"submit\">Purchase Ticket</button></form></td></div>\n";
        } else if (page.equals("my-events")) {
            htm += "<div class= \"menu\"><form action=\"/events/" + id + " method=\"GET\"><button name=\"editEvent\" value=\"" + id +
                    "\" type=\"submit\">Edit This Event</button></form></td></div>\n";
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
