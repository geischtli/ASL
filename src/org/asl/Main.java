package org.asl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asl.client.Client;
import org.asl.client.VirtualClient;
import org.asl.client.management.AdminClient;
import org.asl.client.management.MyAnimator;
import org.asl.common.request.Request;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;
import org.asl.common.request.types.client.SendMessageRequest;
import org.asl.middleware.AbstractMiddleware;
import org.asl.middleware.Middleware;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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
		int numClients = 10;
		
		checkManagement(args, threadpool);
		
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
	
	private static void checkManagement(String[] args, ExecutorService threadpool) {
		for (String s : args){
			if (s.equals("admin")) {
				Application.launch(MyAnimator.class, args);
			}
		}
	}


}