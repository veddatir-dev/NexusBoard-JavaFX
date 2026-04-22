package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Eraser implements iShape {
    private List<Point2D> points;
    private double eraserSize;
    private Color eraserColor;

    public Eraser() {
        this.points = new ArrayList<>();
        this.eraserSize = 10.0;
        this.eraserColor = new Color(1, 1, 1, 0.3);
    }

    public void addPoint(double x, double y) {
        points.add(new Point2D(x, y));
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void setEraserSize(double size) {
        this.eraserSize = size;
    }

    public double getEraserSize() {
        return eraserSize;
    }

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setGlobalAlpha(0.3);
        gc.setFill(Color.WHITE);
        
        for (Point2D p : points) {
            gc.clearRect(p.getX() - eraserSize / 2, p.getY() - eraserSize / 2, eraserSize, eraserSize);
        }
        gc.setGlobalAlpha(1.0);
    }

    @Override
    public void setColor(Color color) {
        this.eraserColor = color;
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("eraserSize", eraserSize);
        props.put("pointCount", (double) points.size());
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("eraserSize")) {
            this.eraserSize = properties.get("eraserSize");
        }
    }
}
