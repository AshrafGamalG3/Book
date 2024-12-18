package library.management.system.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
    private final String user = "root";
    private final String pass = "1234";

    // Constructor with connection creation
    private DatabaseConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new SQLException("Failed to create database connection.", e);
        }
    }

    // Get the instance of DatabaseConnection
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Get the connection, and re-create it if it's closed
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Recreate the connection if it's closed or null
            connection = DriverManager.getConnection(url, user, pass);
        }
        return connection;
    }
}
