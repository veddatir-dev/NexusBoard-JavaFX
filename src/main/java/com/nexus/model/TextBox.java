package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.HashMap;
import java.util.Map;

public class TextBox extends Shape {
    private String text;
    private double fontSize;
    private Font font;
    private TextAlignment alignment;

    public TextBox() {
        this.text = "Text";
        this.fontSize = 16.0;
        this.font = new Font("System", fontSize);
        this.alignment = TextAlignment.LEFT;
        this.color = Color.BLACK;
    }

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        Font currentFont = new Font("System", fontSize);
        gc.setFont(currentFont);
        gc.setTextAlign(alignment);
        gc.fillText(text, x1, y1);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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
        props.put("colorR", (double) color.getRed());
        props.put("colorG", (double) color.getGreen());
        props.put("colorB", (double) color.getBlue());
        props.put("colorA", (double) color.getOpacity());
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        if (properties.containsKey("x")) x1 = properties.get("x");
        if (properties.containsKey("y")) y1 = properties.get("y");
        if (properties.containsKey("fontSize")) setFontSize(properties.get("fontSize"));
    }
}
