//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

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
	private long startRtt;
	
	public ClientReadCompletionHandler(AsynchronousSocketChannel sc, ClientInfo ci, ByteBuffer inbuf,
			List<RequestType> requestList, int requestId, long startRtt) {
		this.sc = sc;
		this.ci = ci;
		this.inbuf = inbuf;
		this.requestList = requestList;
		this.requestId = requestId;
		this.startRtt = startRtt;
	}
	
	public static ClientReadCompletionHandler create(AsynchronousSocketChannel sc, ClientInfo ci,
			ByteBuffer inbuf, List<RequestType> requestList, int requestId, long startRtt) {
		ci.getMyTimeLogger().click(Timing.CLIENT_START_READ, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
		return new ClientReadCompletionHandler(sc, ci, inbuf, requestList, requestId, startRtt);
	}
	
	@Override
	public void completed(Integer readBytes, Object attachment) {
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
		ci.getMyTimeLogger().click(Timing.CLIENT_END_READ, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
		
		if (fullInbufWrap == null || readBytes == -1) {
			return;
		}

		ci.getMyTimeLogger().click(Timing.CLIENT_START_POSTPROCESSING, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
		Request ansReq = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
		try {
			ansReq.processOnClient(ci);
		} catch (ASLException e) {
			System.out.println("Reading message failed with type: " + ansReq.getException().getClass());
			System.out.println("And reason: " + ansReq.getException().getMessage());
		}
		ci.reqPerSec.incrementAndGet();
		ci.rttPerSec.addAndGet(System.currentTimeMillis() - startRtt);
		ci.getMyTimeLogger().click(Timing.CLIENT_END_POSTPROCESSING, ci.getClientId(), ci.getRequestId(), ci.getStartTimeNS());
		if (ci.getRequestId() + 1 >= requestList.size()
				&& (System.nanoTime() - ci.getStartTimeNS())/1000000000 < AbstractClient.DURATION_SEC) {
			ci.setRequestId(2);
		}
			
		long startThinkTime = System.nanoTime();
		if (ci.getRequestId() + 1 < requestList.size()
				&& (System.nanoTime() - ci.getStartTimeNS())/1000000000 < AbstractClient.DURATION_SEC) {
			if (sc.isOpen()) {
				ci.incrementRequestId();
				ci.thinkTimePerSec.addAndGet(System.nanoTime() - startThinkTime);
				ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci));
				sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), ClientWriteCompletionHandler.create(sc, outbufWrap, ci, requestList, requestId));
			} else {
				sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
						ConnectCompletionHandler.create(ci, sc, requestList, requestId)
					);
			}
		} else {
			ci.getMyTimeLogger().stopMyTimeLogger();
			ci.closeLoggers();
			SocketHelper.closeSocket(sc);
			int secsRunned = (int) ((System.nanoTime() - ci.getStartTimeNS())/1000000000);
			System.out.println("Client is done and closed socket after " + secsRunned + " seconds");
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
