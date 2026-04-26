package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UMLClassShape extends DiagramShape {

    public UMLClassShape() {
        super();
        this.text = "ClassName\n---\n+ attribute1: type\n- attribute2: type\n---\n+ method1()\n+ method2()";
    }

    @Override
    protected void drawShape(GraphicsContext gc) {
        Color fill = getFillColor();
        if (fill != null && !fill.equals(Color.TRANSPARENT)) {
            gc.fillRect(getMinX(), getMinY(), getMaxX() - getMinX(), getMaxY() - getMinY());
        }
        
        double width = getMaxX() - getMinX();
        double height = getMaxY() - getMinY();
        
        gc.strokeRect(getMinX(), getMinY(), width, height);
        
        // Draw horizontal lines separating sections
        double sectionHeight = height / 3.0;
        gc.strokeLine(getMinX(), getMinY() + sectionHeight, getMaxX(), getMinY() + sectionHeight);
        gc.strokeLine(getMinX(), getMinY() + 2 * sectionHeight, getMaxX(), getMinY() + 2 * sectionHeight);
        
        // Parse text into 3 sections
        String[] parts = this.text.split("\n---\n");
        String cName = parts.length > 0 ? parts[0] : "";
        String attrs = parts.length > 1 ? parts[1] : "";
        String meths = parts.length > 2 ? parts[2] : "";

        // Draw text
        gc.setFill(getColor());
        gc.fillText(cName, getMinX() + 5, getMinY() + 15);
        
        // Simple multiline draw for attributes/methods (could be improved)
        double attrY = getMinY() + sectionHeight + 15;
        for (String line : attrs.split("\n")) {
            gc.fillText(line, getMinX() + 5, attrY);
            attrY += 12;
        }
        
        double methY = getMinY() + 2 * sectionHeight + 15;
        for (String line : meths.split("\n")) {
            gc.fillText(line, getMinX() + 5, methY);
            methY += 12;
        }
    }
}
