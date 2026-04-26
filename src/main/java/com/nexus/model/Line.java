package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;

public class Line extends Shape {
	private static final long serialVersionUID = 1L;

	@Override
	protected void drawShape(GraphicsContext gc) {
		gc.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public Map<String, Double> getProperties() {
		Map<String,Double> props=new HashMap<>();
		props.put("x1", x1);
		props.put("y1", y1);
		props.put("x2", x2);
		props.put("y2", y2);
		props.put("r", r);
		props.put("g", g);
		props.put("b", b);
		props.put("a", a);
		props.put("strokeWidth", strokeWidth);
		props.put("rotationAngle", rotationAngle);
		props.put("scaleX", scaleX);
		props.put("scaleY", scaleY);
		return props;
	}

	@Override
	public void setProperties(Map<String, Double> props) {
		this.x1=props.get("x1");
		this.y1=props.get("y1");
		this.x2=props.get("x2");
		this.y2=props.get("y2");
		this.r = props.get("r");
		this.g = props.get("g");
		this.b = props.get("b");
		this.a = props.get("a");
		this.strokeWidth = props.getOrDefault("strokeWidth", 2.0);
		this.rotationAngle = props.getOrDefault("rotationAngle", 0.0);
		this.scaleX = props.getOrDefault("scaleX", 1.0);
		this.scaleY = props.getOrDefault("scaleY", 1.0);
	}
	
}
