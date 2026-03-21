package com.nexus.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Paint extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root=FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Nexus Board");
		primaryStage.show();
	}

}
