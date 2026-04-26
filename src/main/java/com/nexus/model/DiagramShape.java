package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

public abstract class DiagramShape extends Shape {

    protected String id;

    public DiagramShape() {
        super();
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public double getMinX() { return Math.min(x1, x2); }
    public double getMaxX() { return Math.max(x1, x2); }
    public double getMinY() { return Math.min(y1, y2); }
    public double getMaxY() { return Math.max(y1, y2); }

    // Anchor Points logic for Connections
    public double getAnchorTopX() {
        return getMinX() + (getMaxX() - getMinX()) / 2.0;
    }

    public double getAnchorTopY() {
        return getMinY();
    }

    public double getAnchorBottomX() {
        return getMinX() + (getMaxX() - getMinX()) / 2.0;
    }

    public double getAnchorBottomY() {
        return getMaxY();
    }

    public double getAnchorLeftX() {
        return getMinX();
    }

    public double getAnchorLeftY() {
        return getMinY() + (getMaxY() - getMinY()) / 2.0;
    }

    public double getAnchorRightX() {
        return getMaxX();
    }

    public double getAnchorRightY() {
        return getMinY() + (getMaxY() - getMinY()) / 2.0;
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("x1", x1);
        props.put("y1", y1);
        props.put("x2", x2);
        props.put("y2", y2);
        props.put("r", r);
        props.put("g", g);
        props.put("b", b);
        props.put("a", a);
        props.put("strokeWidth", strokeWidth);
        props.put("scaleX", scaleX);
        props.put("scaleY", scaleY);
        props.put("rotationAngle", rotationAngle);
        props.put("translateX", translateX);
        props.put("translateY", translateY);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        if (props.containsKey("x1")) this.x1 = props.get("x1");
        if (props.containsKey("y1")) this.y1 = props.get("y1");
        if (props.containsKey("x2")) this.x2 = props.get("x2");
        if (props.containsKey("y2")) this.y2 = props.get("y2");
        if (props.containsKey("r")) this.r = props.get("r");
        if (props.containsKey("g")) this.g = props.get("g");
        if (props.containsKey("b")) this.b = props.get("b");
        if (props.containsKey("a")) this.a = props.get("a");
        if (props.containsKey("strokeWidth")) this.strokeWidth = props.get("strokeWidth");
        if (props.containsKey("scaleX")) this.scaleX = props.get("scaleX");
        if (props.containsKey("scaleY")) this.scaleY = props.get("scaleY");
        if (props.containsKey("rotationAngle")) this.rotationAngle = props.get("rotationAngle");
        if (props.containsKey("translateX")) this.translateX = props.get("translateX");
        if (props.containsKey("translateY")) this.translateY = props.get("translateY");
    }
}
