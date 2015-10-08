package org.asl.middleware.database.config;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.asl.middleware.database.connectionpool.ConnectionPool;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;

public class ASLDatabase {

	private static String url;
	private static Properties props;
	private static ConnectionPool connectionPool;
	
	public ASLDatabase(int maxConnectionsToDB) throws SQLException {
		ASLDatabase.url = "jdbc:postgresql://localhost/mydb";
		ASLDatabase.props = new Properties();
		ASLDatabase.props.setProperty("user", "postgres");
		ASLDatabase.props.setProperty("password", "postgres");
		ASLDatabase.connectionPool = new ConnectionPool(
				maxConnectionsToDB,
				ASLDatabase.url,
				ASLDatabase.props,
				1
			);
		//DriverManager.setLogWriter(new PrintWriter(System.out));
	}
	
	public static ASLDatabase getDatabase(int maxConnectionsToDB) throws SQLException {
		return new ASLDatabase(
				maxConnectionsToDB
			);
	}
	
	public static Future<ConnectionWrapper> getNewConnection() throws SQLException {
		return ASLDatabase.connectionPool.acquireConnection();
	}
	
}