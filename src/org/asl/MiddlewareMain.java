package org.asl;

import java.io.IOException;
import java.sql.SQLException;

import org.asl.middleware.Middleware;

public class MiddlewareMain {
	
	private static final int port = 9090;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		Middleware mw = new Middleware(port);
		System.out.println("Started server");
		mw.accept();

		// wait for enter strike such that the middleware can safely close all its services
		System.out.println("Wait for shutdown command (ENTER) ...");
		System.in.read();
		mw.shutdown();
	}
}
