/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.objs;
/**
 * The Transaction object class
 */
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
    /**
     * Formats the transaction object into a html format that can be shown in a table
     * @return html version of the transaction
     */
    public String toHTML() {
        String htm = "<tr><td style=\"text-align:justify\"> <div class= \"menu\"><a href=\"/event/" +
                event.getId() + "\"class=\"button\">" + event.getName() + "</a></td></div> </td><td> " + transactionType +
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
