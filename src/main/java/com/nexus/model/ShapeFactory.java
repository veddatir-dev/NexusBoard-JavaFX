package com.nexus.model;

public class ShapeFactory {
    
    // This is a “Static” method so we don’t have to create a 
    // new Factory object every time we want to draw.
public static iShape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        
        // The “Switch” statement is much faster and cleaner than “if-else”
        switch (shapeType.toLowerCase()) {
            case "line":
                return new Line();
            case "rectangle":
                return new Rectangle();
            case "circle":
                return new Circle();
            case "triangle":
                return new Triangle();
            case "freehand":
                return new Freehand();
            default:
                return null;
        }
    }
}
