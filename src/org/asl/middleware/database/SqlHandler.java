package org.asl.middleware.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.asl.common.request.Request;

public class SqlHandler extends AbstractSqlHandler {

	public SqlHandler(Connection conn, Request msg) {
		super(conn, msg);
	}
	
	public static SqlHandler getHandler(Connection conn, Request msg) {
		return new SqlHandler(conn, msg);
	}
	
	@Override
	public void createTable(String tablename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropTable(String tablename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(Request m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Request receiveMessage(String tablename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processMessage() {
		// assume insert
		Statement s;
		int i = 15;
		int a = i;
		String query = msg.action + "(" + msg.content + ") RETURNING ID;";
		try {
			s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			rs.next();
			int id = rs.getInt(1);
			int b = a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
