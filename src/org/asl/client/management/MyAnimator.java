package org.asl.client.management;

import java.io.IOException;

import org.asl.common.request.Request.RequestType;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MyAnimator extends Application {
	private AdminClient adminConsole;
	private GridPane root;
	private Scene scene;
	

	public MyAnimator() throws IOException {
		this.adminConsole = null;
		this.root = null;
		this.scene = null;
	}

	@Override
	public void init() throws IOException {
		Parameters p = getParameters();
		adminConsole = new AdminClient(Integer.parseInt(p.getRaw().get(0)));
		createMyConsole();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void createMyConsole() {
		root = new GridPane();
		scene = new Scene(root, 1000,500);
		root.add(new Label("Request: "), 0, 0);
		ObservableList<RequestType> requestlis = adminConsole.getPossibleRequests();
		
		ComboBox<RequestType> requestsChoice = new ComboBox<RequestType>(requestlis);
		requestsChoice.valueProperty().addListener(new ChangeListener<RequestType>() {

			@Override
			public void changed(ObservableValue<? extends RequestType> observable, RequestType old, RequestType request) {
				System.out.println("Dropdown Changed");
				System.out.println("Mby change look and feel of guy ?");
				
				Button requestExecutor = new Button("Hit me to exec Request");
				requestExecutor.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Send actual request");
						System.out.println("I should actually send: " + request.getClass().toString());
						System.out.println("but instead i send this gay everythign shitty fuck mongo arschloch nothing");
						adminConsole.executeRequest(request);
					}
				});
				root.add(requestExecutor, 1, 1);
			}
		});
		root.add(requestsChoice, 1, 0);
		
		Button updateClientsButton = new Button("Update Clients");
		updateClientsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> clients = adminConsole.executeRequest(RequestType.GET_REGISTERED_CLIENTS);
				System.out.println(clients.size() + " clients are online");
				ComboBox<Integer> clientsOnline = new ComboBox<Integer>(clients);
				root.add(clientsOnline, 4, 0);
			}
		});
		root.add(updateClientsButton, 3, 0);
		
		Button updateQueuesButton = new Button("Update Queues");
		updateQueuesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> queues = adminConsole.executeRequest(RequestType.GET_REGISTERED_QUEUES);
				System.out.println(queues.size() + " queues are online");
				ComboBox<Integer> queuesOnline = new ComboBox<Integer>(queues);
				root.add(queuesOnline, 4, 1);
			}
		});
		root.add(updateQueuesButton, 3, 1);
		
		TextField numMessagesText = new TextField();
		root.add(numMessagesText, 4, 2);
		
		Button updateMessageCountButton = new Button("Update Message count");
		updateMessageCountButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> numMessages = adminConsole.executeRequest(RequestType.GET_NUMBER_OF_MESSAGES);
				numMessagesText.setText(numMessages.get(0).toString());
			}
		});
		root.add(updateMessageCountButton, 3, 2);
	}
	
}