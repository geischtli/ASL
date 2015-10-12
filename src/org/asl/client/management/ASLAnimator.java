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

public class ASLAnimator extends Application {
	private AdminClient adminConsole;
	private GridPane root;
	private Scene scene;
	private ComboBox<Integer> senderClient;
	private ComboBox<Integer> receiverClient;
	private ComboBox<Integer> queuesOnline;
	private TextField numMessagesText;
	private TextField content;

	public ASLAnimator() throws IOException {
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
		root.add(new Label("Sender: "), 0, 1);
		root.add(new Label("Receiver: "), 0, 2);
		root.add(new Label("Queue: "), 0, 3);
		root.add(new Label("Content: "), 0, 4);
		content = new TextField();
		root.add(content, 1, 4);
		ObservableList<RequestType> requestlis = adminConsole.getPossibleRequests();
		
		ComboBox<RequestType> requestsChoice = new ComboBox<RequestType>(requestlis);
		requestsChoice.valueProperty().addListener(new ChangeListener<RequestType>() {

			@Override
			public void changed(ObservableValue<? extends RequestType> observable, RequestType old, RequestType request) {
				
				Button requestExecutor = new Button("Execute specified request");
				requestExecutor.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						adminConsole.executeRequest(request,
								senderClient.getValue(),
								receiverClient.getValue(),
								queuesOnline.getValue(),
								content.getText()
							);
					}
					
				});
				root.add(requestExecutor, 1, 5);
			}
		});
		root.add(requestsChoice, 1, 0);
		
		Button updateClientsButton = new Button("Update Clients");
		updateClientsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> clients = adminConsole.executeRequest(RequestType.GET_REGISTERED_CLIENTS, 0, 0, 0, "");
				System.out.println(clients.size() + " clients are online");
				senderClient = new ComboBox<Integer>(clients);
				root.add(senderClient, 1, 1);
				receiverClient = new ComboBox<Integer>(clients);
				root.add(receiverClient, 1, 2);
			}
			
		});
		root.add(updateClientsButton, 3, 0);
		
		Button updateQueuesButton = new Button("Update Queues");
		updateQueuesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> queues = adminConsole.executeRequest(RequestType.GET_REGISTERED_QUEUES, 0, 0, 0, "");
				System.out.println(queues.size() + " queues are online");
				queuesOnline = new ComboBox<Integer>(queues);
				root.add(queuesOnline, 1, 3);
			}
			
		});
		root.add(updateQueuesButton, 3, 1);
		
		numMessagesText = new TextField();
		root.add(numMessagesText, 4, 2);
		
		Button updateMessageCountButton = new Button("Update Message count");
		updateMessageCountButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<Integer> numMessages = adminConsole.executeRequest(RequestType.GET_NUMBER_OF_MESSAGES, 0, 0, 0, "");
				numMessagesText.setText(numMessages.get(0).toString());
			}
			
		});
		root.add(updateMessageCountButton, 3, 2);
	}
	
}