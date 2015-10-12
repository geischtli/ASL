package org.asl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.client.VirtualClient;
import org.asl.client.management.MyAnimator;
import org.asl.middleware.AbstractMiddleware;
import org.asl.middleware.Middleware;

import javafx.application.Application;

public class Main {
	private static final int port = 9090;
	static int p = 0;
	public static void main(String[] args) throws IOException, SQLException {
		AbstractMiddleware mw = new Middleware(port);
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
		int numClients = 0;
		
		checkManagement(args, threadpool, port);
		
		for (int i = 0; i < numClients; i++) {
			try {
				threadpool.submit(new VirtualClient(port));
			} catch (Exception e) {
				System.out.println("Problem with client creation " + e.getMessage());
				e.printStackTrace();
			}
		}
		//threadpool.shutdown();
		try {
			if (!threadpool.awaitTermination(600, TimeUnit.SECONDS)) {
				System.out.println("Force threadpool to shutdown");
				threadpool.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkManagement(String[] args, ExecutorService threadpool, int port) {
		for (String s : args){
			if (s.equals("admin")) {
				String[] portArg = {String.valueOf(port)};
				Application.launch(MyAnimator.class, portArg);
			}
		}
	}


}