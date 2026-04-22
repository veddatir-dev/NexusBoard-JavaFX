package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DottedLine extends Line {

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth > 0 ? strokeWidth : 2);
        gc.setLineDashes(5 + strokeWidth, 5 + strokeWidth); // Dotted pattern scales with width
        gc.strokeLine(x1, y1, x2, y2);
        gc.setLineDashes(null); // Reset
    }
}