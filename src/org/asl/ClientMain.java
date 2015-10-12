package org.asl;

import java.io.IOException;

import org.asl.client.VirtualClient;

public class ClientMain {
	private final static int port = 9090;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Thread t = new Thread(new VirtualClient(port));
		t.start();
		Thread.sleep(1000*600);
	}
}
