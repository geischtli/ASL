package org.asl.client.management;

import java.io.IOException;

import org.asl.client.Client;
import org.asl.common.request.Request;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminClient extends Client implements Runnable {
		

	public AdminClient(int port) throws IOException {
		super(port);
	}


	ObservableList<Request> getPossibleRequests() {
		ObservableList<Request> requestlis = FXCollections.observableArrayList();
		requestlis.addAll(
				new CreateQueueRequest(313), 
				new DeleteQueueRequest(313));
		return requestlis;
	}


	public void executeRequest(Request request) {
		System.out.println("AdminClient: Here execute the request");
	}
	
}
