package org.asl.experiments.baselines.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.middleware.database.config.ASLDatabase;

public class DatabaseBaseline {

	public static void main(String[] args) {
		//int numConnectionsToDB = Integer.parseInt(args[0]);
		int numConnectionsToDB = 1;
		try {
			ASLDatabase.initDatabase(numConnectionsToDB);
		} catch (NumberFormatException | SQLException e1) {
			e1.printStackTrace();
		}
		
		//int numWorkers = Integer.parseInt(args[1]);
		int numWorkers = 1;
		int numRequests = 100;
		boolean performDummyRequests = true;
		
		System.out.println("NumDBConnections = " + numConnectionsToDB);
		System.out.println("NumWorkerClients = " + numWorkers);
		
		ExecutorService threadpool = new ThreadPoolExecutor(
				numConnectionsToDB,
				numConnectionsToDB,
				0,
				TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(numWorkers),
				new ThreadPoolExecutor.CallerRunsPolicy()
				);
		
		for (int i = 1; i <= numWorkers; i++) {
			try {
				threadpool.submit(new DatabaseBaselineClient(i, numRequests, performDummyRequests));
			} catch (Exception e) {
				System.out.println("Problem with client creation " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		// wait for enter strike such that the we can safely close the threadpool
		System.out.println("Wait for shutdown command (ENTER) ...");
		try {
			System.in.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (!threadpool.awaitTermination(0, TimeUnit.SECONDS)) {
				threadpool.shutdownNow();
				System.out.println("Forced threadpool to shutdown");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
