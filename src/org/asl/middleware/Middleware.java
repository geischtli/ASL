package org.asl.middleware;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.middleware.completionHandlers.AcceptCompletionHandler;
import org.asl.middleware.connectioncontrol.WatchDog;
import org.asl.middleware.database.config.ASLDatabase;

public class Middleware {
	
	private final AsynchronousServerSocketChannel serverChannel;
	private PropertyParser propParser;
	public static int INITIAL_BUFSIZE;
	private int requestId;
	private WatchDog watchDog;
	private Timer watchDogTimer;
	private MiddlewareInfo mi;
	public static boolean isShuttingDown;
	public static AtomicInteger numClientsGone;
	// used to measure throughput/sec for middleware benchmark
	public static AtomicInteger messageCount;
	// write current throughput via this writer
	private BufferedWriter tpWriter;
	// write messagecount every second and reset it
	private Timer tpTimer;
	private ExecutorService cachedExecutor;
	private AsynchronousChannelGroup acg;
	// write each RTT to a file
	private static BufferedWriter rttWriter;
	
	public Middleware(int port) throws IOException, SQLException {
		cachedExecutor = Executors.newCachedThreadPool();
		acg = AsynchronousChannelGroup.withCachedThreadPool(cachedExecutor, 30);
		this.serverChannel = AsynchronousServerSocketChannel.open(acg);
		
		this.serverChannel.bind(new InetSocketAddress(port));
		this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.propParser = PropertyParser.create("config/config_common.xml").parse();
		ASLDatabase.initDatabase();
		Middleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.requestId = -1;
		this.watchDog = WatchDog.create(500);
		this.watchDogTimer = new Timer();
		this.watchDogTimer.scheduleAtFixedRate(this.watchDog, 0, 5000);
		this.mi = MiddlewareInfo.create();
		Middleware.isShuttingDown = false;
		Middleware.numClientsGone = new AtomicInteger(0);
		Middleware.messageCount = new AtomicInteger(0);
		this.tpWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/mw_baseline/throughput.log"));
		this.tpTimer = new Timer();
		this.tpTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				try {
					tpWriter.write(String.valueOf(Middleware.messageCount.getAndSet(0)));
					tpWriter.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}, 0, 1000);
		Middleware.rttWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/mw_baseline/rtt.log"));
		
		RequestBuilder.getRegisterMiddlewareRequest().processOnMiddleware(mi);
	}
	
	public void accept() {
		serverChannel.accept(null, AcceptCompletionHandler.create(mi, serverChannel, watchDog, requestId));
	}
	
	public void shutdown() {
		System.out.println("Shutting down middleware");
		Middleware.isShuttingDown = true;
		mi.getMyTimeLogger().stopMyTimeLogger();
		try {
			if (!cachedExecutor.awaitTermination(0, TimeUnit.SECONDS)) {
				System.out.println("Force cachedExecutor to shutdown");
				cachedExecutor.shutdownNow();
			}
			if (!acg.awaitTermination(0, TimeUnit.SECONDS)) {
				System.out.println("Force acg to shutdown");
				acg.shutdownNow();
			}
			serverChannel.close();
			System.out.println("ServerChannel closed");
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		try {
			Middleware.rttWriter.close();
			tpWriter.close();
			System.out.println("Benchmark files closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Middleware successfully shut down");
	}
	
	public static void writeRTT(long rtt) {
		synchronized(Middleware.rttWriter) {
			try {
				Middleware.rttWriter.write(String.valueOf(rtt));
				Middleware.rttWriter.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}