package org.asl.middleware;

import org.asl.common.timing.TimeLogger;

public class MiddlewareInfo {

	private int middlewareId;
	private TimeLogger myTimeLogger;
	private long startTime;
	
	public MiddlewareInfo() {
		this.middlewareId = 0;
		// use this as dummy writer before initialization/registration of the middleware
		this.myTimeLogger = TimeLogger.create("MIDDLEWARE", this.middlewareId, System.nanoTime());
	}
	
	public static MiddlewareInfo create() {
		return new MiddlewareInfo();
	}
	
	public int getMiddlewareId() {
		return middlewareId;
	}
	
	public void setMiddlewareId(int middlewareId) {
		this.middlewareId = middlewareId;
	}
	
	public void initTimeLogger() {
		setStartTime(System.nanoTime());
		myTimeLogger = TimeLogger.create("MIDDLEWARE", getMiddlewareId(), getStartTime()/1000000);
	}
	
	public TimeLogger getMyTimeLogger() {
		return myTimeLogger;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
