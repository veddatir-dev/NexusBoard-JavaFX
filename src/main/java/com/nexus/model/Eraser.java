package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.effect.BlendMode;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Eraser extends Shape {
	private static final long serialVersionUID = 1L;
    private List<Point2D> points;
    private double eraserSize;

    public Eraser() {
        this.points = new ArrayList<>();
        this.eraserSize = 10.0;
        this.a = 1.0;
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

    @Override
    public boolean hitTest(double x, double y) {
        for (Point2D p : points) {
            if (Math.hypot(p.getX() - x, p.getY() - y) <= eraserSize) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void translate(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            points.set(i, new Point2D(p.getX() + dx, p.getY() + dy));
        }
        super.translate(dx, dy);
    }

    public double getEraserSize() {
        return eraserSize;
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        
        String theme = com.nexus.view.ThemeManager.getCurrentTheme();
        if (com.nexus.view.ThemeManager.LIGHT_MODERN.equals(theme)) {
            gc.setFill(javafx.scene.paint.Color.web("#ffffff")); // light mode bg
        } else {
            gc.setFill(javafx.scene.paint.Color.web("#121212")); // dark mode bg
        }
        
        for (Point2D p : points) {
            gc.fillOval(p.getX() - eraserSize / 2, p.getY() - eraserSize / 2, eraserSize, eraserSize);
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("eraserSize", eraserSize);
        props.put("pointCount", (double) points.size());
        for (int i = 0; i < points.size(); i++) {
            props.put("x" + i, points.get(i).getX());
            props.put("y" + i, points.get(i).getY());
        }
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("eraserSize")) {
            this.eraserSize = properties.get("eraserSize");
        }
        int count = properties.getOrDefault("pointCount", 0.0).intValue();
        points.clear();
        for (int i = 0; i < count; i++) {
            points.add(new Point2D(properties.get("x" + i), properties.get("y" + i)));
        }
    }
}
