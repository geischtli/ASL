/*package org.asl.middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.timing.TimeLogger;
import java.util.Timer;
import org.asl.middleware.connectioncontrol.WatchDog;
import org.asl.middleware.database.config.ASLDatabase;

public abstract class AbstractMiddleware {
	protected final AsynchronousServerSocketChannel serverChannel;
	protected PropertyParser propParser;
	protected final ASLDatabase db;
	public static int INITIAL_BUFSIZE;
	protected int requestId;
	//protected Timer clock;
	protected TimeLogger timer;
	protected WatchDog watchDog;
	protected Timer watchDogTimer;
	
	public AbstractMiddleware(int port) throws IOException, SQLException {
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.propParser = PropertyParser.create("config_common.xml").parse();
		this.db = ASLDatabase.getDatabase(
				Integer.valueOf(propParser.getProperty(PropertyKey.MAX_CONNECTIONS_TO_DB))
			);
		AbstractMiddleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.requestId = -1;
		//this.clock = new Timer();
		this.timer = new TimeLogger();
		this.watchDog = WatchDog.create(5);
		this.watchDogTimer = new Timer();
		this.watchDogTimer.scheduleAtFixedRate(this.watchDog, 0, 5000);
		
		RequestBuilder.getRequest(RequestType.REGISTER_MIDDLEWARE, null).processOnMiddleware(null, 0);
	}
	
	public abstract void accept();
}*/