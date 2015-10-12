package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.middleware.Middleware;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;

public class MiddlewareWriteCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper>{

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	
	public MiddlewareWriteCompletionHandler(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap) {
		this.sc = sc;
		this.outbufWrap = outbufWrap;
	}
	
	public static MiddlewareWriteCompletionHandler create(
			AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap) {
		return new MiddlewareWriteCompletionHandler(sc, outbufWrap);
	}
	
	@Override
	public void completed(Integer writtenBytes, ConnectionTimeWrapper connTimeWrapper) {
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, outbufWrap.getBytes(), sc);
//		timer.click(MiddlewareTimings.WROTE_ANSWER, requestId);
		ByteBuffer inbuf = ByteBuffer.allocate(Middleware.INITIAL_BUFSIZE);
		sc.read(inbuf, connTimeWrapper, MiddlewareReadCompletionHandler.create(sc, inbuf, 313L, 0));
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
