package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DecisionShape extends DiagramShape {

    public DecisionShape() {
        super();
        this.text = "Decision";
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        double midX = getMinX() + (getMaxX() - getMinX()) / 2.0;
        double midY = getMinY() + (getMaxY() - getMinY()) / 2.0;
        
        double[] xPoints = {midX, getMaxX(), midX, getMinX()};
        double[] yPoints = {getMinY(), midY, getMaxY(), midY};
        
        Color fill = getFillColor();
        if (fill != null && !fill.equals(Color.TRANSPARENT)) {
            gc.fillPolygon(xPoints, yPoints, 4);
        }
        gc.strokePolygon(xPoints, yPoints, 4);
        
        if (text != null && !text.isEmpty()) {
            gc.setFill(getColor());
            gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
            gc.fillText(text, midX, midY + 4);
            gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        }
    }
}
