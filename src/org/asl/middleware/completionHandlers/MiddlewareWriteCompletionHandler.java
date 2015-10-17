package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.Timing;
import org.asl.middleware.Middleware;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;

public class MiddlewareWriteCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper>{

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	private MiddlewareInfo mi;
	private int clientId;
	private int requestId;
	
	public MiddlewareWriteCompletionHandler(
			MiddlewareInfo mi, AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, int clientId, int requestId) {
		this.mi = mi;
		this.sc = sc;
		this.outbufWrap = outbufWrap;
		this.clientId = clientId;
		this.requestId = requestId;
	}
	
	public static MiddlewareWriteCompletionHandler create(
			MiddlewareInfo mi, AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, int clientId, int requestId) {
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_START_WRITE, clientId, requestId, mi.getStartTime());
		return new MiddlewareWriteCompletionHandler(mi, sc, outbufWrap, clientId, requestId);
	}
	
	@Override
	public void completed(Integer writtenBytes, ConnectionTimeWrapper connTimeWrapper) {
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, outbufWrap.getBytes(), sc);
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_END_WRITE, clientId, requestId, mi.getStartTime());
		ByteBuffer inbuf = ByteBuffer.allocate(Middleware.INITIAL_BUFSIZE);
		sc.read(inbuf, connTimeWrapper, MiddlewareReadCompletionHandler.create(mi, sc, inbuf, 0));
		connTimeWrapper.reset();
	}

	@Override
	public void failed(Throwable se, ConnectionTimeWrapper connTimeWrapper) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.MIDDLEWARE,
				SocketOperation.WRITE,
				se,
				sc
			);
	}

}
