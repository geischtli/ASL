package org.asl.common.locking;

import java.util.concurrent.Semaphore;

public class Locker {

	private Semaphore lock;
	
	public Locker(int initialCount, boolean fair) {
		this.lock = new Semaphore(initialCount, fair);
	}
	
	public static Locker create(int initialCount, boolean fair) {
		return new Locker(initialCount, fair);
	}
	
	public void acquireLock() {
		try {
			lock.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void releaseLock() {
		lock.release();
	}
	
}
