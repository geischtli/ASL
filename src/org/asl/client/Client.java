package org.asl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;

public class Client implements Runnable {

	private final int port;
	private AsynchronousSocketChannel sc;
	private List<RequestType> requestList;
	private Semaphore lock;
	
	public Client(int port, int id, int numMessages) throws IOException {
		this.port = port;
		this.requestList = new ArrayList<RequestType>();
		this.lock = new Semaphore(1, true);
		gatherRequests();
	}
	
	public void gatherRequests() {
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[]{
						RequestType.HANDSHAKE,
						RequestType.CREATE_QUEUE,
						},
				1
				);
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[]{
						RequestType.GET_REGISTERED_CLIENTS,
						RequestType.SEND_MESSAGE
						},
				100
				);
	}

	@Override
	public void run() {
		for (RequestType reqType : requestList) {
			try {
				// get lock, such that not 2 connects to the same socket happen
				lock.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Request req = RequestBuilder.getRequest(reqType);
			try {
				this.sc = AsynchronousSocketChannel.open();
				sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), null, new CompletionHandler<Void, Object>() {
		
					@Override
					public void completed(Void result, Object attachment) {
						
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
										//System.out.println("Client received: " + m.getClass());
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
										lock.release();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
