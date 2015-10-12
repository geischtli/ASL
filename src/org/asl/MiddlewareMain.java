package org.asl;

import java.io.IOException;
import java.sql.SQLException;

import org.asl.middleware.Middleware;

public class MiddlewareMain {
	
	private static final int port = 9090;
	private static final int sleepSeconds = 120;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		Middleware mw = new Middleware(port);
		System.out.println("Started server");
		mw.accept();
		Thread.sleep(sleepSeconds * 1000);
	}
}
