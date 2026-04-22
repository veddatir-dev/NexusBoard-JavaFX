package com.nexus.model;

public class ShapeFactory {
    
    // This is a “Static” method so we don’t have to create a 
    // new Factory object every time we want to draw.
public static iShape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        
        // The “Switch” statement is much faster and cleaner than “if-else”
        switch (shapeType.toLowerCase().replace(" ", "")) {
            case "line":
                return new Line();
            case "dottedline":
                return new DottedLine();
            case "doubleline":
                return new DoubleLine();
            case "arrow":
                return new Arrow();
            case "rectangle":
                return new Rectangle();
            case "square":
                return new Square();
            case "circle":
                return new Circle();
            case "ellipse":
                return new Ellipse();
            case "triangle":
                return new Triangle();
            case "freehand":
                return new Freehand();
            case "text":
            case "textbox":
                return new TextBox();
            case "eraser":
                return new Eraser();
            case "selection":
            case "selectionbox":
                return new SelectionBox();
            default:
                return null;
        }
    }
}
