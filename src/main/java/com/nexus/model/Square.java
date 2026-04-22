package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Rectangle {

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth > 0 ? strokeWidth : 2);
        double Upperx = Math.min(x2, x1);
        double Uppery = Math.min(y2, y1);

        double Width = Math.abs(x2 - x1);
        double Height = Math.abs(y2 - y1);
        double side = Math.min(Width, Height); // Make it square
        
        if (fillColor != null && !fillColor.equals(Color.TRANSPARENT)) {
            gc.setFill(fillColor);
            gc.fillRect(Upperx, Uppery, side, side);
        }
        gc.strokeRect(Upperx, Uppery, side, side);
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        double Upperx = Math.min(x2, x1);
        double Uppery = Math.min(y2, y1);
        double side = Math.min(Math.abs(x2 - x1), Math.abs(y2 - y1));
        props.put("Upperx", Upperx);
        props.put("Uppery", Uppery);
        props.put("Side", side);
        props.put("r", color.getRed());
        props.put("g", color.getGreen());
        props.put("b", color.getBlue());
        props.put("a", color.getOpacity());
        props.put("strokeWidth", strokeWidth);
        if (fillColor != null) {
            props.put("fillR", fillColor.getRed());
            props.put("fillG", fillColor.getGreen());
            props.put("fillB", fillColor.getBlue());
            props.put("fillA", fillColor.getOpacity());
        }
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        this.x1 = props.get("Upperx");
        this.y1 = props.get("Uppery");
        double side = props.get("Side");
        this.x2 = x1 + side;
        this.y2 = y1 + side;
        double r = props.get("r");
        double g = props.get("g");
        double b = props.get("b");
        double a = props.get("a");
        this.color = Color.color(r, g, b, a);
        this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
        if (props.containsKey("fillR")) {
            double fillR = props.get("fillR");
            double fillG = props.get("fillG");
            double fillB = props.get("fillB");
            double fillA = props.get("fillA");
            this.fillColor = Color.color(fillR, fillG, fillB, fillA);
        }
    }
}