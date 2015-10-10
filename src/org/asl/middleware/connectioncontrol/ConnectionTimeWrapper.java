package org.asl.middleware.connectioncontrol;

import java.nio.channels.AsynchronousSocketChannel;

public class ConnectionTimeWrapper {

	private AsynchronousSocketChannel sc;
	private long lastActivity;
	
	public ConnectionTimeWrapper(AsynchronousSocketChannel sc, long lastActivity) {
		this.sc = sc;
		this.lastActivity = lastActivity;
	}
	
	public static ConnectionTimeWrapper create(AsynchronousSocketChannel sc, long lastActivity) {
		return new ConnectionTimeWrapper(sc, lastActivity);
	}

	public void reset() {
		lastActivity = System.currentTimeMillis();
	}
	
	public long getLastActivity() {
		return lastActivity;
	}
	
	public AsynchronousSocketChannel getSocketChannel() {
		return sc;
	}
	
}
