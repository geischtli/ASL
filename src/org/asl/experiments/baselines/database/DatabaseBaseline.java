package org.asl.experiments.baselines.database;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.client.VirtualClient;
import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.middleware.database.connectionpool.ConnectionPool;

public class DatabaseBaseline {

	private static String url;
	private static Properties props;
	private static ConnectionPool connectionPool;
	private static PropertyParser propParser;
	private static String dbIp;
	private static String dbPort;
	private static String dbName;
	private static String user;
	private static String password;
	private static int numConnectionsToDB;
	private static int numAsyncPoolThreads;
	
	public static void main(String[] args) {
		DatabaseBaseline.numConnectionsToDB = Integer.parseInt(args[0]);		
		
		DatabaseBaseline.propParser = PropertyParser.create("config/config_common.xml").parse();
		DatabaseBaseline.dbIp = DatabaseBaseline.propParser.getProperty(PropertyKey.DATABASE_IP);
		DatabaseBaseline.dbPort = DatabaseBaseline.propParser.getProperty(PropertyKey.DATABASE_PORT);
		DatabaseBaseline.dbName = DatabaseBaseline.propParser.getProperty(PropertyKey.DATABASE_NAME);
		DatabaseBaseline.user = DatabaseBaseline.propParser.getProperty(PropertyKey.USER);
		DatabaseBaseline.password = DatabaseBaseline.propParser.getProperty(PropertyKey.PASSWORD);
		DatabaseBaseline.numAsyncPoolThreads = Integer.valueOf(propParser.getProperty(PropertyKey.NUM_ASYNC_CONNECTION_POOL_THREADS));
		
		DatabaseBaseline.url = "jdbc:postgresql://" + dbIp + ":" + dbPort + "/" + dbName;
		//ASLDatabase.url = "jdbc:postgresql://" + "localhost" + ":" + dbPort + "/" + dbName;
		DatabaseBaseline.props = new Properties();
		DatabaseBaseline.props.setProperty("user", DatabaseBaseline.user);
		DatabaseBaseline.props.setProperty("password", DatabaseBaseline.password);
		DatabaseBaseline.connectionPool = new ConnectionPool(
				DatabaseBaseline.numConnectionsToDB,
				DatabaseBaseline.url,
				DatabaseBaseline.props,
				DatabaseBaseline.numAsyncPoolThreads
			);
		ExecutorService threadpool = new ThreadPoolExecutor(
				DatabaseBaseline.numConnectionsToDB,
				DatabaseBaseline.numConnectionsToDB,
				0,
				TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(DatabaseBaseline.numConnectionsToDB),
				new ThreadPoolExecutor.CallerRunsPolicy()
				);
		int numClients = 100;
		for (int i = 0; i < numClients; i++) {
			try {
				threadpool.submit(new VirtualClient(0, ""));
			} catch (Exception e) {
				System.out.println("Problem with client creation " + e.getMessage());
				e.printStackTrace();
			}
		}
		run();
	}
	
	public static void run() {
		
	}
	
}
