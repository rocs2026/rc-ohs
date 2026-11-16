package com.rocs.rc.ohs.data.connection;

import com.rocs.rc.ohs.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public final class ConnectionHelper {

    private ConnectionHelper() {
    }

    /**
     * Establishes and returns a database connection.
     *
     * @return A valid database connection.
     */
    public static Connection getConnection() {
        try {
            String driver = AppConfig.getProperty("db.driver");
            String url = AppConfig.getProperty("db.url");
            String username = AppConfig.getProperty("db.username");
            String password = AppConfig.getProperty("db.password");

            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found. ");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Incorrectt database username or password. ");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Cannot connect to the database ");
            e.printStackTrace();
        }
        return null;
    }
}
