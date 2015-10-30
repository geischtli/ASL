package org.asl.experiments.baselines.client.middlewarelistener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Timer;
import java.util.TimerTask;
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
	private BufferedWriter timeLogger;
	private Timer timer;
	private Long totalWritePerSec;
	private Long totalReadPerSec;
	private AtomicInteger numReqPerSec;
	
	public BenchListener() throws IOException {
		cachedExecutor = Executors.newCachedThreadPool();
		acg = AsynchronousChannelGroup.withCachedThreadPool(cachedExecutor, 100);
		this.serverChannel = AsynchronousServerSocketChannel.open(acg);
		this.serverChannel.bind(new InetSocketAddress(port));
		this.numRequests = new AtomicInteger(0);
		this.goneClients = 1;
		this.timeLogger = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/client_baseline/middlewareTimes.log", false));
		this.totalReadPerSec = new Long(0);
		this.totalWritePerSec = new Long(0);
		this.numReqPerSec = new AtomicInteger(0);
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					timeLogger.write(String.valueOf(numReqPerSec.get()) + "\t"
							+ String.valueOf(totalWritePerSec) + "\t" + String.valueOf(totalReadPerSec) + "\n");
					numReqPerSec.set(0);
					totalWritePerSec = 0L;
					totalReadPerSec = 0L;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	public void go() {
		System.out.println("Server started!");
		serverChannel.accept(null, new AcceptCompletionHandler());
	}
	
	public void shutdown() {
		this.timer.cancel();
		try {
			this.timeLogger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		private long readStart;

		private ReadCompletionHandler(AsynchronousSocketChannel sc, ByteBuffer inbuf) {
			this.sc = sc;
			this.inbuf = inbuf;
			this.readStart = System.nanoTime();
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
			synchronized (totalReadPerSec) {
				totalReadPerSec += (System.nanoTime() - readStart);
			}
			numRequests.incrementAndGet();
			try {
				long startWrite = System.nanoTime();
				sc.write(ByteBuffer.wrap("0".getBytes())).get();
				synchronized (totalWritePerSec) {
					totalWritePerSec += (System.nanoTime() - startWrite);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			numReqPerSec.incrementAndGet();
			ByteBuffer newinbuf = ByteBuffer.allocate(10240);
			sc.read(newinbuf, null, new ReadCompletionHandler(sc, newinbuf));
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			System.out.println("client number " + goneClients++ + " gone");
		}
	}

}
