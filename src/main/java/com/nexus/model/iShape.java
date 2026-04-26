package com.nexus.model;

import java.io.Serializable;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public interface iShape extends Serializable {
	
	// Every shape must know how to draw itself on the canvas
    void draw(Canvas canvas);
    public void setColor(Color color);
    String getText();
    void setText(String text);
    // For Networking: Turn the shape into a Map of properties
    Map<String, Double> getProperties();
    void setProperties(Map<String, Double> properties);

}
