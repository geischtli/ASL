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
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;

public class MiddlewareReadCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper> {

	private AsynchronousSocketChannel sc;
	private ByteBuffer inbuf;
	private TimeLogger timer;
	private int requestId;
	
	public MiddlewareReadCompletionHandler(AsynchronousSocketChannel sc, ByteBuffer inbuf, TimeLogger timer, int requestId) {
		this.sc = sc;
		this.inbuf = inbuf;
		this.timer = timer;
		this.requestId = requestId;
	}
	
	public static MiddlewareReadCompletionHandler create(AsynchronousSocketChannel sc, ByteBuffer inbuf, TimeLogger timer, int requestId) {
		return new MiddlewareReadCompletionHandler(sc, inbuf, timer, requestId);
	}
	
	@Override
	public void completed(Integer readBytes, ConnectionTimeWrapper connTimeWrapper) {
		connTimeWrapper.reset();
		//System.out.println("middleware read " + readBytes + " bytes");
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, (int)readBytes, sc);
		
		if (fullInbufWrap == null || readBytes == -1) {
			System.out.println("Got null buffer or read -1 bytes, i return");
			return;
		}
		
//		timer.click(MiddlewareTimings.READ_REQUEST, requestId);
		Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
//		timer.click(MiddlewareTimings.PROCESSED_READ, requestId);
		
		req.processOnMiddleware(timer, requestId);
		
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
//		timer.click(MiddlewareTimings.PACKED_REQUEST, requestId);
		
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
