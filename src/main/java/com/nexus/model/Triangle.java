package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends Shape {

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);

        // Calculate the three corners
        double topX = (x1 + x2) / 2;
        double topY = y1;
        double bottomLeftX = x1;
        double bottomLeftY = y2;
        double bottomRightX = x2;
        double bottomRightY = y2;

        // Draw the path (Triangle)
        gc.setLineWidth(2);
        gc.beginPath();
        gc.moveTo(topX, topY);          // Start at the top
        gc.lineTo(bottomLeftX, bottomLeftY); // Draw down to the left
        gc.lineTo(bottomRightX, bottomRightY); // Draw across the base
        gc.lineTo(topX, topY);          // Close back to the top
        gc.stroke();
        gc.closePath();
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        // Even for a triangle, we only need two points to define this shape!
        props.put("x1", x1);
        props.put("y1", y1);
        props.put("x2", x2);
        props.put("y2", y2);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        this.x1 = props.get("x1");
        this.y1 = props.get("y1");
        this.x2 = props.get("x2");
        this.y2 = props.get("y2");
    }
}
