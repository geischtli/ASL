package org.asl.middleware.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.asl.common.message.Message;

public abstract class AbstractSqlHandler {
	
	protected final Connection conn;
	protected final Message msg;
	
	public AbstractSqlHandler(Connection conn, Message msg) {
		this.conn = conn;
		this.msg = msg;
	}
	
	public abstract void processMessage();
	public abstract void createTable(String tablename);
	public abstract void dropTable(String tablename);
	public abstract void sendMessage(Message m);
	public abstract Message receiveMessage(String tablename);
}