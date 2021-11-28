package cs601.project4.frontend.login.utilities;

/**
 * A class to maintain info about each client.
 */
public class ClientInfo {

    private String name;
    private String email;
    private int id;

    /**
     * Constructor
     * @param name
     */
    public ClientInfo(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = -1;
    }

    /**
     * return name
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
