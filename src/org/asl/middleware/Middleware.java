package org.asl.middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;
import java.util.Timer;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.completionHandlers.AcceptCompletionHandler;
import org.asl.middleware.connectioncontrol.WatchDog;
import org.asl.middleware.database.config.ASLDatabase;

public class Middleware {
	
	protected final AsynchronousServerSocketChannel serverChannel;
	protected PropertyParser propParser;
	protected final ASLDatabase db;
	public static int INITIAL_BUFSIZE;
	protected int requestId;
	protected WatchDog watchDog;
	protected Timer watchDogTimer;
	
	public Middleware(int port) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.propParser = PropertyParser.create("config_common.xml").parse();
		this.db = ASLDatabase.getDatabase();
		Middleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.requestId = -1;
		this.watchDog = WatchDog.create(500);
		this.watchDogTimer = new Timer();
		this.watchDogTimer.scheduleAtFixedRate(this.watchDog, 0, 5000);
		
		RequestBuilder.getRegisterMiddlewareRequest().processOnMiddleware();
	}
	
	public void accept() {
		serverChannel.accept(null, AcceptCompletionHandler.create(serverChannel, watchDog, requestId));
	}
	
}