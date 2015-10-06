package org.asl.middleware;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;
import java.util.Properties;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.database.config.ASLDatabase;

public abstract class AbstractMiddleware {
	protected final AsynchronousServerSocketChannel serverChannel;
	protected final ASLDatabase db;
	protected final Object lock;
	
	public AbstractMiddleware(int port) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.db = ASLDatabase.getDatabase(
				200,
				10
			);
		this.lock = new Object();
		// Register this Middleware instance on the database (i.e. get an id into MiddlewareInfo)
		RequestBuilder.getRequest(RequestType.REGISTER_MIDDLEWARE).processOnMiddleware();
		
		// TODO: go on...
		int numasync = Integer.valueOf(PropertyParser.create("configMiddleware.xml").parse().getProperty(PropertyKey.NUMASYNCCONNECTIONPOOLTHREADS));
	}
	
	public abstract void accept();
}