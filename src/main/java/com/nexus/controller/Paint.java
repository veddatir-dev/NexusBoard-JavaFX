package com.nexus.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Paint extends Application {

	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root=FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
		Scene scene = new Scene(root);
	    
	    // link CSS
	    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
	    
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Nexus Board");
	    primaryStage.show();

	}

}
