package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * An enhanced shape class that supports rotation, scaling, and transformation.
 */
public abstract class TransformableShape extends Shape {
    protected double rotation; // in degrees
    protected double scaleX;
    protected double scaleY;
    protected double centerX;
    protected double centerY;

    public TransformableShape() {
        super();
        this.rotation = 0.0;
        this.scaleX = 1.0;
        this.scaleY = 1.0;
        this.centerX = 0.0;
        this.centerY = 0.0;
    }

    public void setRotation(double degrees) {
        this.rotation = degrees % 360.0;
    }

    public double getRotation() {
        return rotation;
    }

    public void setScale(double scaleX, double scaleY) {
        this.scaleX = Math.max(0.1, scaleX);
        this.scaleY = Math.max(0.1, scaleY);
    }

    public void setScaleUniform(double scale) {
        this.scaleX = Math.max(0.1, scale);
        this.scaleY = Math.max(0.1, scale);
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setTransformCenter(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void rotateBy(double degrees) {
        this.rotation += degrees;
        this.rotation %= 360.0;
    }

    protected void applyTransform(GraphicsContext gc, Runnable drawOperation) {
        Affine transform = new Affine();
        
        // Translate to center, apply transformations, translate back
        transform.appendTranslation(centerX, centerY);
        transform.appendRotation(rotation);
        transform.appendScale(scaleX, scaleY);
        transform.appendTranslation(-centerX, -centerY);
        
        gc.setTransform(transform);
        drawOperation.run();
        gc.setTransform(new Affine()); // Reset transform
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("x1", x1);
        props.put("y1", y1);
        props.put("x2", x2);
        props.put("y2", y2);
        props.put("rotation", rotation);
        props.put("scaleX", scaleX);
        props.put("scaleY", scaleY);
        props.put("strokeWidth", strokeWidth);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("x1")) x1 = properties.get("x1");
        if (properties.containsKey("y1")) y1 = properties.get("y1");
        if (properties.containsKey("x2")) x2 = properties.get("x2");
        if (properties.containsKey("y2")) y2 = properties.get("y2");
        if (properties.containsKey("rotation")) rotation = properties.get("rotation");
        if (properties.containsKey("scaleX")) scaleX = properties.get("scaleX");
        if (properties.containsKey("scaleY")) scaleY = properties.get("scaleY");
        if (properties.containsKey("strokeWidth")) strokeWidth = properties.get("strokeWidth");
    }
}
