package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape implements iShape {
	private static final long serialVersionUID = 1L;
	protected double x1,y1,x2,y2;
	
	// Store color components as serializable primitives
	protected double r = 0, g = 0, b = 0, a = 1.0;
	protected double fillR = 0, fillG = 0, fillB = 0, fillA = 0.0;
	
	protected String text = "";
	
	protected double strokeWidth = 2.0;
	protected double rotationAngle = 0.0;
	protected double scaleX = 1.0;
	protected double scaleY = 1.0;
	protected double translateX = 0.0;
	protected double translateY = 0.0;
	
	public void setPoints(double x1,double y1,double x2,double y2) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	
	@Override
	public String getText() {
	    return this.text;
	}
	
	@Override
	public void setText(String text) {
	    this.text = text != null ? text : "";
	}

	@Override
	public void setColor(Color color) {
		if (color != null) {
			this.r = color.getRed();
			this.g = color.getGreen();
			this.b = color.getBlue();
			this.a = color.getOpacity();
		}
	}
	
	public void setFillColor(Color fillColor) {
		if (fillColor != null) {
			this.fillR = fillColor.getRed();
			this.fillG = fillColor.getGreen();
			this.fillB = fillColor.getBlue();
			this.fillA = fillColor.getOpacity();
		}
	}
	
	public Color getColor() {
		return Color.color(r, g, b, a);
	}
	
	public Color getFillColor() {
		return Color.color(fillR, fillG, fillB, fillA);
	}
	
	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public void setRotation(double angle) {
		this.rotationAngle = angle;
	}
	
	public void setScale(double sx, double sy) {
		this.scaleX = sx;
		this.scaleY = sy;
	}
	
	public void setTransformTranslation(double tx, double ty) {
	    this.translateX = tx;
	    this.translateY = ty;
	}
	
	public boolean hitTest(double x, double y) {
		double minX = Math.min(x1, x2) + translateX;
		double maxX = Math.max(x1, x2) + translateX;
		double minY = Math.min(y1, y2) + translateY;
		double maxY = Math.max(y1, y2) + translateY;
		// Adding a small buffer for easier selection
		return x >= minX - 5 && x <= maxX + 5 && y >= minY - 5 && y <= maxY + 5;
	}
	
	public void translate(double dx, double dy) {
	    this.x1 += dx;
	    this.y1 += dy;
	    this.x2 += dx;
	    this.y2 += dy;
	}

	@Override
	public final void draw(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		
		// Apply Transformations
		double cx = (x1 + x2) / 2;
		double cy = (y1 + y2) / 2;
		
		gc.translate(cx + translateX, cy + translateY);
		gc.rotate(rotationAngle);
		gc.scale(scaleX, scaleY);
		gc.translate(-cx, -cy);
		
		gc.setStroke(getColor());
		gc.setFill(getFillColor());
		gc.setLineWidth(strokeWidth);
		
		drawShape(gc);
		
		gc.restore();
	}
	
	// Template method for subclasses to implement actual drawing
	protected abstract void drawShape(GraphicsContext gc);
}
