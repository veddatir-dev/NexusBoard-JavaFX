package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;

public class EquilateralTriangle extends Triangle {
	private static final long serialVersionUID = 1L;

    @Override
    protected void drawShape(GraphicsContext gc) {
        double width = Math.abs(x2 - x1);
        double height = width * Math.sqrt(3) / 2; // Height of an equilateral triangle
        
        double centerX = (x1 + x2) / 2;
        double topY = Math.min(y1, y2);
        
        double topX = centerX;
        double bottomLeftX = centerX - width / 2;
        double bottomLeftY = topY + height;
        double bottomRightX = centerX + width / 2;
        double bottomRightY = topY + height;

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
