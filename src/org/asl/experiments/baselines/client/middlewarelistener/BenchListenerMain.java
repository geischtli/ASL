package org.asl.experiments.baselines.client.middlewarelistener;

import java.io.IOException;

public class BenchListenerMain {

	public static void main(String[] args) {
		try {
			BenchListener bl = new BenchListener();
			bl.go();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
