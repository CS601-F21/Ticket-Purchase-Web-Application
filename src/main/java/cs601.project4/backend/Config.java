package cs601.project4.backend;

/**
 * A class to store the properties of the JSON configuration file.
 */
public class Config {

    private String database;
    private String username;
    private String password;
    private String host;

    /**
     * Config class constructor.
     * @param database
     * @param username
     * @param password
     */
    public Config(String database, String username, String password, String host) {
        this.database = database;
        this.username = username;
        this.password = password;
        this.host = host;
    }

    /**
     * Return the database property.
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return the username property.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password property.
     * @return
     */
    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }
}