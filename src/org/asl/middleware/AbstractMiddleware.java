package org.asl.middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;

import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.database.config.ASLDatabase;

public abstract class AbstractMiddleware {
	protected final AsynchronousServerSocketChannel serverChannel;
	protected final ASLDatabase db;
	
	public AbstractMiddleware(int port, boolean initDB) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.db = ASLDatabase.getDatabase(initDB, 200);
		// Register this Middleware instance on the database (i.e. get an id into MiddlewareInfo)
		RequestBuilder.newRegisterMiddlewareRequest().processOnMiddleware();
	}
	
	public abstract void accept();
}