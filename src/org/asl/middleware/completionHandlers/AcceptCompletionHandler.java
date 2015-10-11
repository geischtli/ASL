package org.asl.middleware.completionHandlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.AbstractMiddleware;
import org.asl.middleware.connectioncontrol.ConnectionTimeWrapper;
import org.asl.middleware.connectioncontrol.WatchDog;

public class AcceptCompletionHandler<V, A> implements CompletionHandler<AsynchronousSocketChannel, Integer> {

	private AsynchronousServerSocketChannel serverChannel;
	private WatchDog watchDog;
	private TimeLogger timer;
	private int requestId;
	
	public AcceptCompletionHandler(AsynchronousServerSocketChannel serverChannel, WatchDog watchDog, TimeLogger timer, int requestId) {
		this.serverChannel = serverChannel;
		this.watchDog = watchDog;
		this.timer = timer;
		this.requestId = requestId;
	}
	
	public static AcceptCompletionHandler<AsynchronousSocketChannel, Integer> create(
			AsynchronousServerSocketChannel serverChannel, WatchDog watchDog, TimeLogger timer, int requestId) {
		return new AcceptCompletionHandler<AsynchronousSocketChannel, Integer>(serverChannel, watchDog, timer, requestId);
	}
	
	@Override
	public void completed(AsynchronousSocketChannel sc, Integer attachment) {
		ConnectionTimeWrapper c = new ConnectionTimeWrapper(sc, System.currentTimeMillis());
		watchDog.addConnection(c);
		//timer.click(MiddlewareTimings.ACCEPTED_CLIENT, requestId);
		//accept();
		serverChannel.accept(++requestId, this);
		ByteBuffer inbuf = ByteBuffer.allocate(AbstractMiddleware.INITIAL_BUFSIZE);
		sc.read(inbuf, c, ReadCompletionHandler.create(sc, inbuf, timer, requestId));
	}

	@Override
	public void failed(Throwable se, Integer attachment) {
		SocketHelper.closeSocketAfterException(
				SocketLocation.MIDDLEWARE,
				SocketOperation.ACCEPT,
				se
			);
	}

}
