package org.asl.middleware.database.config;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.asl.middleware.database.connectionpool.ConnectionPool;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;

public class ASLDatabase {

	private static String url;
	private static Properties props;
	private static ConnectionPool connectionPool;
	
	public ASLDatabase(boolean initDB, int contentLength, int maxConnectionsToDB) throws SQLException {
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
		ASLDatabase.props.setProperty("password", "postgres");
	}
	
	public static ASLDatabase getDatabase(boolean initDB, int contentLength, int maxConnectionsToDB) throws SQLException {
		return new ASLDatabase(
				initDB,
				contentLength,
				maxConnectionsToDB
			);
	}
	
	public static Future<ConnectionWrapper> getNewConnection() throws SQLException {
		return ASLDatabase.connectionPool.acquireConnection();
	}
	
}