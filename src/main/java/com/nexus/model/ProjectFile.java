package com.nexus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a complete project file that can be saved and loaded.
 * Contains all layers and their shapes.
 */
public class ProjectFile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String projectName;
    private long createdAt;
    private long lastModified;
    private List<SerializedLayer> layers;
    private int canvasWidth;
    private int canvasHeight;

    public ProjectFile() {
        this.layers = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.lastModified = System.currentTimeMillis();
    }

    public ProjectFile(String projectName, int width, int height) {
        this();
        this.projectName = projectName;
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    public void addLayer(SerializedLayer layer) {
        this.layers.add(layer);
        this.lastModified = System.currentTimeMillis();
    }

    public void setLayers(List<SerializedLayer> layers) {
        this.layers = layers;
        this.lastModified = System.currentTimeMillis();
    }

    // Getters and setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<SerializedLayer> getLayers() {
        return layers;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLastModified() {
        return lastModified;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int width) {
        this.canvasWidth = width;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int height) {
        this.canvasHeight = height;
    }

    /**
     * Inner class to represent a layer in serializable form
     */
    public static class SerializedLayer implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String name;
        public boolean visible;
        public boolean locked;
        public double opacity;
        public List<SerializedShape> shapes;

        public SerializedLayer(String name) {
            this.name = name;
            this.visible = true;
            this.locked = false;
            this.opacity = 1.0;
            this.shapes = new ArrayList<>();
        }

        public void addShape(SerializedShape shape) {
            this.shapes.add(shape);
        }
    }

    /**
     * Inner class to represent a shape in serializable form
     */
    public static class SerializedShape implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String shapeType;
        public Map<String, Double> properties;
        public String text; // For text shapes
        public double colorR, colorG, colorB, colorA;

        public SerializedShape(String shapeType) {
            this.shapeType = shapeType;
            this.colorR = 0;
            this.colorG = 0;
            this.colorB = 0;
            this.colorA = 1.0;
        }
    }
}
