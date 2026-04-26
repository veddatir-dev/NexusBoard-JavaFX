package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DoubleLine extends Line {

    @Override
    protected void drawShape(GraphicsContext gc) {
        // Draw two parallel lines
        double offset = 3 + strokeWidth;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx*dx + dy*dy);
        double ux = dx / length;
        double uy = dy / length;
        double px = -uy * offset;
        double py = ux * offset;
        gc.strokeLine(x1 + px, y1 + py, x2 + px, y2 + py);
        gc.strokeLine(x1 - px, y1 - py, x2 - px, y2 - py);
    }
}