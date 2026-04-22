package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Freehand implements iShape {
    
    // We use a List because we don’t know how many points the user will draw

    // Double is used for high precision coordinates on the Canvas.
    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();
    private Color color;
    private double strokeWidth = 2.0;

    /**
     * This method is called by the Controller EVERY TIME the mouse moves
     * while pressed. It builds the path point-by-point.
     */
    public void addPoint(double x, double y) {
        xPoints.add(x);
        yPoints.add(y);
    }
    
    @Override
	public void setColor(Color color) {
		
		this.color=color;
	}
	
	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
    @Override
    public void draw(Canvas canvas) {
        // We need at least 2 points to draw a line segment
        if (xPoints.size() < 2) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth); 

        // Step 1: Tell the “pen” to start a new path
        gc.beginPath();
        
        // Step 2: Put the pen down at the very first point
        gc.moveTo(xPoints.get(0), yPoints.get(0));

        // Step 3: Loop through all stored points and connect them
        for (int i = 1; i < xPoints.size(); i++) {
            gc.lineTo(xPoints.get(i), yPoints.get(i));
        }

        // Step 4: Actually paint the line we just traced
        gc.stroke();
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        
        // For simple networking/storage, we send the “Size” of the path first
        props.put("pointCount", (double) xPoints.size());
        
        // Then we store every coordinate with a unique key like “x0”, “y0”, “x1”…
        for (int i = 0; i < xPoints.size(); i++) {
            props.put("x" + i, xPoints.get(i));
            props.put("y" + i, yPoints.get(i));
        }        props.put("r", color.getRed());
        props.put("g", color.getGreen());
        props.put("b", color.getBlue());
        props.put("a", color.getOpacity());
        props.put("strokeWidth", strokeWidth);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        // This “reconstructs” the shape when receiving data from a collaborator
        int count = props.get("pointCount").intValue();
        xPoints.clear();
        yPoints.clear();
        
        for (int i = 0; i < count; i++) {
            xPoints.add(props.get("x" + i));
            yPoints.add(props.get("y" + i));
        }        double r = props.get("r");
        double g = props.get("g");
        double b = props.get("b");
        double a = props.get("a");
        this.color = Color.color(r, g, b, a);
        this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
    }
    public List<Point2D> getPointList() {
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < xPoints.size(); i++) {
            points.add(new Point2D(xPoints.get(i), yPoints.get(i)));
        }
        return points;
    }

	
}

