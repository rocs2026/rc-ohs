package com.rocs.rc.ohs.data.connection;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides MySQL database connections.
 */
public class MySqlConnectionHelper {

    /** The JDBC URL of the database. */
    public static final String URL = "jdbc:mysql://localhost:3306/rcis";

    /** The database username. */
    public static final String USERNAME = "rcis";

    /** The database password. */
    public static final String PASSWORD = "Changeme0";

    /**
     * Allows subclasses to extend this helper.
     */
    protected MySqlConnectionHelper() {
    }

    /**
     * Opens a connection to the MySQL database.
     *
     * @return A {@link Connection} to the database.
     */
    public static Connection mySqlGetConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception ex) {
            throw new RuntimeException("Error connecting to the database.", ex);
        }
    }
}
