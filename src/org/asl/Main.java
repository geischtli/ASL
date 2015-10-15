package org.asl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.client.VirtualClient;
import org.asl.client.management.ASLAnimator;
import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.middleware.Middleware;

import javafx.application.Application;

public class Main {

	private static String mwIp;
	private static String dbIp;
	private static int mwPort;
	private static PropertyParser propParser;
	
	public static void main(String[] args) throws IOException, SQLException {
		propParser = PropertyParser.create("config_common.xml").parse();

		mwIp = propParser.getProperty(PropertyKey.MIDDLEWARE_IP);
		mwPort = Integer.parseInt(propParser.getProperty(PropertyKey.MIDDLEWARE_PORT));		
		
		Middleware mw = new Middleware(mwPort);
		mw.accept();
		System.out.println("Started server");
		
		ExecutorService threadpool = new ThreadPoolExecutor(
				8,
				8,
				0,
				TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(64),
				new ThreadPoolExecutor.CallerRunsPolicy()
				);
		int numClients = 1;
		
		checkManagement(args, threadpool, mwPort, mwIp);
		
		for (int i = 0; i < numClients; i++) {
			try {
				threadpool.submit(new VirtualClient(mwPort, mwIp));
			} catch (Exception e) {
				System.out.println("Problem with client creation " + e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			if (!threadpool.awaitTermination(600, TimeUnit.SECONDS)) {
				System.out.println("Force threadpool to shutdown");
				threadpool.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkManagement(String[] args, ExecutorService threadpool, int port, String ip) {
		for (String s : args){
			if (s.equals("admin")) {
				String[] portArg = {String.valueOf(port), ip};
				Application.launch(ASLAnimator.class, portArg);
			}
		}
	}


}