package org.asl.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import org.asl.common.message.Message;
import org.asl.common.message.builder.MessageBuilder;
import org.asl.common.message.types.MessageType;

public class Client implements Runnable {

	private final int id;
	private final int numMessages;
	private final int port;
	private final String uuid;
	
	public Client(int port, int id, int numMessages) throws IOException {
		this.port = port;
		this.id = id;
		this.numMessages = numMessages;
		this.uuid = UUID.randomUUID().toString();
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), port)){
			Message msg = MessageBuilder.newHandshakeMessage();
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(msg);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Message ret = (Message) in.readObject();
			System.out.println();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Client problem " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*@Override
	public void run() {
		for (int i = 0; i < numMessages; i++) {
			try {
				socket = new Socket(InetAddress.getLoopbackAddress(), port);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Message msg = MessageBuilder.newHandshakeMessage();
			ObjectOutputStream out;
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(msg);
				out.flush();
				socket.close();
			} catch (IOException e) {
				System.out.println("Client problem " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("Client " + id + " completed run");
	}*/
}
