package com.nexus.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Circle extends Shape{
   
    @Override
	public void draw(Canvas canvas) {
		GraphicsContext gc=canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(2);
		double Upperx=Math.min(x2, x1);
		double Uppery=Math.min(y2,y1);
		
		double Width=Math.abs(x2-x1);
		double Height=Math.abs(y2-y1);
		gc.strokeOval(Upperx,Uppery,Width,Height);
	}

    @Override
	public Map<String, Double> getProperties() {
		Map<String,Double> props=new HashMap<>();
		props.put("Upperx",Math.min(x2, x1));
		props.put("Uppery",Math.min(y2, y1));
		props.put("Width", Math.abs(x2-x1));
		props.put("Height", Math.abs(y2-y1));
		return props;
	}

	@Override
	public void setProperties(Map<String, Double> props) {
		this.x1=props.get("Upperx");
		this.y1=props.get("Uppery");
		this.x2=props.get("Width")+x1;
		this.y2=props.get("Height")+y1;
	}
	

}
