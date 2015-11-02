package org.asl.middleware.completionHandlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.Request;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.Timing;
import org.asl.middleware.Middleware;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;

public class MiddlewareReadCompletionHandler implements CompletionHandler<Integer, ConnectionTimeWrapper> {

	private AsynchronousSocketChannel sc;
	private ByteBuffer inbuf;
	private long MIDDLEWARE_START_READ;
	private MiddlewareInfo mi;
	// used for middleware benchmark
	private long rttStart;
	
	public MiddlewareReadCompletionHandler(MiddlewareInfo mi,
			AsynchronousSocketChannel sc, ByteBuffer inbuf, long MIDDLEWARE_START_READ, int requestId, long rttStart) {
		this.mi = mi;
		this.sc = sc;
		this.inbuf = inbuf;
		this.MIDDLEWARE_START_READ = MIDDLEWARE_START_READ;
		this.rttStart = rttStart;
	}
	
	public static MiddlewareReadCompletionHandler create(MiddlewareInfo mi, AsynchronousSocketChannel sc, ByteBuffer inbuf, int requestId) {
		long MIDDLEWARE_START_READ = System.nanoTime();
		long rttStart = System.nanoTime();
		return new MiddlewareReadCompletionHandler(mi, sc, inbuf, MIDDLEWARE_START_READ, requestId, rttStart);
	}
	
	@Override
	public void completed(Integer readBytes, ConnectionTimeWrapper connTimeWrapper) {
		connTimeWrapper.reset();
		ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, (int)readBytes, sc);
		long MIDDLEWARE_END_READ = System.nanoTime();
		
		if (fullInbufWrap == null || readBytes == -1) {
			//System.out.println("Got null buffer or read -1 bytes, i return");
			System.out.println("Middleware sees client go. Totally " + Middleware.numClientsGone.incrementAndGet() + " clients gone.");
			return;
		}
		
		Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
		
		mi.getMyTimeLogger().setClick(Timing.MIDDLEWARE_START_READ, MIDDLEWARE_START_READ, req.getClientId(), req.getRequestId(), mi.getStartTime());
		mi.getMyTimeLogger().setClick(Timing.MIDDLEWARE_END_READ, MIDDLEWARE_END_READ, req.getClientId(), req.getRequestId(), mi.getStartTime());
		
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_START_PROCESSING, req.getClientId(), req.getRequestId(), mi.getStartTime());
		//req.processOnMiddleware(mi);
		// ONLY USED FOR MW BENCHMARK
		// BECAUSE DB CONN POOL ACCESS IS THE CRITICAL BIT ONLY DO THIS HERE LOCALY
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
		} catch (IOException | InterruptedException | ExecutionException | SQLException e) {
			e.printStackTrace();
		}
		mi.getMyTimeLogger().click(Timing.MIDDLEWARE_END_PROCESSING, req.getClientId(), req.getRequestId(), mi.getStartTime());
		
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
		
		sc.write(
				outbufWrap.getBuf(),
				connTimeWrapper,
				MiddlewareWriteCompletionHandler.create(
						mi, sc, outbufWrap, req.getClientId(), req.getRequestId(), rttStart)
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
