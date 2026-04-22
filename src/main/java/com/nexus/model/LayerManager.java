package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all layers in the drawing canvas.
 * Handles layer ordering, visibility, and rendering.
 */
public class LayerManager {
    private List<Layer> layers;
    private int currentLayerIndex;

    public LayerManager() {
        this.layers = new ArrayList<>();
        this.currentLayerIndex = -1;
        // Create default background layer
        createLayer("Background");
    }

    public void createLayer(String name) {
        int newZIndex = layers.size();
        Layer layer = new Layer(name, newZIndex);
        layers.add(layer);
        currentLayerIndex = layers.size() - 1;
    }

    public void deleteLayer(int index) {
        if (layers.size() > 1 && index >= 0 && index < layers.size()) {
            layers.remove(index);
            if (currentLayerIndex >= layers.size()) {
                currentLayerIndex = layers.size() - 1;
            }
        }
    }

    public void deleteLayer(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getName().equals(name)) {
                deleteLayer(i);
                break;
            }
        }
    }

    public void moveLayerUp(int index) {
        if (index < layers.size() - 1) {
            Layer temp = layers.get(index);
            layers.set(index, layers.get(index + 1));
            layers.set(index + 1, temp);
            updateZIndices();
        }
    }

    public void moveLayerDown(int index) {
        if (index > 0) {
            Layer temp = layers.get(index);
            layers.set(index, layers.get(index - 1));
            layers.set(index - 1, temp);
            updateZIndices();
        }
    }

    public void addShapeToCurrentLayer(iShape shape) {
        if (currentLayerIndex >= 0 && currentLayerIndex < layers.size()) {
            layers.get(currentLayerIndex).addShape(shape);
        }
    }

    public void drawAllLayers(Canvas canvas) {
        for (Layer layer : layers) {
            layer.drawLayer(canvas);
        }
    }

    public Layer getCurrentLayer() {
        if (currentLayerIndex >= 0 && currentLayerIndex < layers.size()) {
            return layers.get(currentLayerIndex);
        }
        return null;
    }

    public void setCurrentLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            this.currentLayerIndex = index;
        }
    }

    public void setCurrentLayer(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getName().equals(name)) {
                currentLayerIndex = i;
                break;
            }
        }
    }

    public void toggleLayerVisibility(int index) {
        if (index >= 0 && index < layers.size()) {
            Layer layer = layers.get(index);
            layer.setVisible(!layer.isVisible());
        }
    }

    public void toggleLayerLock(int index) {
        if (index >= 0 && index < layers.size()) {
            Layer layer = layers.get(index);
            layer.setLocked(!layer.isLocked());
        }
    }

    public ObservableList<Layer> getLayersList() {
        return FXCollections.observableArrayList(layers);
    }

    public List<Layer> getAllLayers() {
        return new ArrayList<>(layers);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public void clearAllLayers() {
        for (Layer layer : layers) {
            layer.clear();
        }
    }

    private void updateZIndices() {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).setZIndex(i);
        }
    }
}
