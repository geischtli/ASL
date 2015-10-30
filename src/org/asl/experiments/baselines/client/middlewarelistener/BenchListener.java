package org.asl.experiments.baselines.client.middlewarelistener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.asl.common.request.serialize.SerializingUtilities;

public class BenchListener {


	private ExecutorService cachedExecutor;
	private AsynchronousChannelGroup acg;
	private final AsynchronousServerSocketChannel serverChannel;
	private int port = 9090;
	public AtomicInteger numRequests;
	private int goneClients;
	
	public BenchListener() throws IOException {
		cachedExecutor = Executors.newCachedThreadPool();
		acg = AsynchronousChannelGroup.withCachedThreadPool(cachedExecutor, 100);
		this.serverChannel = AsynchronousServerSocketChannel.open(acg);
		this.serverChannel.bind(new InetSocketAddress(port));
		this.numRequests = new AtomicInteger(0);
		this.goneClients = 1;
	}
	
	public void go() {
		System.out.println("Server started");
		serverChannel.accept(null, new AcceptCompletionHandler());
	}
	
	private final class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {
		@Override
		public void completed(AsynchronousSocketChannel sc, Object attachment) {
			System.out.println("accepted a client");
			serverChannel.accept(null, this);
			final ByteBuffer inbuf = ByteBuffer.allocate(10240);
			sc.read(inbuf, null, new ReadCompletionHandler(sc, inbuf));
		}

		@Override
		public void failed(Throwable exc, Object attachment) {
			System.out.println("accept failed");
		}
	}

	private final class ReadCompletionHandler implements CompletionHandler<Integer, Void> {
		private AsynchronousSocketChannel sc;
		private ByteBuffer inbuf;

		private ReadCompletionHandler(AsynchronousSocketChannel sc, ByteBuffer inbuf) {
			this.sc = sc;
			this.inbuf = inbuf;
		}

		@Override
		public void completed(Integer readBytes, Void attachment) {
			int expectedBytes = SerializingUtilities.unpackLength(inbuf);
			while (!readBytes.equals(expectedBytes + 4)) {
				try {
					readBytes += sc.read(inbuf).get();
				} catch (InterruptedException | ExecutionException e) {
					if (e instanceof ExecutionException) {
						System.out.println("client number " + goneClients++ + " gone");
						return;
					} else {
						e.printStackTrace();
					}
				}
			}
			numRequests.incrementAndGet();
			try {
				sc.write(ByteBuffer.wrap("0".getBytes())).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			ByteBuffer newinbuf = ByteBuffer.allocate(10240);
			sc.read(newinbuf, null, new ReadCompletionHandler(sc, newinbuf));
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			System.out.println("client number " + goneClients++ + " gone");
		}
	}

}
