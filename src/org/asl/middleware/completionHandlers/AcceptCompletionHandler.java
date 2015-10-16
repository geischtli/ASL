package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.Timing;
import org.asl.middleware.Middleware;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;
import org.asl.middleware.connectioncontrol.WatchDog;

public class AcceptCompletionHandler<V, A> implements CompletionHandler<AsynchronousSocketChannel, Integer> {

	private AsynchronousServerSocketChannel serverChannel;
	private WatchDog watchDog;
	private int requestId;
	private MiddlewareInfo mi;
	
	public AcceptCompletionHandler(MiddlewareInfo mi, AsynchronousServerSocketChannel serverChannel, WatchDog watchDog, int requestId) {
		this.mi = mi;
		this.serverChannel = serverChannel;
		this.watchDog = watchDog;
		this.requestId = requestId;
	}
	
	public static AcceptCompletionHandler<AsynchronousSocketChannel, Integer> create(
			MiddlewareInfo mi, AsynchronousServerSocketChannel serverChannel, WatchDog watchDog, int requestId) {
		return new AcceptCompletionHandler<AsynchronousSocketChannel, Integer>(mi, serverChannel, watchDog, requestId);
	}
	
	@Override
	public void completed(AsynchronousSocketChannel sc, Integer attachment) {
		ConnectionTimeWrapper c = new ConnectionTimeWrapper(sc, System.currentTimeMillis());
		watchDog.addConnection(c);
		serverChannel.accept(++requestId, this);
		ByteBuffer inbuf = ByteBuffer.allocate(Middleware.INITIAL_BUFSIZE);
		sc.read(inbuf, c, MiddlewareReadCompletionHandler.create(mi, sc, inbuf, requestId));
	}

	@Override
	public void failed(Throwable se, Integer attachment) {
		if (se.getClass() != AsynchronousCloseException.class || !Middleware.isShuttingDown) {
			SocketHelper.closeSocketAfterException(
					SocketLocation.MIDDLEWARE,
					SocketOperation.ACCEPT,
					se
				);
		}
	}

}
