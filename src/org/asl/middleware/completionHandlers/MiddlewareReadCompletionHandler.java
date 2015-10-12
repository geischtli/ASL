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
import org.asl.common.timing.TimeLogger;
import org.asl.common.timing.Timing;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;

public class MiddlewareReadCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper> {

	private AsynchronousSocketChannel sc;
	private ByteBuffer inbuf;
	private int requestId;
	private long MIDDLEWARE_START_READ;
	
	public MiddlewareReadCompletionHandler(AsynchronousSocketChannel sc, ByteBuffer inbuf, long MIDDLEWARE_START_READ, int requestId) {
		this.sc = sc;
		this.inbuf = inbuf;
		this.MIDDLEWARE_START_READ = MIDDLEWARE_START_READ;
		this.requestId = requestId;
	}
	
	public static MiddlewareReadCompletionHandler create(AsynchronousSocketChannel sc, ByteBuffer inbuf, long MIDDLEWARE_START_READ, int requestId) {
		return new MiddlewareReadCompletionHandler(sc, inbuf, MIDDLEWARE_START_READ, requestId);
	}
	
	@Override
	public void completed(Integer readBytes, ConnectionTimeWrapper connTimeWrapper) {
		connTimeWrapper.reset();
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, (int)readBytes, sc);
		long MIDDLEWARE_END_READ = System.nanoTime();
		
		if (fullInbufWrap == null || readBytes == -1) {
			System.out.println("Got null buffer or read -1 bytes, i return");
			return;
		}
		
		Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
		
		TimeLogger.setClick(Timing.MIDDLEWARE_START_READ, MIDDLEWARE_START_READ, req.getClientId(), req.getRequestId());
		TimeLogger.setClick(Timing.MIDDLEWARE_END_READ, MIDDLEWARE_END_READ, req.getClientId(), req.getRequestId());
		
		TimeLogger.click(Timing.MIDDLEWARE_START_PROCESSING, req.getClientId(), req.getRequestId());
		req.processOnMiddleware();
		TimeLogger.click(Timing.MIDDLEWARE_END_PROCESSING, req.getClientId(), req.getRequestId());
		
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
		
		sc.write(
				outbufWrap.getBuf(),
				connTimeWrapper,
				MiddlewareWriteCompletionHandler.create(sc, outbufWrap)
			);
	}

	@Override
	public void failed(Throwable se, ConnectionTimeWrapper connTimeWrapper) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.MIDDLEWARE,
				SocketOperation.READ,
				se,
				sc
			);
	}

}
