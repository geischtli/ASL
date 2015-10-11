package org.asl.client.management;

import java.io.IOException;

import org.asl.common.request.Request;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MyAnimator extends Application {
	private final AdminClient adminConsole;
	private GridPane root;
	private Scene scene;
	

	public MyAnimator() throws IOException {
		adminConsole = new AdminClient(313);
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
		root.add(new Label("Sandy isch es gaggimoggeli"),0, 0);
		ObservableList<Request> requestlis = adminConsole.getPossibleRequests();
		
		ComboBox<Request> requestsChoice = new ComboBox<Request>(requestlis);
		requestsChoice.valueProperty().addListener(new ChangeListener<Request>() {

			@Override
			public void changed(ObservableValue<? extends Request> observable, Request old, Request request) {
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
				root.add(requestExecutor,5, 0);
			}
		});
		root.add(requestsChoice, 0, 1);
	}
	
}