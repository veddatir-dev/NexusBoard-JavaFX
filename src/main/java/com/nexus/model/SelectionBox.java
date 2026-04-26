package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Bounds;
import java.util.HashMap;
import java.util.Map;

public class SelectionBox extends Shape {
	private static final long serialVersionUID = 1L;
    private boolean isActive;
    private iShape selectedShape;

    public SelectionBox() {
        // #0066CC is RGB 0, 102, 204
        this.r = 0.0;
        this.g = 102.0 / 255.0;
        this.b = 204.0 / 255.0;
        this.a = 1.0;
        this.strokeWidth = 2.0;
        this.isActive = false;
    }

    public void setSelectedShape(iShape shape) {
        this.selectedShape = shape;
        this.isActive = (shape != null);
    }

    public iShape getSelectedShape() {
        return selectedShape;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deselect() {
        selectedShape = null;
        isActive = false;
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        if (!isActive || selectedShape == null) return;

        gc.setLineDashes(5);
        
        // Draw selection box around the shape bounds
        gc.strokeRect(x1, y1, x2 - x1, y2 - y1);
        
        // Draw resize handles at corners
        double handleSize = 8;
        gc.setFill(getColor());
        gc.fillRect(x1 - handleSize / 2, y1 - handleSize / 2, handleSize, handleSize);
        gc.fillRect(x2 - handleSize / 2, y1 - handleSize / 2, handleSize, handleSize);
        gc.fillRect(x1 - handleSize / 2, y2 - handleSize / 2, handleSize, handleSize);
        gc.fillRect(x2 - handleSize / 2, y2 - handleSize / 2, handleSize, handleSize);
        
        gc.setLineDashes(0);
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("x1", x1);
        props.put("y1", y1);
        props.put("x2", x2);
        props.put("y2", y2);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("x1")) x1 = properties.get("x1");
        if (properties.containsKey("y1")) y1 = properties.get("y1");
        if (properties.containsKey("x2")) x2 = properties.get("x2");
        if (properties.containsKey("y2")) y2 = properties.get("y2");
    }
}
