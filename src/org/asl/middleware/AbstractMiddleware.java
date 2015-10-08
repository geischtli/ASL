package org.asl.middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.database.config.ASLDatabase;

public abstract class AbstractMiddleware {
	protected final AsynchronousServerSocketChannel serverChannel;
	protected PropertyParser propParser;
	protected final ASLDatabase db;
	protected static int INITIAL_BUFSIZE;
	
	public AbstractMiddleware(int port) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.propParser = PropertyParser.create("config_common.xml").parse();
		this.db = ASLDatabase.getDatabase(
				Integer.valueOf(propParser.getProperty(PropertyKey.MAX_CONNECTIONS_TO_DB))
			);
		AbstractMiddleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		RequestBuilder.getRequest(RequestType.REGISTER_MIDDLEWARE, null).processOnMiddleware();
	}
	
	public abstract void accept();
}