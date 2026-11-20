package com.rocs.rc.ohs.data.connection;

import com.rocs.rc.ohs.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public final class ConnectionHelper {

    /** The Database Driver. */
    private static final String DB_DRIVER =  AppConfig.getProperty("db.driver");

    /** The Database Connection String. */
    private static final String URL =  AppConfig.getProperty("db.url");

    /** The Database User Username. */
    private static final String USERNAME =  AppConfig.getProperty("db.username");

    /** The Database Password. */
    private static final String PASSWORD =  AppConfig.getProperty("db.password");

    private ConnectionHelper() {
    }

    /**
     * Establishes and returns a database connection.
     *
     * @return A valid database connection.
     */
    public static Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found. ");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Incorrect database username or password. ");
        } catch (Exception e) {
            System.out.println("Cannot connect to the database ");
            e.printStackTrace();
        }
        return null;
    }
}
