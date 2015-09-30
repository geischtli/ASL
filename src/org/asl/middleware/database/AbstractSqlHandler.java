package org.asl.middleware.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.asl.common.request.Request;

public abstract class AbstractSqlHandler {
	
	protected final Connection conn;
	protected final Request msg;
	
	public AbstractSqlHandler(Connection conn, Request msg) {
		this.conn = conn;
		this.msg = msg;
	}
	
	public abstract void processMessage();
	public abstract void createTable(String tablename);
	public abstract void dropTable(String tablename);
	public abstract void sendMessage(Request m);
	public abstract Request receiveMessage(String tablename);
}