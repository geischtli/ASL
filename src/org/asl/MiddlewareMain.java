//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////
/*
 * This is the main method for all middlewares
 * The ant script calls this main method in order to 
 * start up a middleware
 */
package org.asl;

import java.io.IOException;
import java.sql.SQLException;

import org.asl.middleware.Middleware;

public class MiddlewareMain {
	
	private static final int port = 9090;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		int numDBConns = Integer.parseInt(args[0]);
		Middleware mw = new Middleware(port, numDBConns);
		System.out.println("Started server");
		mw.accept();

		// wait for enter strike such that the middleware can safely close all its services
		System.out.println("Wait for shutdown command (ENTER) ...");
		System.in.read();
		mw.shutdown();
	}
}
