package org.asl.middleware;

import org.asl.common.timing.TimeLogger;

public class MiddlewareInfo {

	private int middlewareId;
	private TimeLogger myTimeLogger;
	
	public MiddlewareInfo() {
		this.middlewareId = -1;
		this.myTimeLogger = null;
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
		myTimeLogger = new TimeLogger("MIDDLEWARE", getMiddlewareId());
	}
	
	public TimeLogger getMyTimeLogger() {
		return myTimeLogger;
	}
	
}
