package com.project.artconnect.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import com.project.artconnect.config.DatabaseConfig;
import javafx.application.Platform;


/**
 * Utility class to manage JDBC connections.
 * TODO: Students must implementation the getConnection logic.
 */
public class ConnectionManager {
	
	
	private static Connection Connection = null;

    /**
     * Provides a connection to the MySQL database.
     * 
     * @return Connection object
     * exit the application if fails.
     */
    public static Connection getConnection() {
        // TODO: Students should implement this using DatabaseConfig properties
        // return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
        // DatabaseConfig.PASSWORD);
		try {
			if ( Connection == null || Connection.isClosed()) {
				Connection = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
			}
		} catch (SQLException ex) {
			Platform.exit();
		}

		return Connection;
    }
}
