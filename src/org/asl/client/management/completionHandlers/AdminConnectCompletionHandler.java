package org.asl.client.management.completionHandlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.TimeLogger;
import org.asl.common.timing.Timing;

public class AdminConnectCompletionHandler implements CompletionHandler<Void, Object> {

	private AsynchronousSocketChannel sc;
	private ClientInfo ci;
	private List<RequestType> requestList;
	private int requestId;
	
	public AdminConnectCompletionHandler(ClientInfo ci, AsynchronousSocketChannel sc, List<RequestType> requestList, int requestId) {
		this.ci = ci;
		this.sc = sc;
		this.requestList = requestList;
		this.requestId = requestId;
	}
	
	public static AdminConnectCompletionHandler create(ClientInfo ci, AsynchronousSocketChannel sc, List<RequestType> requestList, int requestId) {
		TimeLogger.click(Timing.CLIENT_START_CONNECT, ci.getClientId(), ci.getRequestId());
		return new AdminConnectCompletionHandler(ci, sc, requestList, ci.getRequestId());
	}
	
	@Override
	public void completed(Void result, Object attachment) {
		TimeLogger.click(Timing.CLIENT_END_CONNECT, ci.getClientId(), ci.getRequestId());
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(0), ci));
		sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), AdminWriteCompletionHandler.create(sc, outbufWrap, ci, requestList, requestId));
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

}
