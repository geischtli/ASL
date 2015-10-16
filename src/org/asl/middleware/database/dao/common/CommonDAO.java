package org.asl.middleware.database.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.timing.Timing;
import org.asl.middleware.MiddlewareInfo;

public class CommonDAO {

	public static ResultSet executeQuery(Connection conn,
			PreparedStatement prepStmt, int clientId, int requestId, MiddlewareInfo mi) throws SQLException {
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_START_QUERY, clientId, requestId);
		ResultSet rs = prepStmt.executeQuery();
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_END_QUERY, clientId, requestId);
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_START_COMMIT, clientId, requestId);
		conn.commit();
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_END_COMMIT, clientId, requestId);
		return rs;
	}
	
}
