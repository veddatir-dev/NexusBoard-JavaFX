package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape {

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc=canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(strokeWidth > 0 ? strokeWidth : 2);
		gc.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public Map<String, Double> getProperties() {
		Map<String,Double> props=new HashMap<>();
		props.put("x1", x1);
		props.put("y1", y1);
		props.put("x2", x2);
		props.put("y2", y2);
		props.put("r", color.getRed());
		props.put("g", color.getGreen());
		props.put("b", color.getBlue());
		props.put("a", color.getOpacity());
		props.put("strokeWidth", strokeWidth);
		return props;
	}

	@Override
	public void setProperties(Map<String, Double> props) {
		this.x1=props.get("x1");
		this.y1=props.get("y1");
		this.x2=props.get("x2");
		this.y2=props.get("y2");
		double r = props.get("r");
		double g = props.get("g");
		double b = props.get("b");
		double a = props.get("a");
		this.color = Color.color(r, g, b, a);
		this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
	}
	
}
