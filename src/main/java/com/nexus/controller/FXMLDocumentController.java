package com.nexus.controller;

import com.nexus.model.*; // Import all shapes and the Factory

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController {

    @FXML private Canvas CanvasBox;
    @FXML private ComboBox<String> ShapeBox;
    @FXML private ColorPicker ColorBox;

    private double x1, y1; // To store the starting click
    private iShape currentShape; // The interface handles any shape!

    @FXML
    void startDrag(MouseEvent event) {
        // Step 1: Record where the mouse touched the canvas
        x1 = event.getX();
        y1 = event.getY();

        // Step 2: Use the Factory to create the correct shape object
        // Based on what is selected in the ComboBox (Shapes)
        String selected = ShapeBox.getValue();
        currentShape = ShapeFactory.getShape(selected);
        
        // Step 3: Special logic for Freehand (needs points as we move)
        if (currentShape instanceof Freehand) {
            ((Freehand) currentShape).addPoint(x1, y1);
        }
    }
    @FXML
    void whileDragging(MouseEvent event) {
        if (currentShape instanceof Freehand) {
            // 1. Add the new point to the list
            ((Freehand) currentShape).addPoint(event.getX(), event.getY());
            
            // 2. Draw it IMMEDIATELY so we can see the line following the mouse
            currentShape.draw(CanvasBox);
        }
    }


    @FXML
    void endDrag(MouseEvent event) {
        if (currentShape == null) return;

        // Step 4: Record where the mouse was released
        double x2 = event.getX();
        double y2 = event.getY();

        // Step 5: Tell the shape its start and end points
        // (This works for Line, Circle, Rectangle, etc.)
        if (currentShape instanceof Shape) {
            ((Shape) currentShape).setPoints(x1, y1, x2, y2);
            ((Shape) currentShape).setColor(ColorBox.getValue());
        }

        // Step 6: Draw the final shape on the Canvas!
        currentShape.draw(CanvasBox);
    }
    @FXML
    public void initialize() {
        // 1. Create a list of names for shapes
        ObservableList<String> list = FXCollections.observableArrayList(
            "Line", "Rectangle", "Circle", "Triangle", "Freehand"
        );

        // 2. Connect the list to ComboBox
        ShapeBox.setItems(list);
        
        // 3. Set a default value so it’s not empty when the app starts
        ShapeBox.setValue("Line");
    }
    @FXML
    void handleButtonAction(ActionEvent event) {
        // Source tells us which button was clicked
        System.out.println("A button was clicked!");
    }


}
