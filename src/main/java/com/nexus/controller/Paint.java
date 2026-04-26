package com.nexus.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Paint extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	    // This tells Java to look inside the "resources" folder for the fxml subfolder
	    URL fxmlLocation = getClass().getResource("/fxml/MainView.fxml");
	    
	    if (fxmlLocation == null) {
	        throw new RuntimeException("Critical Error: /fxml/MainView.fxml not found in resources!");
	    }

	    Parent root = FXMLLoader.load(fxmlLocation);
	    Scene scene = new Scene(root);

	    // Load the CSS similarly
	    URL cssLocation = getClass().getResource("/css/professional-dark.css");
	    if (cssLocation != null) {
	        scene.getStylesheets().add(cssLocation.toExternalForm());
	    }

	    primaryStage.setTitle("Nexus Board v3.0 - Developer Edition");
	    primaryStage.setScene(scene);
	    
	    // Set window bounds and center
	    primaryStage.setMinWidth(1024);
	    primaryStage.setMinHeight(768);
	    primaryStage.sizeToScene();
	    primaryStage.centerOnScreen();
	    
	    primaryStage.show();
	}
}
