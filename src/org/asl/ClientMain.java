package org.asl;

import java.io.IOException;

import org.asl.client.Client;

public class ClientMain {
	private final static int port = 9090;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Thread t = new Thread(new Client(port, -1, 1));
		t.start();
		Thread.sleep(1000*120);
	}
}
