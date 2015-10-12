package org.asl.middleware.database.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.timing.TimeLogger;
import org.asl.common.timing.Timing;

public class CommonDAO {

	public static ResultSet executeQuery(Connection conn, PreparedStatement prepStmt, int clientId, int requestId) throws SQLException {
		TimeLogger.click(Timing.MIDDLEWARE_START_QUERY, clientId, requestId);
		ResultSet rs = prepStmt.executeQuery();
		TimeLogger.click(Timing.MIDDLEWARE_END_QUERY, clientId, requestId);
		TimeLogger.click(Timing.MIDDLEWARE_START_COMMIT, clientId, requestId);
		conn.commit();
		TimeLogger.click(Timing.MIDDLEWARE_END_COMMIT, clientId, requestId);
		return rs;
	}
	
}
