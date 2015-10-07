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

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;

public class Client implements Runnable {

	private final int port;
	private AsynchronousSocketChannel sc;
	private List<RequestType> requestList;
	private Semaphore lock;
	private PropertyParser propParser;
	private static int INITIAL_BUFSIZE;
	private SerializingUtilities serUtil;
	
	
	public Client(int port) throws IOException {
		this.port = port;
		this.requestList = new ArrayList<RequestType>();
		this.lock = new Semaphore(1, true);
		this.propParser = PropertyParser.create("config_common.xml").parse();
		Client.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.serUtil = SerializingUtilities.create();
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
				200
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
						ByteBuffer outbuf = serUtil.packRequest(req);
						sc.write(outbuf, null, new CompletionHandler<Integer, Object>() {
			                
							@Override
							public void completed(Integer writtenBytes, final Object attachment) {
								serUtil.forceFurtherWriteIfNeeded(outbuf, writtenBytes, sc);
		                    	ByteBuffer inbuf = ByteBuffer.allocate(Client.INITIAL_BUFSIZE);
		                    	sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {
		
									@Override
									public void completed(Integer readBytes, Object attachment) {
										ByteBuffer fullInbuf = serUtil.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
										Request ansReq = serUtil.unpackRequest(fullInbuf);
										try {
											ansReq.processOnClient();
										} catch (ASLException e) {
											System.out.println("Reading message failed with type: " + ansReq.getException().getClass());
											System.out.println("And reason: " + ansReq.getException().getMessage());
										}
										SocketHelper.closeSocket(sc);
										lock.release();
									}
		
									@Override
									public void failed(Throwable se, Object attachment) {
										SocketHelper.closeSocketAfterException(
												SocketLocation.CLIENT,
												SocketOperation.READ,
												se,
												sc
											);
									}
		                    		
		                    	});
							}
							
			                @Override
			                public void failed(Throwable se, Object att) {
			                	SocketHelper.closeSocketAfterException(
										SocketLocation.CLIENT,
										SocketOperation.WRITE,
										se,
										sc
									);
			                }
			                
						});
					}
		
					@Override
					public void failed(Throwable se, Object attachment) {
						SocketHelper.closeSocketAfterException(
								SocketLocation.CLIENT,
								SocketOperation.CONNECT,
								se,
								sc
							);
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Client " + ClientInfo.getClientId() + " is done");
	}
	
}
