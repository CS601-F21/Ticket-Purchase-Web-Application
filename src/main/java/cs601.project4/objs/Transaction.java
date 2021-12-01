package cs601.project4.objs;

public class Transaction {
    int id;
    Event event;
    User user;
    String transactionType;
    User otherUser;

    public Transaction(int id, Event event, User user, String transactionType, User otherUser) {
        this.id = id;
        this.event = event;
        this.user = user;
        this.transactionType = transactionType;
        this.otherUser = otherUser;
    }

    public String toHTML() {
        String htm = "<tr><td> " + event.getName() + "    </td><td> " + transactionType +
                "    </td><td> " + otherUser.getName() + "    </td><td style=\"text-align:justify\"> ";
        htm += "</tr>";
        return htm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
