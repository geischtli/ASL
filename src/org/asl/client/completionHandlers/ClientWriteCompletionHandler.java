package org.asl.client.completionHandlers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

import org.asl.client.AbstractClient;
import org.asl.client.ClientInfo;
import org.asl.client.VirtualClient;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.Timing;

public class ClientWriteCompletionHandler implements CompletionHandler<Integer, Integer> {

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	private ClientInfo ci;
	private List<RequestType> requestList;
	private int requestId;
	private long startWrite;
	private long startPrepare;
	
	public ClientWriteCompletionHandler(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ClientInfo ci, List<RequestType> requestList, int requestId) {
		this.sc = sc;
		this.outbufWrap = outbufWrap;
		this.ci = ci;
		this.requestList = requestList;
		this.requestId = requestId;
		this.startWrite = System.nanoTime();
	}
	
	public static ClientWriteCompletionHandler create(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ClientInfo ci, List<RequestType> requestList, int requestId) {
		ci.getMyTimeLogger().click(Timing.CLIENT_START_WRITE, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
		return new ClientWriteCompletionHandler(sc, outbufWrap, ci, requestList, requestId);
	}
	
	@Override
	public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
		System.out.println("write completed");
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, expectedWriteBytes, sc);
		System.out.println("force write completed");
		VirtualClient.semaphore.release();
		VirtualClient.writeLog(String.valueOf(System.nanoTime() - startWrite));
		System.out.println("1st log completed");
		//ci.getMyTimeLogger().click(Timing.CLIENT_END_WRITE, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
    	//ByteBuffer inbuf = ByteBuffer.allocate(AbstractClient.INITIAL_BUFSIZE);
    	//sc.read(inbuf, null, ClientReadCompletionHandler.create(sc, ci, inbuf, requestList, requestId));
		startPrepare = System.nanoTime();
    	// Only used for load generator benchmark
		System.out.println("request = " + ci.getRequestId());
    	if (ci.getRequestId() + 1 < requestList.size()) {
    		System.out.println("1st if completed");
			if (sc.isOpen()) {
				System.out.println("2nd of completed");
				ci.incrementRequestId();
				System.out.println("increment completed");
				ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci));
				System.out.println("packing completed");
				VirtualClient.writeLog(String.valueOf(System.nanoTime() - startPrepare));
				System.out.println("2nd log completed");
				try {
					VirtualClient.semaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), ClientWriteCompletionHandler.create(sc, outbufWrap, ci, requestList, requestId));
				System.out.println("write call completed");
			} else {
				sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
						ConnectCompletionHandler.create(ci, sc, requestList, requestId)
					);
			}
		} else {
			VirtualClient.writeLog(String.valueOf((double)((System.nanoTime() - VirtualClient.startAll))/(80000.0*1000000000)));
			try {
				VirtualClient.logWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ci.getMyTimeLogger().stopMyTimeLogger();
			SocketHelper.closeSocket(sc);
			System.out.println("Client is done and closed socket");
		}
	}

	@Override
	public void failed(Throwable se, Integer attachment) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.CLIENT,
				SocketOperation.WRITE,
				se,
				sc
			);
	}

}
