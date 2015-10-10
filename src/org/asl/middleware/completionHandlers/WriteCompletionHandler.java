package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;

public class WriteCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper>{

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	private ByteBuffer inbuf;
	
	public WriteCompletionHandler(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ByteBuffer inbuf) {
		this.sc = sc;
		this.outbufWrap = outbufWrap;
		this.inbuf = inbuf;
	}
	
	public static WriteCompletionHandler create(
			AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ByteBuffer inbuf) {
		return new WriteCompletionHandler(sc, outbufWrap, inbuf);
	}
	
	@Override
	public void completed(Integer writtenBytes, ConnectionTimeWrapper connTimeWrapper) {
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, outbufWrap.getBytes(), sc);
//		timer.click(MiddlewareTimings.WROTE_ANSWER, requestId);
		inbuf.flip();
		sc.read(inbuf, connTimeWrapper, ReadCompletionHandler.create(sc, inbuf, null, 313));
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
