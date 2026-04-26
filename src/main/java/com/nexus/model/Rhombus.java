package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;

public class Rhombus extends Quadrilateral {
	private static final long serialVersionUID = 1L;

    @Override
    protected void drawShape(GraphicsContext gc) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        gc.beginPath();
        gc.moveTo(midX, minY);
        gc.lineTo(maxX, midY);
        gc.lineTo(midX, maxY);
        gc.lineTo(minX, midY);
        gc.lineTo(midX, minY);
        
        if (fillA > 0.0) gc.fill();
        gc.stroke();
        gc.closePath();
    }
}
