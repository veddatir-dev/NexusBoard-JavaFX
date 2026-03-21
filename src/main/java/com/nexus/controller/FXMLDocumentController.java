package com.nexus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController {

    // These @FXML fields link your UI components to Java code
    @FXML private Canvas CanvasBox;
    @FXML private ComboBox<String> ShapeBox;
    @FXML private ColorPicker ColorBox;
    @FXML private ListView<String> ShapeList;

    // These match the "On Action" and "On Mouse" events in Scene Builder
    @FXML
    void startDrag(MouseEvent event) {
        System.out.println("Mouse Pressed at: " + event.getX() + ", " + event.getY());
    }

    @FXML
    void endDrag(MouseEvent event) {
        System.out.println("Mouse Released at: " + event.getX() + ", " + event.getY());
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        // Source tells us which button was clicked
        System.out.println("A button was clicked!");
    }
}