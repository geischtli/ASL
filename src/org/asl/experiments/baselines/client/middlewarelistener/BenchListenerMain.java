//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.experiments.baselines.client.middlewarelistener;

import java.io.IOException;

public class BenchListenerMain {

	public static void main(String[] args) {
		BenchListener bl = null;
		try {
			bl = new BenchListener();
			bl.go();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.in.read();
			bl.shutdown();
			System.out.println("Middleware saw " + bl.numRequests.get() + " requests");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
