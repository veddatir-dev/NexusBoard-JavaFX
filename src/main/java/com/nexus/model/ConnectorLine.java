package com.nexus.model;

import javafx.scene.canvas.GraphicsContext;
import java.util.Map;
import java.util.HashMap;

public class ConnectorLine extends Shape {
    
    private DiagramShape sourceShape;
    private DiagramShape targetShape;
    private String sourceAnchor = "Right"; // Top, Bottom, Left, Right
    private String targetAnchor = "Left";
    private String lineType = "Solid"; // Solid, Dashed, Association, Inheritance
    
    public ConnectorLine() {
        super();
    }
    
    public void bind(DiagramShape source, DiagramShape target, String srcAnchor, String tgtAnchor) {
        this.sourceShape = source;
        this.targetShape = target;
        this.sourceAnchor = srcAnchor;
        this.targetAnchor = tgtAnchor;
    }
    
    public void setLineType(String type) {
        this.lineType = type;
    }
    
    public String getLineType() {
        return this.lineType;
    }
    
    public DiagramShape getSourceShape() { return sourceShape; }
    public DiagramShape getTargetShape() { return targetShape; }

    @Override
    protected void drawShape(GraphicsContext gc) {
        if (sourceShape == null || targetShape == null) return;
        
        // Update coordinates dynamically before drawing
        double sx = getAnchorX(sourceShape, sourceAnchor);
        double sy = getAnchorY(sourceShape, sourceAnchor);
        double ex = getAnchorX(targetShape, targetAnchor);
        double ey = getAnchorY(targetShape, targetAnchor);
        
        if ("Dashed".equals(lineType)) {
            gc.setLineDashes(10d, 10d);
        } else {
            gc.setLineDashes(null);
        }
        
        gc.strokeLine(sx, sy, ex, ey);
        gc.setLineDashes(null); // Reset
        
        // Draw UML line endings (arrows)
        if ("Association".equals(lineType) || "Inheritance".equals(lineType)) {
            drawArrowHead(gc, sx, sy, ex, ey, "Inheritance".equals(lineType));
        }
    }
    
    private void drawArrowHead(GraphicsContext gc, double sx, double sy, double ex, double ey, boolean hollow) {
        double angle = Math.atan2((ey - sy), (ex - sx)) - Math.PI / 2.0;
        double headSize = 15;
        
        double x1 = ex + headSize * Math.cos(angle - Math.PI / 6.0);
        double y1 = ey + headSize * Math.sin(angle - Math.PI / 6.0);
        double x2 = ex + headSize * Math.cos(angle + Math.PI / 6.0);
        double y2 = ey + headSize * Math.sin(angle + Math.PI / 6.0);
        
        if (hollow) {
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillPolygon(new double[]{ex, x1, x2}, new double[]{ey, y1, y2}, 3);
            gc.strokePolygon(new double[]{ex, x1, x2}, new double[]{ey, y1, y2}, 3);
        } else {
            gc.strokeLine(ex, ey, x1, y1);
            gc.strokeLine(ex, ey, x2, y2);
        }
    }

    private double getAnchorX(DiagramShape shape, String anchor) {
        switch (anchor) {
            case "Top": return shape.getAnchorTopX();
            case "Bottom": return shape.getAnchorBottomX();
            case "Left": return shape.getAnchorLeftX();
            case "Right": return shape.getAnchorRightX();
            default: return shape.getMinX();
        }
    }

    private double getAnchorY(DiagramShape shape, String anchor) {
        switch (anchor) {
            case "Top": return shape.getAnchorTopY();
            case "Bottom": return shape.getAnchorBottomY();
            case "Left": return shape.getAnchorLeftY();
            case "Right": return shape.getAnchorRightY();
            default: return shape.getMinY();
        }
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> props = new HashMap<>();
        props.put("r", r);
        props.put("g", g);
        props.put("b", b);
        props.put("a", a);
        props.put("strokeWidth", strokeWidth);
        return props;
    }

    @Override
    public void setProperties(Map<String, Double> props) {
        if (props.containsKey("r")) this.r = props.get("r");
        if (props.containsKey("g")) this.g = props.get("g");
        if (props.containsKey("b")) this.b = props.get("b");
        if (props.containsKey("a")) this.a = props.get("a");
        if (props.containsKey("strokeWidth")) this.strokeWidth = props.get("strokeWidth");
    }
}
