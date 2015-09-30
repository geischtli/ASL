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
	
	private static String DROP_ALL_TABLES_AND_SEQUENCES_SQL =
			"DROP SCHEMA PUBLIC CASCADE;" +
			"CREATE SCHEMA PUBLIC;";
	private static String CREATE_CLIENT_SEQUENCE_SQL =
			"CREATE SEQUENCE CLIENT START 1;";
	private static String CREATE_QUEUE_SEQUENCE_SQL =
			"CREATE SEQUENCE QUEUE START 1;";
	private static String CREATE_MESSAGE_TABLE_SQL =
			"CREATE TABLE MESSAGE(" +
			"ID SERIAL PRIMARY KEY," +
			"SENDER INT DEFAULT nextval('client')," +
			"RECEIVER INT DEFAULT nextval('client')," +
			"QUEUE INT DEFAULT nextval('queue'));";
	
	public ASLDatabase(boolean initDB) throws SQLException {
		this.url = "jdbc:postgresql://localhost/mydb";
		this.props = new Properties();
		this.props.setProperty("user", "postgres");
		this.props.setProperty("password", "postgres");
		this.conn = DriverManager.getConnection(url, props);
		this.conn.setAutoCommit(false);
		if (initDB) {
			System.out.println("I'm gonna clear the DB and set all tables newly up!");
			initDB();
		}
	}
	
	private void initDB() throws SQLException {
		try (PreparedStatement drop_all_tables_and_sequences = conn.prepareStatement(DROP_ALL_TABLES_AND_SEQUENCES_SQL);
				PreparedStatement create_clients_sequence = conn.prepareStatement(CREATE_CLIENT_SEQUENCE_SQL);
				PreparedStatement create_queues_sequence = conn.prepareStatement(CREATE_QUEUE_SEQUENCE_SQL);
				PreparedStatement create_messages_table = conn.prepareStatement(CREATE_MESSAGE_TABLE_SQL)) {
			drop_all_tables_and_sequences.execute();
			create_clients_sequence.execute();
			create_queues_sequence.execute();
			create_messages_table.execute();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		}
	}

	public static ASLDatabase getDatabase(boolean initDB) throws SQLException {
		return new ASLDatabase(initDB);
	}
	
	public static Connection getNewConnection() throws SQLException {
		Connection c = DriverManager.getConnection(url, props);
		c.setAutoCommit(false);
		return c;
	}
}
