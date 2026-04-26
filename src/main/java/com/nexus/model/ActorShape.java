package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;

public class ActorShape extends DiagramShape {

    public ActorShape() {
        super();
        this.text = "Actor";
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        double width = getMaxX() - getMinX();
        double height = getMaxY() - getMinY();
        double midX = getMinX() + width / 2.0;
        
        // Head
        double headSize = Math.min(width, height * 0.3);
        gc.strokeOval(midX - headSize / 2.0, getMinY(), headSize, headSize);
        
        // Body
        double bodyStartY = getMinY() + headSize;
        double bodyEndY = getMinY() + height * 0.7;
        gc.strokeLine(midX, bodyStartY, midX, bodyEndY);
        
        // Arms
        double armsY = bodyStartY + (bodyEndY - bodyStartY) * 0.3;
        gc.strokeLine(getMinX(), armsY, getMaxX(), armsY);
        
        // Legs
        gc.strokeLine(midX, bodyEndY, getMinX(), getMaxY());
        gc.strokeLine(midX, bodyEndY, getMaxX(), getMaxY());
        
        if (text != null && !text.isEmpty()) {
            gc.setFill(getColor());
            gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
            gc.fillText(text, midX, getMaxY() + 15);
            gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        }
    }
}
