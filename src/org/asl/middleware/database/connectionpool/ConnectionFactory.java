//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware.database.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

	public static ConnectionWrapper create(ConnectionPool pool, Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ConnectionWrapper(pool, conn);
	}
	
}
