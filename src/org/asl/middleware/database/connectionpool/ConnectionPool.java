//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware.database.connectionpool;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.asl.middleware.Middleware;

public class ConnectionPool {
	
	private int numConnections;
	private LinkedBlockingQueue<ConnectionWrapper> connectionPool;
	private String url;
	private Properties props;
	private ExecutorService asyncExecutor;
	
	public ConnectionPool(int numConnections, String url, Properties props, int numAsyncThreads) {
		this.numConnections = numConnections;
		this.connectionPool = new LinkedBlockingQueue<ConnectionWrapper>(numConnections);
		this.url = url;
		this.props = props;
		this.asyncExecutor = Executors.newFixedThreadPool(numAsyncThreads);
		initPool();
	}
	
	private void initPool() {
		try {
		    Class.forName("org.postgresql.Driver");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("DIDNT FOUND DRIVER");
		    e.printStackTrace();
		}
		for (int i = 0; i < numConnections; i++) {
			try {
				connectionPool.add(ConnectionFactory.create(this, DriverManager.getConnection(url, props)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Future<ConnectionWrapper> acquireConnection() {
		return asyncExecutor.submit(
			new Callable<ConnectionWrapper>() {
				
				@Override
				public ConnectionWrapper call() throws Exception {
					//long startWait = System.nanoTime();
					ConnectionWrapper cw = connectionPool.take();
					//Middleware.waitForDbConnPerSec.addAndGet(System.nanoTime() - startWait);
					return cw;
				}
				
			}
		);
	}
	
	public void releaseConnection(ConnectionWrapper connectionWrapper) {
		try {
			connectionPool.put(connectionWrapper);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
