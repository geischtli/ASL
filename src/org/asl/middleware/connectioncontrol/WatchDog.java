package org.asl.middleware.connectioncontrol;

import java.io.IOException;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WatchDog extends TimerTask {

	private BlockingQueue<ConnectionTimeWrapper> connections;
	private long maxActivityDiffMillis;
	
	public WatchDog(long maxActivityDiffSec) {
		this.connections = new LinkedBlockingQueue<ConnectionTimeWrapper>();
		this.maxActivityDiffMillis = maxActivityDiffSec*1000L;
	}
	
	public static WatchDog create(int maxActivityDiffSec) {
		return new WatchDog(maxActivityDiffSec);
	}
	
	public void addConnection(ConnectionTimeWrapper c) {
		connections.add(c);
	}
	
	public void closeConnection(ConnectionTimeWrapper c) {
		try {
			c.getSocketChannel().close();
			System.out.println("closed conn");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		System.out.println("Dog barks");
		for (Iterator<ConnectionTimeWrapper> it = connections.iterator(); it.hasNext();) {
			ConnectionTimeWrapper c = it.next();
			if ((System.currentTimeMillis() - c.getLastActivity()) > maxActivityDiffMillis) {
				closeConnection(c);
				it.remove();
			}
		}
	}

}
