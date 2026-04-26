package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;

public class Parallelogram extends Quadrilateral {
	private static final long serialVersionUID = 1L;

    @Override
    protected void drawShape(GraphicsContext gc) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        
        double offset = (maxX - minX) * 0.2; // 20% offset for the slant

        gc.beginPath();
        gc.moveTo(minX + offset, minY);
        gc.lineTo(maxX, minY);
        gc.lineTo(maxX - offset, maxY);
        gc.lineTo(minX, maxY);
        gc.lineTo(minX + offset, minY);
        
        if (fillA > 0.0) gc.fill();
        gc.stroke();
        gc.closePath();
    }
}
