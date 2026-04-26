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
            case "parallelogram":
                return new Parallelogram();
            case "rhombus":
                return new Rhombus();
            case "trapezoid":
                return new Trapezoid();
            case "circle":
                return new Circle();
            case "ellipse":
                return new Ellipse();
            case "triangle":
                return new IsoscelesTriangle(); // Default to Isosceles for generic 'triangle'
            case "equilateraltriangle":
                return new EquilateralTriangle();
            case "isoscelestriangle":
                return new IsoscelesTriangle();
            case "righttriangle":
                return new RightTriangle();
            case "umlclass":
                return new UMLClassShape();
            case "decision":
                return new DecisionShape();
            case "actor":
                return new ActorShape();
            case "connector":
            case "connectorline":
                return new ConnectorLine();
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
