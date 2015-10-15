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
	
	public ASLDatabase(int maxConnectionsToDB) throws SQLException {
		propParser = PropertyParser.create("config_common.xml").parse();
		dbIp = propParser.getProperty(PropertyKey.DATABASE_IP);
		dbPort = propParser.getProperty(PropertyKey.DATABASE_PORT);
		dbName = propParser.getProperty(PropertyKey.DATABASE_NAME);
		user = propParser.getProperty(PropertyKey.USER);
		password = propParser.getProperty(PropertyKey.PASSWORD);
		
		ASLDatabase.url = "jdbc:postgresql://" + dbIp + ":" + dbPort + "/" + dbName;
		ASLDatabase.props = new Properties();
		ASLDatabase.props.setProperty("user", user);
		ASLDatabase.props.setProperty("password", password);
		ASLDatabase.connectionPool = new ConnectionPool(
				maxConnectionsToDB,
				ASLDatabase.url,
				ASLDatabase.props,
				1
			);
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