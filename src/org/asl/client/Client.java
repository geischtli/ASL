package org.asl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;

import org.asl.common.request.Request;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;

public class Client implements Runnable {

	private final int port;
	private AsynchronousSocketChannel sc;
	private List<Request> requestList;
	private int lockCount;
	
	public Client(int port, int id, int numMessages) throws IOException {
		this.port = port;
		this.requestList = new ArrayList<Request>();
		this.lockCount = 1;
		gatherRequests();
	}
	
	public void gatherRequests() {
		requestList.add(RequestBuilder.newHandshakeRequest());
		requestList.add(RequestBuilder.newCreateQueueRequest(1));
		requestList.add(RequestBuilder.newSendMessageRequest(
				1,
				1,
				1,
				"This is my first message"
			)
		);
	}

	@Override
	public void run() {
		for (Request req : requestList) {
			while (lockCount == 0) {
				try {
					System.out.println("i wait");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lockCount--;
			// TODO: Put requests in a queue and do a while-request-has loop and put setup of requests aftter handshakemessage
			// also put lock into a single class and try to prevent sleep to happen
			System.out.println("decrease lock count to: " + lockCount);
			System.out.println("got new request");
			try {
				this.sc = AsynchronousSocketChannel.open();
				sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), null, new CompletionHandler<Void, Object>() {
		
					@Override
					public void completed(Void result, Object attachment) {
						//Request req = RequestBuilder.newCreateQueueRequest(1);
						//Request req = RequestBuilder.newHandshakeRequest();
						//Request req = RequestBuilder.newSendMessageRequest(1, 2, 1, "this is a test content");
						//Request req = RequestBuilder.newReadAllMessagesOfQueueRequest(1, 1);
						//Request req = RequestBuilder.newReadMessageFromSenderRequest(1, 1);
						//Request req = RequestBuilder.newGetQueuesWithMessagesForClientRequest(1);
						System.out.println("increased lock to: " + lockCount);
						
						ByteBuffer outbuf = ByteBuffer.wrap(SerializingUtilities.objectToByteArray(req));
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
											System.out.println("And reason: " + m.getException().getMessage());
										}
										try {
											sc.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
										lockCount++;
									}
		
									@Override
									public void failed(Throwable exc, Object attachment) {
										System.out.println("In client: Read failed");
										try {
											sc.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
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
						exc.printStackTrace();
						System.out.println("In client: Connect failed");
					}
					
				});
				System.out.println("conn finished");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
