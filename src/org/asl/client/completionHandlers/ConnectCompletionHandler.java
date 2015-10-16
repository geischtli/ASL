package org.asl.client.completionHandlers;

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

public class ConnectCompletionHandler implements CompletionHandler<Void, Object> {

	private AsynchronousSocketChannel sc;
	private ClientInfo ci;
	private List<RequestType> requestList;
	private int requestId;
	
	public ConnectCompletionHandler(ClientInfo ci, AsynchronousSocketChannel sc, List<RequestType> requestList, int requestId) {
		this.ci = ci;
		this.sc = sc;
		this.requestList = requestList;
		this.requestId = requestId;
	}
	
	public static ConnectCompletionHandler create(ClientInfo ci, AsynchronousSocketChannel sc, List<RequestType> requestList, int requestId) {
		return new ConnectCompletionHandler(ci, sc, requestList, ci.getRequestId());
	}
	
	@Override
	public void completed(Void result, Object attachment) {
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci));
		sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), ClientWriteCompletionHandler.create(sc, outbufWrap, ci, requestList, requestId));
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
