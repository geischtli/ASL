package org.asl.client.management.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

import org.asl.client.AbstractClient;
import org.asl.client.ClientInfo;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.TimeLogger;
import org.asl.common.timing.Timing;

public class AdminWriteCompletionHandler implements CompletionHandler<Integer, Integer> {

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	private ClientInfo ci;
	private List<RequestType> requestList;
	private int requestId;

	public AdminWriteCompletionHandler(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ClientInfo ci, List<RequestType> requestList, int requestId) {
		this.sc = sc;
		this.outbufWrap = outbufWrap;
		this.ci = ci;
		this.requestList = requestList;
		this.requestId = requestId;
	}
	
	public static AdminWriteCompletionHandler create(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ClientInfo ci, List<RequestType> requestList, int requestId) {
		TimeLogger.click(Timing.CLIENT_START_WRITE, ci.getClientId(), ci.getRequestId());
		return new AdminWriteCompletionHandler(sc, outbufWrap, ci, requestList, requestId);
	}
	
	@Override
	public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, expectedWriteBytes, sc);
		TimeLogger.click(Timing.CLIENT_END_WRITE, ci.getClientId(), ci.getRequestId());
    	ByteBuffer inbuf = ByteBuffer.allocate(AbstractClient.INITIAL_BUFSIZE);
    	sc.read(inbuf, null, AdminReadCompletionHandler.create(sc, ci, inbuf, requestList, requestId));
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
