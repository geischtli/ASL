//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

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
	private static PropertyParser propParser = PropertyParser.create("config/config_common.xml").parse();
	private static String dbIp;
	private static String dbPort;
	private static String dbName;
	private static String user;
	private static String password;
	private static int numConnectionsToDB;
	private static int numAsyncPoolThreads;
	
	public static void initDatabase() throws NumberFormatException, SQLException {
		ASLDatabase.initDatabase(Integer.valueOf(propParser.getProperty(PropertyKey.MAX_CONNECTIONS_TO_DB)));
	}
	
	public static void initDatabase(int numConnectionsToDB) throws NumberFormatException, SQLException {
		ASLDatabase.dbIp = ASLDatabase.propParser.getProperty(PropertyKey.DATABASE_IP);
		ASLDatabase.dbPort = ASLDatabase.propParser.getProperty(PropertyKey.DATABASE_PORT);
		ASLDatabase.dbName = ASLDatabase.propParser.getProperty(PropertyKey.DATABASE_NAME);
		ASLDatabase.user = ASLDatabase.propParser.getProperty(PropertyKey.USER);
		ASLDatabase.password = ASLDatabase.propParser.getProperty(PropertyKey.PASSWORD);
		ASLDatabase.numConnectionsToDB = numConnectionsToDB;
		ASLDatabase.numAsyncPoolThreads = Integer.valueOf(propParser.getProperty(PropertyKey.NUM_ASYNC_CONNECTION_POOL_THREADS));

		ASLDatabase.url = "jdbc:postgresql://" + dbIp + ":" + dbPort + "/" + dbName;
		//ASLDatabase.url = "jdbc:postgresql://" + "localhost" + ":" + dbPort + "/" + dbName;
		ASLDatabase.props = new Properties();
		ASLDatabase.props.setProperty("user", user);
		ASLDatabase.props.setProperty("password", password);
		ASLDatabase.connectionPool = new ConnectionPool(
				ASLDatabase.numConnectionsToDB,
				ASLDatabase.url,
				ASLDatabase.props,
				ASLDatabase.numAsyncPoolThreads
			);
	}
	
	public static Future<ConnectionWrapper> getNewConnection() throws SQLException {
		return ASLDatabase.connectionPool.acquireConnection();
	}
	
}