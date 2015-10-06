package org.asl.middleware;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;
import java.util.Properties;

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
		
		Properties prop = new Properties();
	    FileInputStream fis =
	      new FileInputStream("configMiddleware.xml");
	    prop.loadFromXML(fis);
	    prop.list(System.out);
	    System.out.println("done");
	}
	
	public abstract void accept();
}