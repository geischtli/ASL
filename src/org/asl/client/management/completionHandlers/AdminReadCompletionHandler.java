package org.asl.client.management.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.client.management.AdminClient;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.TimeLogger;
import org.asl.common.timing.Timing;

public class AdminReadCompletionHandler implements CompletionHandler<Integer, Object> {

	private AsynchronousSocketChannel sc;
	private ClientInfo ci;
	ByteBuffer inbuf;
	private List<RequestType> requestList;
	private int requestId;
	
	public AdminReadCompletionHandler(AsynchronousSocketChannel sc, ClientInfo ci, ByteBuffer inbuf, List<RequestType> requestList, int requestId) {
		this.sc = sc;
		this.ci = ci;
		this.inbuf = inbuf;
		this.requestList = requestList;
		this.requestId = requestId;
	}
	
	public static AdminReadCompletionHandler create(AsynchronousSocketChannel sc, ClientInfo ci, ByteBuffer inbuf, List<RequestType> requestList, int requestId) {
		ci.getMyTimeLogger().click(Timing.CLIENT_START_READ, ci.getClientId(), ci.getRequestId());
		return new AdminReadCompletionHandler(sc, ci, inbuf, requestList, requestId);
	}
	
	@Override
	public void completed(Integer readBytes, Object attachment) {
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
		ci.getMyTimeLogger().click(Timing.CLIENT_END_READ, ci.getClientId(), ci.getRequestId());
		
		if (fullInbufWrap == null || readBytes == -1) {
			return;
		}

		ci.getMyTimeLogger().click(Timing.CLIENT_START_POSTPROCESSING, ci.getClientId(), ci.getRequestId());
		Request ansReq = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
		try {
			ansReq.processOnClient(ci);
		} catch (ASLException e) {
			System.out.println("Reading message failed with type: " + ansReq.getException().getClass());
			System.out.println("And reason: " + ansReq.getException().getMessage());
		}
		AdminClient.semaphore.release();
		ci.getMyTimeLogger().click(Timing.CLIENT_END_POSTPROCESSING, ci.getClientId(), ci.getRequestId());
		SocketHelper.closeSocket(sc);
		System.out.println("Client is done and closed socket");
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
