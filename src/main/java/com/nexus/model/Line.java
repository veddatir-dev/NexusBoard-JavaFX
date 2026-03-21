package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Line extends Shape {

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc=canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(2);
		gc.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public Map<String, Double> getProperties() {
		Map<String,Double> props=new HashMap<>();
		props.put("x1", x1);
		props.put("y1", y1);
		props.put("x2", x2);
		props.put("y2", y2);
		return props;
	}

	@Override
	public void setProperties(Map<String, Double> props) {
		this.x1=props.get("x1");
		this.y1=props.get("y1");
		this.x2=props.get("x2");
		this.y2=props.get("y2");
	}
	
}
