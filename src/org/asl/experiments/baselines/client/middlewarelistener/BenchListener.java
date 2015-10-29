package org.asl.experiments.baselines.client.middlewarelistener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.middleware.Middleware;

public class BenchListener {

	private ExecutorService cachedExecutor;
	private AsynchronousChannelGroup acg;
	private final AsynchronousServerSocketChannel serverChannel;
	private int port = 9090;
	
	public BenchListener() throws IOException {
		cachedExecutor = Executors.newCachedThreadPool();
		acg = AsynchronousChannelGroup.withCachedThreadPool(cachedExecutor, 100);
		this.serverChannel = AsynchronousServerSocketChannel.open(acg);
		this.serverChannel.bind(new InetSocketAddress(port));
	}
	
	public void go() {
		System.out.println("Server started");
		try {
			while(true) {
				AsynchronousSocketChannel sc = serverChannel.accept().get();
				Thread t = new Thread(new Reader(sc));
				t.start();
				System.out.println("started new reader thread");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	class Reader implements Runnable {

		private AsynchronousSocketChannel sc;
		
		public Reader(AsynchronousSocketChannel sc) {
			this.sc = sc;
		}
		
		@Override
		public void run() {
			int i = 0;
			while (i < 10) {
				ByteBuffer inbuf = ByteBuffer.allocate(Middleware.INITIAL_BUFSIZE);
				Integer result = 0;
				int expectedReadBytes = 0;
				do {
					try {
						Future<Integer> readFuture = sc.read(inbuf);
						result += readFuture.get();
						expectedReadBytes = SerializingUtilities.unpackLength(inbuf);
						System.out.println("Expect for this request: " + expectedReadBytes + " bytes");
						System.out.println("read " + result + " bytes of " + expectedReadBytes + " bytes");
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				} while (result < expectedReadBytes);
				i++;
			}
		}
		
	}
	
}
