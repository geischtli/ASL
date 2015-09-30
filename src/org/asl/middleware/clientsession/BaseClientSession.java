package org.asl.middleware.clientsession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import org.asl.common.request.Request;

public class BaseClientSession {
	private static final AtomicInteger clientCount = new AtomicInteger(1);
	private final int id = clientCount.getAndIncrement();
	private Request req;
	
	public int getId() {
		return id;
	}
	
	public Request getRequest() {
		return req;
	}
	
	public boolean handleInput(ByteBuffer buf, int len) {
		ByteArrayInputStream is = new ByteArrayInputStream(buf.array());
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			req = (Request) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void handleFailure() {
		System.out.println("Failed");
	}
	
	@Override
	public String toString() {
		return "Session of client " + id;
	}
}