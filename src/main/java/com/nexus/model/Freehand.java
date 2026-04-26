package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Freehand extends Shape {
	private static final long serialVersionUID = 1L;
    
    // We use a List because we don’t know how many points the user will draw

    // Double is used for high precision coordinates on the Canvas.
    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();

    /**
     * This method is called by the Controller EVERY TIME the mouse moves
     * while pressed. It builds the path point-by-point.
     */
    public void addPoint(double x, double y) {
        xPoints.add(x);
        yPoints.add(y);
    }
    
    @Override
    protected void drawShape(GraphicsContext gc) {
        // We need at least 2 points to draw a line segment
        if (xPoints.size() < 2) return;

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
        }
        props.put("r", r);
        props.put("g", g);
        props.put("b", b);
        props.put("a", a);
        props.put("strokeWidth", strokeWidth);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        // This “reconstructs” the shape when receiving data from a collaborator
        int count = props.getOrDefault("pointCount", 0.0).intValue();
        xPoints.clear();
        yPoints.clear();
        
        for (int i = 0; i < count; i++) {
            xPoints.add(props.get("x" + i));
            yPoints.add(props.get("y" + i));
        }
        this.r = props.getOrDefault("r", 0.0);
        this.g = props.getOrDefault("g", 0.0);
        this.b = props.getOrDefault("b", 0.0);
        this.a = props.getOrDefault("a", 1.0);
        this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
    }

    public List<Point2D> getPointList() {
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < xPoints.size(); i++) {
            points.add(new Point2D(xPoints.get(i), yPoints.get(i)));
        }
        return points;
    }

    @Override
    public boolean hitTest(double x, double y) {
        for (int i = 0; i < xPoints.size(); i++) {
            double px = xPoints.get(i);
            double py = yPoints.get(i);
            if (Math.hypot(px - x, py - y) <= strokeWidth + 5) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void translate(double dx, double dy) {
        for (int i = 0; i < xPoints.size(); i++) {
            xPoints.set(i, xPoints.get(i) + dx);
            yPoints.set(i, yPoints.get(i) + dy);
        }
        // Also call super just in case
        super.translate(dx, dy);
    }
}

