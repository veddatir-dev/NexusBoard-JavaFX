package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Circle extends Shape{
    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        double X=x2-x1;
        double Y=y2-y1;
        // Math: Distance between two points is our radius
        double radius = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        gc.setLineWidth(2);
        // JavaFX draws from the TOP-LEFT, so we subtract radius from center
        // to make (x1, y1) feel like the actual center.
        double Centerx=x1-radius;
        double Centery=y1-radius;
        gc.strokeOval(Centerx, Centery, radius * 2, radius * 2);
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("Centerx", x1);
        props.put("Centery", y1);
        double X=x2-x1;
        double Y=y2-y1;
        double radius = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2)); 
        props.put("radius", radius);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        // We take the numbers back out using our specific “Keys”
        this.x1 = props.get("Centerx");
        this.y1 = props.get("Centery");
        // For x2/y2, we can just store the radius back or calculate a point
        this.x2 = x1 + props.get("radius"); 
        this.y2 = y1; 
    }

	

}
