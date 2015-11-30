//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.ShutdownChannelGroupException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
	private BufferedWriter rttWriter;
	// write the number of exectuing threads into a file
	private static BufferedWriter threadWriter;
	// timer for the threadWriter
	private Timer threadTimer;
	// generall log timer
	private Timer logTimer;
	// store all rtt's gathered in the last second
	public static AtomicLong rttPerSec;
	// how many nano seconds all threads waited to get a db connection
	public static AtomicLong waitForDbConnPerSec;
	private BufferedWriter waitForDbConnWriter;
	// store the actual time needed to perform the database access
	// this includes network MW<->DB + DB service time
	public static AtomicLong dbRtPerSec;
	private BufferedWriter dbRtWriter;
	// log connection to db queue length
	public static AtomicInteger currDbConnQueueLength;
	public static AtomicLong dbConnQueueLengthPerSec;
	private BufferedWriter dbConnQueueLengthWriter;
	
	public Middleware(int port, int numDBConns) throws IOException, SQLException {
		cachedExecutor = Executors.newCachedThreadPool();
		acg = AsynchronousChannelGroup.withCachedThreadPool(cachedExecutor, 100);
		//acg = AsynchronousChannelGroup.withFixedThreadPool(10, Executors.defaultThreadFactory());
		this.serverChannel = AsynchronousServerSocketChannel.open(acg);

		this.serverChannel.bind(new InetSocketAddress(port));
		this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.propParser = PropertyParser.create("config/config_common.xml").parse();
		ASLDatabase.initDatabase(numDBConns);
		Middleware.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.requestId = -1;
		this.watchDog = WatchDog.create(500); // = seconds of difference before removal
		this.watchDogTimer = new Timer();
		this.watchDogTimer.scheduleAtFixedRate(this.watchDog, 0, 5000);
		this.mi = MiddlewareInfo.create();
		Middleware.isShuttingDown = false;
		Middleware.numClientsGone = new AtomicInteger(0);
		Middleware.messageCount = new AtomicInteger(0);
		this.tpWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/throughput.log", false));
		Middleware.threadWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/threadCount.log", false));
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
		
		Middleware.rttPerSec = new AtomicLong(0);
		Middleware.waitForDbConnPerSec = new AtomicLong(0);
		Middleware.dbRtPerSec = new AtomicLong(0);
		Middleware.dbConnQueueLengthPerSec = new AtomicLong(0);
		Middleware.currDbConnQueueLength = new AtomicInteger(0);
		
		this.threadTimer = new Timer();
		this.threadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				int numRunning = 0;
				for (Thread t : Thread.getAllStackTraces().keySet()) {
					if (t.getState() == Thread.State.RUNNABLE) {
						numRunning++;
					}
				}
				try {
					threadWriter.write(String.valueOf(numRunning));
					threadWriter.newLine();	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
		
		this.rttWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/rtt.log", false));
		this.waitForDbConnWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/waitForDbConn.log", false));
		this.dbRtWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/db_plus_network_rt.log", false));
		this.dbConnQueueLengthWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/db_conn_queue_length.log", false));
		initLoggers();
		// register mw itself as last step before it is ready to serve clients
		RequestBuilder.getRegisterMiddlewareRequest().processOnMiddleware(mi);
	}
	
	public void initLoggers() {
		this.logTimer = new Timer();
		this.logTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					rttWriter.write(String.valueOf(Middleware.rttPerSec.get()) + "\n");
					waitForDbConnWriter.write(String.valueOf(Middleware.waitForDbConnPerSec.get()) + "\n");
					dbRtWriter.write(String.valueOf(Middleware.dbRtPerSec.get()) + "\n");
					dbConnQueueLengthWriter.write(String.valueOf(Middleware.dbConnQueueLengthPerSec.get()) + "\n");
					Middleware.waitForDbConnPerSec.set(0);
					Middleware.rttPerSec.set(0);
					Middleware.dbRtPerSec.set(0);
					Middleware.dbConnQueueLengthPerSec.set(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	public void accept() {
		serverChannel.accept(null, AcceptCompletionHandler.create(mi, serverChannel, watchDog, requestId));
	}
	
	public void shutdown() {
		System.out.println("Shutting down middleware");
		Middleware.isShuttingDown = true;
		mi.getMyTimeLogger().stopMyTimeLogger();
		tpTimer.cancel();
		threadTimer.cancel();
		this.logTimer.cancel();
		System.out.println("timers stopped");
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
		} catch (InterruptedException | IOException | ShutdownChannelGroupException e) {
			if (!(e instanceof ShutdownChannelGroupException)) {
				e.printStackTrace();
			}
		}
		try {
			this.rttWriter.close();
			this.waitForDbConnWriter.close();
			this.dbRtWriter.close();
			this.tpWriter.close();
			this.dbConnQueueLengthWriter.close();
			Middleware.threadWriter.close();
			System.out.println("Benchmark files closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Middleware successfully shut down");
	}
	
}