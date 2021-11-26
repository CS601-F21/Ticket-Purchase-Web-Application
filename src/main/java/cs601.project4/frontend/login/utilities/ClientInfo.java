package cs601.project4.frontend.login.utilities;

/**
 * A class to maintain info about each client.
 */
public class ClientInfo {

    private String name;

    /**
     * Constructor
     * @param name
     */
    public ClientInfo(String name) {
        this.name = name;
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
}
