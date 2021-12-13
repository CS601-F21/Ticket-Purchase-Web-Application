/**
 * Author: Firoozeh Kaveh
 */
package cs601.project4.backend;

import java.sql.SQLException;
import java.sql.Connection;
/**
 * Class to help access the SQL database. It is mainly a wrapper around DBCPDataSource
 */
public class DBManager {
    private static DBManager dbManager;
    private final Connection connection;

    /**
     * returns the connection pool to the database
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    private DBManager() throws SQLException {
        this.connection = DBCPDataSource.getConnection();
    }

    /**
     * if the object has been initiated, return it otherwise initiate and return it. This is done so that we don't have
     * to initiate this object everytime we need to access the database
     * @return the dbmanager object
     */
    public synchronized static DBManager getInstance() {
        try {
            if (dbManager == null) {
                dbManager = new DBManager();
            }
            return dbManager;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}