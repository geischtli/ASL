package org.asl.middleware.clientsession;

import java.nio.channels.AsynchronousSocketChannel;

public class ClientSession extends BaseClientSession {
	private final AsynchronousSocketChannel ssc;
	private final Integer client_id;
	
	public ClientSession(AsynchronousSocketChannel ssc, Integer client_id) {
		this.ssc = ssc;
		this.client_id = client_id;
	}
	
	public AsynchronousSocketChannel getChannel() {
		return ssc;
	}
}