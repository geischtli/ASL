package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;

public class WriteCompletionHandler<V, A> implements CompletionHandler<Integer, ReadCompletionHandler<Integer, Object>>{

	private AsynchronousSocketChannel sc;
	private ByteBufferWrapper outbufWrap;
	private ByteBuffer inbuf;
	private ReadCompletionHandler<Integer, Object> readHandler;
	
	public WriteCompletionHandler(AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap,
			ByteBuffer inbuf, ReadCompletionHandler<Integer, Object> readHandler) {
		this.sc = sc;
		this.outbufWrap = outbufWrap;
		this.inbuf = inbuf;
		this.readHandler = readHandler;
	}
	
	public static WriteCompletionHandler<Integer, ReadCompletionHandler<Integer, Object>> create(
			AsynchronousSocketChannel sc, ByteBufferWrapper outbufWrap, ByteBuffer inbuf, ReadCompletionHandler<Integer, Object> readHandler) {
		return new WriteCompletionHandler<Integer, ReadCompletionHandler<Integer, Object>>(sc, outbufWrap, inbuf, readHandler);
	}
	
	@Override
	public void completed(Integer writtenBytes, ReadCompletionHandler<Integer, Object> attachment) {
		SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, outbufWrap.getBytes(), sc);
//		timer.click(MiddlewareTimings.WROTE_ANSWER, requestId);
		inbuf.flip();
		sc.read(inbuf, null, readHandler);
	}

	@Override
	public void failed(Throwable se, ReadCompletionHandler<Integer, Object> attachment) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.MIDDLEWARE,
				SocketOperation.WRITE,
				se,
				sc
			);
	}

}
