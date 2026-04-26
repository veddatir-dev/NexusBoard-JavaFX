package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;

public class RightTriangle extends Triangle {
	private static final long serialVersionUID = 1L;

    @Override
    protected void drawShape(GraphicsContext gc) {
        double topX = Math.min(x1, x2);
        double topY = Math.min(y1, y2);
        double bottomLeftX = Math.min(x1, x2);
        double bottomLeftY = Math.max(y1, y2);
        double bottomRightX = Math.max(x1, x2);
        double bottomRightY = Math.max(y1, y2);

        gc.beginPath();
        gc.moveTo(topX, topY);          
        gc.lineTo(bottomLeftX, bottomLeftY); 
        gc.lineTo(bottomRightX, bottomRightY); 
        gc.lineTo(topX, topY);          
        
        if (fillA > 0.0) gc.fill();
        gc.stroke();
        gc.closePath();
    }
}
