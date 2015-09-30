package org.asl.middleware.database.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ASLDatabase {

	private static String url;
	private static Properties props;
	private final Connection conn;
	private static int contentLength;
	
	private static String DROP_ALL_TABLES_SQL =
			"DROP SCHEMA PUBLIC CASCADE;" +
			"CREATE SCHEMA PUBLIC;";
	private static String CREATE_SEQUENCE_MIDDLEWARE_SQL =
			"CREATE SEQUENCE MIDDLEWARE START 1";
	private static String CREATE_CLIENT_TABLE_SQL =
			"CREATE TABLE CLIENT(" +
					"ID SERIAL PRIMARY KEY," +
					"ON_MIDDLEWARE INT DEFAULT nextval('middleware')" +
			");";
	private static String CREATE_QUEUE_TABLE_SQL =
			"CREATE TABLE QUEUE(" +
					"ID SERIAL PRIMARY KEY," +
					"CREATOR_CLIENT INT REFERENCES CLIENT (ID)" +
			");";
	private static String CREATE_MESSAGE_TABLE_SQL =
			"CREATE TABLE MESSAGE(" +
			"ID SERIAL PRIMARY KEY," +
			"SENDER INT REFERENCES CLIENT (ID)," +
			"RECEIVER INT REFERENCES CLIENT (ID)," +
			"QUEUE INT REFERENCES QUEUE (ID)," +
			"CONTENT VARCHAR("; // dynamic length set in constructor
	
	public ASLDatabase(boolean initDB, int contentLength) throws SQLException {
		this.url = "jdbc:postgresql://localhost/mydb";
		this.props = new Properties();
		this.props.setProperty("user", "postgres");
		this.props.setProperty("password", "postgres");
		this.conn = DriverManager.getConnection(url, props);
		this.conn.setAutoCommit(false);
		ASLDatabase.contentLength = contentLength;
		ASLDatabase.CREATE_MESSAGE_TABLE_SQL += ASLDatabase.contentLength + "));";
		if (initDB) {
			System.out.println("I'm gonna clear the DB and set all tables newly up!");
			initDB();
		}
	}
	
	private void initDB() throws SQLException {
		try (PreparedStatement dropAllTablesAndSequences = conn.prepareStatement(DROP_ALL_TABLES_SQL);
				PreparedStatement createMiddlewareSequence = conn.prepareStatement(CREATE_SEQUENCE_MIDDLEWARE_SQL);
				PreparedStatement createClientsTable= conn.prepareStatement(CREATE_CLIENT_TABLE_SQL);
				PreparedStatement createQueuesTable = conn.prepareStatement(CREATE_QUEUE_TABLE_SQL);
				PreparedStatement createMessagesTable = conn.prepareStatement(CREATE_MESSAGE_TABLE_SQL)) {
			dropAllTablesAndSequences.execute();
			createMiddlewareSequence.execute();
			createClientsTable.execute();
			createQueuesTable.execute();
			createMessagesTable.execute();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		}
	}

	public static ASLDatabase getDatabase(boolean initDB, int contentLength) throws SQLException {
		return new ASLDatabase(initDB, contentLength);
	}
	
	public static Connection getNewConnection() throws SQLException {
		Connection c = DriverManager.getConnection(url, props);
		c.setAutoCommit(false);
		return c;
	}
}
