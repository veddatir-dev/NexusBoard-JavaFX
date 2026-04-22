package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Arrow extends Line {

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth > 0 ? strokeWidth : 2);
        gc.strokeLine(x1, y1, x2, y2);

        // Draw arrowhead
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10 + strokeWidth * 2; // Scale arrowhead with stroke width
        double arrowAngle = Math.PI / 6; // 30 degrees

        double x3 = x2 - arrowLength * Math.cos(angle - arrowAngle);
        double y3 = y2 - arrowLength * Math.sin(angle - arrowAngle);
        double x4 = x2 - arrowLength * Math.cos(angle + arrowAngle);
        double y4 = y2 - arrowLength * Math.sin(angle + arrowAngle);

        gc.strokeLine(x2, y2, x3, y3);
        gc.strokeLine(x2, y2, x4, y4);
    }
}