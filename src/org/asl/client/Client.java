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
import java.util.concurrent.TimeUnit;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timer.client.ClientTimer;
import org.asl.common.timer.client.ClientTimings;

public class Client implements Runnable {

	private final int port;
	private AsynchronousSocketChannel sc;
	private List<RequestType> requestList;
	private Semaphore lock;
	private PropertyParser propParser;
	private static int INITIAL_BUFSIZE;
	private ClientInfo ci;
	private ClientTimer timer;
	
	public Client(int port) throws IOException {
		this.port = port;
		this.requestList = new ArrayList<RequestType>();
		this.lock = new Semaphore(1, true);
		this.propParser = PropertyParser.create("config_common.xml").parse();
		Client.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.ci = ClientInfo.create();
		this.timer = ClientTimer.create();
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
		timer.setup(requestList.size());
		for (RequestType reqType : requestList) {
			try {
				// get lock, such that not 2 connects to the same socket happen
				lock.tryAcquire(1, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				System.out.println("Failed in semaphore tryAcquire with 1 second");
				e1.printStackTrace();
			}
			timer.click(ClientTimings.START_REQUEST);
			Request req = RequestBuilder.getRequest(reqType, ci);
			sc = SocketHelper.openSocket();
			sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), null, new CompletionHandler<Void, Object>() {
	
				@Override
				public void completed(Void result, Object attachment) {
					ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
					sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new CompletionHandler<Integer, Integer>() {
		                
						@Override
						public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
							SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, expectedWriteBytes, sc);
							timer.click(ClientTimings.SENT_REQUEST);
	                    	ByteBuffer inbuf = ByteBuffer.allocate(Client.INITIAL_BUFSIZE);
	                    	sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {
	
								@Override
								public void completed(Integer readBytes, Object attachment) {
									ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
									timer.click(ClientTimings.READ_ANSWER);
									Request ansReq = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
									try {
										ansReq.processOnClient(ci);
									} catch (ASLException e) {
										System.out.println("Reading message failed with type: " + ansReq.getException().getClass());
										System.out.println("And reason: " + ansReq.getException().getMessage());
									}
									timer.click(ClientTimings.PROCESSED_ANSWER);
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
		                public void failed(Throwable se, Integer expectedWriteBytes) {
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
		}
		timer.printTotalTimePerRequest();
		System.out.println("Client " + ci.getClientId() + " is done");
	}
	
}
