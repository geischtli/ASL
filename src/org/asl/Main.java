package org.asl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.client.Client;
import org.asl.middleware.AbstractMiddleware;
import org.asl.middleware.Middleware;

public class Main {
	private static final int port = 9090;
	
	public static void main(String[] args) throws IOException, SQLException {
		AbstractMiddleware mw = new Middleware(port);
		System.out.println("Started server");
		mw.accept();
		ExecutorService threadpool = new ThreadPoolExecutor(
				8,
				64,
				0,
				TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(64),
				new ThreadPoolExecutor.CallerRunsPolicy()
				);
		int numClients = 5000;
		for (int i = 0; i < numClients; i++) {
			try {
				threadpool.submit(new Client(port));
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
}