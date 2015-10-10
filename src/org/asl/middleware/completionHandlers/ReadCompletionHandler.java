package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.request.Request;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.ASLTimer;

public class ReadCompletionHandler<V, A> implements CompletionHandler<Integer, Object> {

	private AsynchronousSocketChannel sc;
	private ByteBuffer inbuf;
	private ASLTimer timer;
	private int requestId;
	
	public ReadCompletionHandler(AsynchronousSocketChannel sc, ByteBuffer inbuf, ASLTimer timer, int requestId) {
		this.sc = sc;
		this.inbuf = inbuf;
		this.timer = timer;
		this.requestId = requestId;
	}
	
	public static ReadCompletionHandler<Integer, Object> create(AsynchronousSocketChannel sc, ByteBuffer inbuf, ASLTimer timer, int requestId) {
		return new ReadCompletionHandler<Integer, Object>(sc, inbuf, timer, requestId);
	}
	
	@Override
	public void completed(Integer readBytes, Object attachment) {
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, (int)readBytes, sc);
		
//		timer.click(MiddlewareTimings.READ_REQUEST, requestId);
		Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
//		timer.click(MiddlewareTimings.PROCESSED_READ, requestId);
		
		req.processOnMiddleware(timer, requestId);
		
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
//		timer.click(MiddlewareTimings.PACKED_REQUEST, requestId);
		
		sc.write(
				outbufWrap.getBuf(),
				null,
				WriteCompletionHandler.create(sc, outbufWrap, inbuf, (ReadCompletionHandler<Integer, Object>) this)
			);
	}

	@Override
	public void failed(Throwable se, Object attachment) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.MIDDLEWARE,
				SocketOperation.READ,
				se,
				sc
			);
	}

}
