package org.asl.middleware;

public class MiddlewareInfo {

	private static int middleware_id;
	
	public static int getMiddlewareId() {
		return middleware_id;
	}
	
	public static void setMiddlewareId(int middleware_id) {
		MiddlewareInfo.middleware_id = middleware_id;
	}
}
