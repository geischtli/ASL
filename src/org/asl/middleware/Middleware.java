package org.asl.middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.completionHandlers.AcceptCompletionHandler;
import org.asl.middleware.connectioncontrol.WatchDog;
import org.asl.middleware.database.config.ASLDatabase;

public class Middleware {
	
	private final AsynchronousServerSocketChannel serverChannel;
	private PropertyParser propParser;
	private final ASLDatabase db;
	public static int INITIAL_BUFSIZE;
	private int requestId;
	private WatchDog watchDog;
	private Timer watchDogTimer;
	private MiddlewareInfo mi;
	public static boolean isShuttingDown;
	public static AtomicInteger numClientsGone;
	
	public Middleware(int port) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.propParser = PropertyParser.create("config/config_common.xml").parse();
		this.db = ASLDatabase.getDatabase();
		Middleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.requestId = -1;
		this.watchDog = WatchDog.create(500);
		this.watchDogTimer = new Timer();
		this.watchDogTimer.scheduleAtFixedRate(this.watchDog, 0, 5000);
		this.mi = MiddlewareInfo.create();
		Middleware.isShuttingDown = false;
		Middleware.numClientsGone = new AtomicInteger(0);
		
		RequestBuilder.getRegisterMiddlewareRequest().processOnMiddleware(mi);
	}
	
	public void accept() {
		serverChannel.accept(null, AcceptCompletionHandler.create(mi, serverChannel, watchDog, requestId));
	}
	
	public void shutdown() {
		System.out.println("Shutting down middleware");
		Middleware.isShuttingDown = true;
		mi.getMyTimeLogger().stopMyTimeLogger();
		try {
			serverChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Middleware successfully shut down");
	}
	
}