package org.asl.middleware.database.connectionpool;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;

public class ConnectionWrapper implements Closeable {

	private final ConnectionPool connectionPool;
	private Connection conn;
	
	public ConnectionWrapper(ConnectionPool connectionPool, Connection conn) {
		this.connectionPool = connectionPool;
		this.conn = conn;
	}
	
	public Connection get() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void close() throws IOException {
		connectionPool.releaseConnection(this);
	}
	
}