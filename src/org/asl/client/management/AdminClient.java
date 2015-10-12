package org.asl.client.management;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import org.asl.client.AbstractClient;
import org.asl.client.completionHandlers.ConnectCompletionHandler;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;
import org.asl.common.socket.SocketHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminClient extends AbstractClient implements Runnable {
		

	public AdminClient(int port) throws IOException {
		super(port);
	}


	ObservableList<RequestType> getPossibleRequests() {
		ObservableList<RequestType> requestlis = FXCollections.observableArrayList();
		requestlis.addAll(
				RequestType.CREATE_QUEUE,
				RequestType.DELETE_QUEUE,
				RequestType.GET_QUEUES_WITH_MESSAGES_FOR_CLIENT,
				RequestType.GET_REGISTERED_CLIENTS,
				RequestType.READ_ALL_MESSAGES_OF_QUEUE,
				RequestType.READ_MESSAGE_FROM_SENDER,
				RequestType.REMOVE_TOP_MESSAGE_FROM_QUEUE,
				RequestType.SEND_MESSAGE,
				RequestType.REGISTER_MIDDLEWARE
				);
		return requestlis;
	}


	public ObservableList<Integer> executeRequest(RequestType request) {
		requestList.add(0, request);
		run();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		switch (request) {
			case GET_REGISTERED_CLIENTS:
				return FXCollections.observableArrayList(ci.getClientsOnline());
			case GET_REGISTERED_QUEUES:
				return FXCollections.observableArrayList(ci.getQueuesOnline());
			case GET_NUMBER_OF_MESSAGES:
				return FXCollections.observableArrayList(ci.getNumberOfMessages());
			}
		return null;
	}


	@Override
	public void run() {
		sc = SocketHelper.openSocket();
		sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
				ConnectCompletionHandler.create(ci, sc, requestList, 0)
			);
	}
	
}
