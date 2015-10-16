package org.asl.middleware;

import org.asl.common.timing.TimeLogger;

public class MiddlewareInfo {

	private int middlewareId;
	private TimeLogger myTimeLogger;
	
	public MiddlewareInfo() {
		this.middlewareId = 0;
		this.myTimeLogger = TimeLogger.create("MIDDLEWARE", this.middlewareId);
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
		myTimeLogger = TimeLogger.create("MIDDLEWARE", getMiddlewareId());
	}
	
	public TimeLogger getMyTimeLogger() {
		return myTimeLogger;
	}
	
}
