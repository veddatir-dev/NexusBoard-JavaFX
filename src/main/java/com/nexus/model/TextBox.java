package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.HashMap;
import java.util.Map;

public class TextBox extends Shape {
	private static final long serialVersionUID = 1L;
    private double fontSize;
    private Font font;
    private TextAlignment alignment;

    public TextBox() {
        this.text = "Text";
        this.fontSize = 16.0;
        this.font = new Font("System", fontSize);
        this.alignment = TextAlignment.LEFT;
        this.r = 0.0;
        this.g = 0.0;
        this.b = 0.0;
        this.a = 1.0;
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        // Text should be filled with the stroke color usually, or fill color.
        gc.setFill(getColor());
        Font currentFont = new Font("System", fontSize);
        gc.setFont(currentFont);
        gc.setTextAlign(alignment);
        gc.fillText(text, x1, y1);
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
        this.font = new Font("System", fontSize);
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("x", x1);
        props.put("y", y1);
        props.put("fontSize", fontSize);
        props.put("colorR", r);
        props.put("colorG", g);
        props.put("colorB", b);
        props.put("colorA", a);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("x")) x1 = properties.get("x");
        if (properties.containsKey("y")) y1 = properties.get("y");
        if (properties.containsKey("fontSize")) setFontSize(properties.get("fontSize"));
        if (properties.containsKey("colorR")) {
            this.r = properties.get("colorR");
            this.g = properties.get("colorG");
            this.b = properties.get("colorB");
            this.a = properties.get("colorA");
        }
    }
}
