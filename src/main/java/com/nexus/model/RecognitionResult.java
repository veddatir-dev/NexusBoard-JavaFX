package com.nexus.model;

public class RecognitionResult {
    public String shapeType;
    public double minX, minY, maxX, maxY;

    public RecognitionResult(String type, double x1, double y1, double x2, double y2) {
        this.shapeType = type;
        this.minX = x1; this.minY = y1;
        this.maxX = x2; this.maxY = y2;
    }
}