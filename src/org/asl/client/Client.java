package org.asl.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.UUID;

import org.asl.common.message.Message;
import org.asl.common.message.builder.MessageBuilder;
import org.asl.common.message.serialize.SerializingUtilities;
import org.asl.common.message.types.MessageType;

public class Client implements Runnable {

	private final int id;
	private final int numMessages;
	private final int port;
	private final String uuid;
	private final AsynchronousSocketChannel sc;
	
	public Client(int port, int id, int numMessages) throws IOException {
		this.port = port;
		this.id = id;
		this.numMessages = numMessages;
		this.uuid = UUID.randomUUID().toString();
		this.sc = AsynchronousSocketChannel.open();
	}

	@Override
	public void run() {
		sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), null, new CompletionHandler<Void, Object>() {

			@Override
			public void completed(Void result, Object attachment) {
				Message m = MessageBuilder.newHandshakeMessage();
				ByteBuffer outbuf = ByteBuffer.wrap(SerializingUtilities.objectToByteArray(m));
				sc.write(outbuf, 0L, new CompletionHandler<Integer, Long>() {
	                
					@Override
					public void completed(final Integer result, final Long attachment) {
                    	outbuf.flip();
                    	sc.read(outbuf, null, new CompletionHandler<Integer, Object>() {

							@Override
							public void completed(Integer result, Object attachment) {
								// TODO Auto-generated method stub
								outbuf.flip();
								Message m = (Message)SerializingUtilities.byteArrayToObject(outbuf.array());
								System.out.println("Client received: " + m.getClass());
								m.processOnClient();
								System.out.println("Yaiii got my id: " + ClientInfo.getClientId());
							}

							@Override
							public void failed(Throwable exc, Object attachment) {
								// TODO Auto-generated method stub
								
							}
                    		
                    	});
					}
					
	                @Override
	                public void failed(Throwable exc, Long att) {
	                	System.out.println("failed in writing");
	                }
				});
					
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				int i = 15;
				int a = i;
			}
			
		});
	}
	
	/*@Override
	public void run() {
		try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), port)){
			Message msg = MessageBuilder.newHandshakeMessage();
			msg.processOnClient();
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(msg);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			System.out.println("passed input stream");
			Message ret = (Message) in.readObject();
			System.out.println(ret.content);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Client problem " + e.getMessage());
			e.printStackTrace();
		}
	}*/
	
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
