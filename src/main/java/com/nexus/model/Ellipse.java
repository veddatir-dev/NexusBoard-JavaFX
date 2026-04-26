package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Shape {
	private static final long serialVersionUID = 1L;

    @Override
    protected void drawShape(GraphicsContext gc) {
        double Upperx = Math.min(x2, x1);
        double Uppery = Math.min(y2, y1);

        double Width = Math.abs(x2 - x1);
        double Height = Math.abs(y2 - y1);
        
        if (fillA > 0.0) {
            gc.fillOval(Upperx, Uppery, Width, Height);
        }
        gc.strokeOval(Upperx, Uppery, Width, Height);
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("Upperx", Math.min(x2, x1));
        props.put("Uppery", Math.min(y2, y1));
        props.put("Width", Math.abs(x2 - x1));
        props.put("Height", Math.abs(y2 - y1));
        props.put("r", r);
        props.put("g", g);
        props.put("b", b);
        props.put("a", a);
        props.put("strokeWidth", strokeWidth);
		props.put("rotationAngle", rotationAngle);
		props.put("scaleX", scaleX);
		props.put("scaleY", scaleY);
        
        props.put("fillR", fillR);
        props.put("fillG", fillG);
        props.put("fillB", fillB);
        props.put("fillA", fillA);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        this.x1 = props.get("Upperx");
        this.y1 = props.get("Uppery");
        this.x2 = props.get("Width") + x1;
        this.y2 = props.get("Height") + y1;
        this.r = props.get("r");
        this.g = props.get("g");
        this.b = props.get("b");
        this.a = props.get("a");
        this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
		this.rotationAngle = props.getOrDefault("rotationAngle", 0.0);
		this.scaleX = props.getOrDefault("scaleX", 1.0);
		this.scaleY = props.getOrDefault("scaleY", 1.0);
        
        if (props.containsKey("fillR")) {
            this.fillR = props.get("fillR");
            this.fillG = props.get("fillG");
            this.fillB = props.get("fillB");
            this.fillA = props.get("fillA");
        }
    }
}