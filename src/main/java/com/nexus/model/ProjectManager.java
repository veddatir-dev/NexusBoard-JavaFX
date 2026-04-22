package com.nexus.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading of project files.
 */
public class ProjectManager {
    
    public static void saveProjectFile(ProjectFile project, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(project);
        }
    }

    public static ProjectFile loadProjectFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ProjectFile) ois.readObject();
        }
    }

    public static ProjectFile createProjectFromLayers(String projectName, LayerManager layerManager, int width, int height) {
        ProjectFile project = new ProjectFile(projectName, width, height);
        
        for (Layer layer : layerManager.getAllLayers()) {
            ProjectFile.SerializedLayer serialLayer = new ProjectFile.SerializedLayer(layer.getName());
            serialLayer.visible = layer.isVisible();
            serialLayer.locked = layer.isLocked();
            serialLayer.opacity = layer.getOpacity();
            
            for (iShape shape : layer.getShapes()) {
                ProjectFile.SerializedShape serialShape = new ProjectFile.SerializedShape(shape.getClass().getSimpleName());
                serialShape.properties = shape.getProperties();
                
                // Handle text shapes specially
                if (shape instanceof TextBox) {
                    serialShape.text = ((TextBox) shape).getText();
                }
                
                serialLayer.addShape(serialShape);
            }
            
            project.addLayer(serialLayer);
        }
        
        return project;
    }

    public static void loadProjectToLayers(ProjectFile project, LayerManager layerManager) {
        layerManager.clearAllLayers();
        
        for (ProjectFile.SerializedLayer serialLayer : project.getLayers()) {
            Layer layer = new Layer(serialLayer.name, layerManager.getLayerCount());
            layer.setVisible(serialLayer.visible);
            layer.setLocked(serialLayer.locked);
            layer.setOpacity(serialLayer.opacity);
            
            for (ProjectFile.SerializedShape serialShape : serialLayer.shapes) {
                iShape shape = ShapeFactory.getShape(serialShape.shapeType);
                
                if (shape != null) {
                    shape.setProperties(serialShape.properties);
                    
                    if (shape instanceof TextBox && serialShape.text != null) {
                        ((TextBox) shape).setText(serialShape.text);
                    }
                    
                    layer.addShape(shape);
                }
            }
            
            if (layerManager.getLayerCount() > 0) {
                layerManager.deleteLayer(layerManager.getLayerCount() - 1);
            }
            layerManager.createLayer(layer.getName());
        }
    }

    public static List<String> getRecentProjects(String recentListPath) {
        List<String> recent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(recentListPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                recent.add(line);
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
        return recent;
    }

    public static void saveRecentProject(String recentListPath, String projectPath) {
        List<String> recent = getRecentProjects(recentListPath);
        recent.remove(projectPath); // Remove if already exists
        recent.add(0, projectPath); // Add to front
        
        if (recent.size() > 10) {
            recent = recent.subList(0, 10); // Keep only last 10
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(recentListPath))) {
            for (String path : recent) {
                writer.println(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
