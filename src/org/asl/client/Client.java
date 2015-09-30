package org.asl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.Request;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;

public class Client implements Runnable {

	private final int port;
	private final AsynchronousSocketChannel sc;
	
	public Client(int port, int id, int numMessages) throws IOException {
		this.port = port;
		this.sc = AsynchronousSocketChannel.open();
	}

	@Override
	public void run() {
		sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), null, new CompletionHandler<Void, Object>() {

			@Override
			public void completed(Void result, Object attachment) {
				Request m = RequestBuilder.newHandshakeRequest();
				ByteBuffer outbuf = ByteBuffer.wrap(SerializingUtilities.objectToByteArray(m));
				sc.write(outbuf, 0L, new CompletionHandler<Integer, Long>() {
	                
					@Override
					public void completed(final Integer result, final Long attachment) {
                    	ByteBuffer inbuf = ByteBuffer.allocate(10240);
                    	sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {

							@Override
							public void completed(Integer result, Object attachment) {
								inbuf.flip();
								Request m = (Request)SerializingUtilities.byteArrayToObject(inbuf.array());
								System.out.println("Client received: " + m.getClass());
								try {
									m.processOnClient();
								} catch (ASLException e) {
									System.out.println("Reading message failed with type: " + m.getException().getClass());
								}
							}

							@Override
							public void failed(Throwable exc, Object attachment) {
								System.out.println("In client: Read failed");
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
				System.out.println("In client: Connect failed");
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
