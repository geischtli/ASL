package org.asl.client.completionHandlers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

import org.asl.client.AbstractClient;
import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.Timing;

public class ClientReadCompletionHandler implements CompletionHandler<Integer, Object> {

	private AsynchronousSocketChannel sc;
	private ClientInfo ci;
	ByteBuffer inbuf;
	private List<RequestType> requestList;
	private int requestId;
	
	public ClientReadCompletionHandler(AsynchronousSocketChannel sc, ClientInfo ci, ByteBuffer inbuf, List<RequestType> requestList, int requestId) {
		this.sc = sc;
		this.ci = ci;
		this.inbuf = inbuf;
		this.requestList = requestList;
		this.requestId = requestId;
	}
	
	public static ClientReadCompletionHandler create(AsynchronousSocketChannel sc, ClientInfo ci, ByteBuffer inbuf, List<RequestType> requestList, int requestId) {
		ci.getMyTimeLogger().click(Timing.CLIENT_START_READ, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
		return new ClientReadCompletionHandler(sc, ci, inbuf, requestList, requestId);
	}
	
	@Override
	public void completed(Integer readBytes, Object attachment) {
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
		ci.getMyTimeLogger().click(Timing.CLIENT_END_READ, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
		
		if (fullInbufWrap == null || readBytes == -1) {
			return;
		}

		ci.getMyTimeLogger().click(Timing.CLIENT_START_POSTPROCESSING, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
		Request ansReq = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
		try {
			ansReq.processOnClient(ci);
		} catch (ASLException e) {
			System.out.println("Reading message failed with type: " + ansReq.getException().getClass());
			System.out.println("And reason: " + ansReq.getException().getMessage());
		}
		ci.getMyTimeLogger().click(Timing.CLIENT_END_POSTPROCESSING, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
		if (ci.getRequestId() + 1 < requestList.size()) {
			if (sc.isOpen()) {
				ci.incrementRequestId();
				ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci));
				sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), ClientWriteCompletionHandler.create(sc, outbufWrap, ci, requestList, requestId));
			} else {
				sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
						ConnectCompletionHandler.create(ci, sc, requestList, requestId)
					);
			}
		} else {
			ci.getMyTimeLogger().stopMyTimeLogger();
			SocketHelper.closeSocket(sc);
			System.out.println("Client is done and closed socket");
		}
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

}
