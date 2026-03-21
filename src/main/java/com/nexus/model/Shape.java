package com.nexus.model;

import javafx.scene.paint.Color;

public abstract class Shape implements iShape {
	protected double x1,y1,x2,y2;
	protected Color color;
	
	public void setPoints(double x1,double y1,double x2,double y2) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	@Override
	public void setColor(Color color) {
		this.color=color;
	}
}
