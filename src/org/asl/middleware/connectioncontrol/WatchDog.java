package org.asl.middleware.connectioncontrol;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class WatchDog extends TimerTask {

	private List<ConnectionTimeWrapper> connections;
	private long maxActivityDiffNano;
	
	public WatchDog(long maxActivityDiffSec) {
		this.connections = new LinkedList<ConnectionTimeWrapper>();
		this.maxActivityDiffNano = maxActivityDiffSec*1000000000L;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//System.out.println("Dog barks");
		for (Iterator<ConnectionTimeWrapper> it = connections.iterator(); it.hasNext();) {
			ConnectionTimeWrapper c = it.next();
			if ((System.nanoTime() - c.getLastActivity()) > maxActivityDiffNano) {
				closeConnection(c);
				it.remove();
			}
		}
	}

}
