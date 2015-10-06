package org.asl.middleware.database.connectionpool;

import java.sql.Connection;

public class ConnectionFactory {

	public static ConnectionWrapper create(ConnectionPool pool, Connection conn) {
		return new ConnectionWrapper(pool, conn);
	}
	
}
