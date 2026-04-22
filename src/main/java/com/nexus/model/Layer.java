package com.nexus.model;

import javafx.scene.canvas.Canvas;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single layer in the drawing canvas.
 * Layers can be shown/hidden, locked, and contain multiple shapes.
 */
public class Layer {
    private String name;
    private List<iShape> shapes;
    private boolean visible;
    private boolean locked;
    private double opacity;
    private int zIndex;

    public Layer(String name, int zIndex) {
        this.name = name;
        this.shapes = new ArrayList<>();
        this.visible = true;
        this.locked = false;
        this.opacity = 1.0;
        this.zIndex = zIndex;
    }

    public void addShape(iShape shape) {
        if (!locked) {
            shapes.add(shape);
        }
    }

    public void removeShape(iShape shape) {
        if (!locked) {
            shapes.remove(shape);
        }
    }

    public void removeShapeAt(int index) {
        if (!locked && index >= 0 && index < shapes.size()) {
            shapes.remove(index);
        }
    }

    public List<iShape> getShapes() {
        return shapes;
    }

    public void drawLayer(Canvas canvas) {
        if (visible) {
            for (iShape shape : shapes) {
                shape.draw(canvas);
            }
        }
    }

    public void clear() {
        if (!locked) {
            shapes.clear();
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = Math.max(0, Math.min(1, opacity));
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public int getShapeCount() {
        return shapes.size();
    }
}
