package cs601.project4.backend;

import java.sql.SQLException;
import java.sql.Connection;

public class DBManager {
    private static DBManager dbManager;
    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private DBManager() throws SQLException {
        this.connection = DBCPDataSource.getConnection();
    }

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