package org.asl.middleware.database.config;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.middleware.database.connectionpool.ConnectionPool;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;

public class ASLDatabase {

	private static String url;
	private static Properties props;
	private static ConnectionPool connectionPool;
	private static PropertyParser propParser;
	private String dbIp;
	private String dbPort;
	private String dbName;
	private String user;
	private String password;
	private int maxConnectionToDB;
	private int numAsyncPoolThreads;
	
	public ASLDatabase() throws SQLException {
		propParser = PropertyParser.create("config/config_common.xml").parse();
		dbIp = propParser.getProperty(PropertyKey.DATABASE_IP);
		dbPort = propParser.getProperty(PropertyKey.DATABASE_PORT);
		dbName = propParser.getProperty(PropertyKey.DATABASE_NAME);
		user = propParser.getProperty(PropertyKey.USER);
		password = propParser.getProperty(PropertyKey.PASSWORD);
		maxConnectionToDB = Integer.valueOf(propParser.getProperty(PropertyKey.MAX_CONNECTIONS_TO_DB));
		numAsyncPoolThreads = Integer.valueOf(propParser.getProperty(PropertyKey.NUM_ASYNC_CONNECTION_POOL_THREADS));
		
		ASLDatabase.url = "jdbc:postgresql://" + dbIp + ":" + dbPort + "/" + dbName;
		//ASLDatabase.url = "jdbc:postgresql://" + "localhost" + ":" + dbPort + "/" + dbName;
		ASLDatabase.props = new Properties();
		ASLDatabase.props.setProperty("user", user);
		ASLDatabase.props.setProperty("password", password);
		ASLDatabase.connectionPool = new ConnectionPool(
				maxConnectionToDB,
				ASLDatabase.url,
				ASLDatabase.props,
				numAsyncPoolThreads
			);
	}
	
	public static ASLDatabase getDatabase() throws SQLException {
		return new ASLDatabase();
	}
	
	public static Future<ConnectionWrapper> getNewConnection() throws SQLException {
		return ASLDatabase.connectionPool.acquireConnection();
	}
	
}