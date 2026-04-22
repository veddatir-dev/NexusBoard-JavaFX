package com.nexus.controller;

import com.nexus.model.*;
import com.nexus.view.ThemeManager;
import com.nexus.network.ImageGenService;
import com.nexus.network.NetworkManager;
import com.nexus.network.NetworkCallback;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FXMLDocumentController implements NetworkCallback {
    
    // Core Data Structures
    private LayerManager layerManager;
    private Stack<ProjectFile> undoStack;
    private Stack<ProjectFile> redoStack;
    private iShape currentShape;
    private SelectionBox selectionBox;
    private String currentTool = "Draw";
    private double x1, y1;

    // FXML Controls - File & Theme
    @FXML private Canvas CanvasBox;
    @FXML private Button SaveProjectBtn;
    @FXML private Button LoadProjectBtn;
    @FXML private Button ExportBtn;
    @FXML private ComboBox<String> ThemeBox;
    
    // FXML Controls - Drawing
    @FXML private ComboBox<String> ShapeBox;
    @FXML private ColorPicker ColorBox;
    @FXML private ColorPicker FillColorBox;
    @FXML private Slider StrokeSizeSlider;
    
    // FXML Controls - New Tools
    @FXML private Button TextBtn;
    @FXML private Button EraserBtn;
    @FXML private Button SelectionBtn;
    @FXML private Slider RotationSlider;
    @FXML private Slider ScaleSlider;
    
    // FXML Controls - Edit & Navigation
    @FXML private Button UndoBtn;
    @FXML private Button RedoBtn;
    @FXML private Button ClearBtn;
    @FXML private Button ExitBtn;
    
    // FXML Controls - Layer Management
    @FXML private ListView<String> LayerList;
    @FXML private Button AddLayerBtn;
    @FXML private Button DeleteLayerBtn;
    @FXML private Button LayerUpBtn;
    @FXML private Button LayerDownBtn;
    
    // FXML Controls - Shape Management
    @FXML private ListView<String> ShapeList;
    @FXML private Button MoveBtn;
    @FXML private Button CopyBtn;
    @FXML private Button ResizeBtn;
    @FXML private Button DeleteShapeBtn;
    
    // FXML Controls - Network
    @FXML private TextField IPField;
    @FXML private Button HostBtn;
    @FXML private Button ConnectBtn;
    @FXML private Button AIBtn;
    
    // FXML Controls - Status
    @FXML private Label StatusLabel;
    
    @FXML
    public void initialize() {
        // Initialize layer manager and history
        layerManager = new LayerManager();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        selectionBox = new SelectionBox();
        
        // Setup shapes combo
        ObservableList<String> shapes = FXCollections.observableArrayList(
            "Line", "Dotted Line", "Double Line", "Arrow", "Rectangle", "Square", 
            "Circle", "Ellipse", "Triangle", "Freehand"
        );
        ShapeBox.setItems(shapes);
        ShapeBox.setValue("Freehand");
        
        // Setup theme combo
        ObservableList<String> themes = FXCollections.observableArrayList(
            ThemeManager.getAvailableThemes()
        );
        ThemeBox.setItems(themes);
        ThemeBox.setValue(ThemeManager.PROFESSIONAL_THEME);
        ThemeBox.setOnAction(e -> applyTheme());
        
        // Setup color pickers
        ColorBox.setValue(Color.BLACK);
        FillColorBox.setValue(Color.TRANSPARENT);
        
        // Canvas sizing
        CanvasBox.setWidth(900);
        CanvasBox.setHeight(600);
        
        // Update UI
        updateLayerList();
        updateShapeList();
        StatusLabel.setText("Ready - Nexus Board v3.0 Enhanced");
    }

    @FXML
    void startDrag(MouseEvent event) {
        x1 = event.getX();
        y1 = event.getY();
        
        if ("Text".equals(currentTool)) {
            handleTextToolDraw(event);
        } else if ("Eraser".equals(currentTool)) {
            currentShape = new Eraser();
            ((Eraser) currentShape).addPoint(x1, y1);
        } else if ("Select".equals(currentTool)) {
            // Selection tool logic
        } else {
            // Normal drawing
            String selected = ShapeBox.getValue();
            currentShape = ShapeFactory.getShape(selected);
            if (currentShape != null) {
                currentShape.setColor(ColorBox.getValue());
                
                if (currentShape instanceof Shape) {
                    Shape shape = (Shape) currentShape;
                    shape.setStrokeWidth(StrokeSizeSlider.getValue());
                    shape.setFillColor(FillColorBox.getValue());
                } else if (currentShape instanceof Freehand) {
                    ((Freehand) currentShape).setStrokeWidth(StrokeSizeSlider.getValue());
                    ((Freehand) currentShape).addPoint(x1, y1);
                }
            }
        }
    }

    @FXML
    void whileDragging(MouseEvent event) {
        if (currentShape instanceof Freehand) {
            ((Freehand) currentShape).addPoint(event.getX(), event.getY());
            redrawCanvas();
        } else if (currentShape instanceof Eraser) {
            ((Eraser) currentShape).addPoint(event.getX(), event.getY());
            redrawCanvas();
        }
    }

    @FXML
    void endDrag(MouseEvent event) {
        if (currentShape == null) return;
        
        if (currentShape instanceof Freehand) {
            handleFreehandEnd((Freehand) currentShape);
        } else if (currentShape instanceof Eraser) {
            // Eraser is drawn immediately
        } else if (currentShape instanceof Shape) {
            ((Shape) currentShape).setPoints(x1, y1, event.getX(), event.getY());
            ((Shape) currentShape).setStrokeWidth(StrokeSizeSlider.getValue());
            ((Shape) currentShape).setFillColor(FillColorBox.getValue());
            layerManager.addShapeToCurrentLayer(currentShape);
            NetworkManager.getInstance().broadcastShape(currentShape);
            updateShapeList();
        }
        
        redrawCanvas();
        currentShape = null;
    }

    private void handleFreehandEnd(Freehand freehand) {
        RecognitionResult result = GeometryAgent.recognize(freehand.getPointList());
        
        iShape finalShape;
        if (!result.shapeType.equals("Freehand")) {
            finalShape = ShapeFactory.getShape(result.shapeType);
            finalShape.setColor(ColorBox.getValue());
            if (finalShape instanceof Shape) {
                Shape shape = (Shape) finalShape;
                shape.setStrokeWidth(StrokeSizeSlider.getValue());
                shape.setFillColor(FillColorBox.getValue());
                shape.setPoints(result.minX, result.minY, result.maxX, result.maxY);
            }
        } else {
            finalShape = freehand;
            ((Freehand) finalShape).setStrokeWidth(StrokeSizeSlider.getValue());
        }
        
        layerManager.addShapeToCurrentLayer(finalShape);
        NetworkManager.getInstance().broadcastShape(finalShape);
    }

    private void handleTextToolDraw(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog("Enter text");
        dialog.setTitle("Add Text");
        dialog.setHeaderText("Enter the text to add to the canvas");
        dialog.setContentText("Text:");
        
        dialog.showAndWait().ifPresent(text -> {
            TextBox textBox = new TextBox();
            textBox.setPoints(x1, y1, x1 + 100, y1 + 30);
            textBox.setText(text);
            textBox.setColor(ColorBox.getValue());
            textBox.setFontSize(16.0);
            layerManager.addShapeToCurrentLayer(textBox);
            updateShapeList();
            redrawCanvas();
        });
    }

    private void redrawCanvas() {
        GraphicsContext gc = CanvasBox.getGraphicsContext2D();
        gc.clearRect(0, 0, CanvasBox.getWidth(), CanvasBox.getHeight());
        
        // Draw all layers
        layerManager.drawAllLayers(CanvasBox);
        
        // Draw selection box if active
        if (selectionBox.isActive()) {
            selectionBox.draw(CanvasBox);
        }
    }

    private void updateShapeList() {
        Layer currentLayer = layerManager.getCurrentLayer();
        if (currentLayer == null) return;
        
        ObservableList<String> items = FXCollections.observableArrayList();
        List<iShape> shapes = currentLayer.getShapes();
        for (int i = 0; i < shapes.size(); i++) {
            items.add((i + 1) + ". " + shapes.get(i).getClass().getSimpleName());
        }
        ShapeList.setItems(items);
    }

    private void updateLayerList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Layer layer : layerManager.getAllLayers()) {
            String icon = layer.isVisible() ? "👁" : "🚫";
            String lock = layer.isLocked() ? "🔒" : "🔓";
            items.add(icon + " " + lock + " " + layer.getName() + " (" + layer.getShapeCount() + ")");
        }
        LayerList.setItems(items);
    }

    // ===== FILE OPERATIONS =====
    @FXML
    void handleSaveProject(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Project");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Nexus Project", "*.nbx"));
        
        Stage stage = (Stage) CanvasBox.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);
        
        if (file != null) {
            try {
                ProjectFile project = ProjectManager.createProjectFromLayers(
                    "Drawing", layerManager, 
                    (int) CanvasBox.getWidth(), 
                    (int) CanvasBox.getHeight()
                );
                ProjectManager.saveProjectFile(project, file.getAbsolutePath());
                ProjectManager.saveRecentProject(
                    System.getProperty("user.home") + "/.nexusboard/recent.txt",
                    file.getAbsolutePath()
                );
                StatusLabel.setText("Project saved: " + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                StatusLabel.setText("Error saving project");
            }
        }
    }

    @FXML
    void handleLoadProject(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Project");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Nexus Project", "*.nbx"));
        
        Stage stage = (Stage) CanvasBox.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        
        if (file != null) {
            try {
                ProjectFile project = ProjectManager.loadProjectFile(file.getAbsolutePath());
                ProjectManager.loadProjectToLayers(project, layerManager);
                updateLayerList();
                updateShapeList();
                redrawCanvas();
                StatusLabel.setText("Project loaded: " + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                StatusLabel.setText("Error loading project");
            }
        }
    }

    @FXML
    void handleExport(ActionEvent event) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("PNG", "PNG", "JPEG", "BMP", "SVG");
        dialog.setTitle("Export");
        dialog.setHeaderText("Select export format:");
        dialog.setContentText("Format:");
        
        dialog.showAndWait().ifPresent(format -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Export Drawing");
            chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(format + " Image", "*" + ExportManager.getExtensionForFormat(format))
            );
            
            Stage stage = (Stage) CanvasBox.getScene().getWindow();
            File file = chooser.showSaveDialog(stage);
            
            if (file != null) {
                try {
                    switch (format.toLowerCase()) {
                        case "png":
                            ExportManager.exportToPNG(CanvasBox, file.getAbsolutePath());
                            break;
                        case "jpeg":
                            ExportManager.exportToJPEG(CanvasBox, file.getAbsolutePath());
                            break;
                        case "bmp":
                            ExportManager.exportToBMP(CanvasBox, file.getAbsolutePath());
                            break;
                        case "svg":
                            ExportManager.exportToSVG(CanvasBox, layerManager, file.getAbsolutePath());
                            break;
                    }
                    StatusLabel.setText("Exported to: " + file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    StatusLabel.setText("Error exporting");
                }
            }
        });
    }

    // ===== EDIT OPERATIONS =====
    @FXML
    void handleUndo(ActionEvent event) {
        if (layerManager.getCurrentLayer() != null && layerManager.getCurrentLayer().getShapeCount() > 0) {
            Layer currentLayer = layerManager.getCurrentLayer();
            currentLayer.removeShapeAt(currentLayer.getShapeCount() - 1);
            redrawCanvas();
            updateShapeList();
            NetworkManager.getInstance().broadcastCommand("UNDO");
        }
    }

    @FXML
    void handleRedo(ActionEvent event) {
        StatusLabel.setText("Redo: Feature coming soon");
    }

    @FXML
    void handleClear(ActionEvent event) {
        layerManager.clearAllLayers();
        redrawCanvas();
        updateShapeList();
        updateLayerList();
        NetworkManager.getInstance().broadcastCommand("CLEAR");
        StatusLabel.setText("Canvas cleared");
    }

    // ===== TOOL SELECTION =====
    @FXML
    void handleTextTool(ActionEvent event) {
        currentTool = "Text";
        StatusLabel.setText("Text tool active - Click to add text");
        TextBtn.setStyle("-fx-font-weight: bold; -fx-border-color: #00d4ff; -fx-border-width: 2;");
    }

    @FXML
    void handleEraserTool(ActionEvent event) {
        currentTool = "Eraser";
        StatusLabel.setText("Eraser tool active - Click and drag to erase");
        EraserBtn.setStyle("-fx-font-weight: bold; -fx-border-color: #00d4ff; -fx-border-width: 2;");
    }

    @FXML
    void handleSelectionTool(ActionEvent event) {
        currentTool = "Select";
        StatusLabel.setText("Selection tool active - Click to select shapes");
        SelectionBtn.setStyle("-fx-font-weight: bold; -fx-border-color: #00d4ff; -fx-border-width: 2;");
    }

    // ===== LAYER OPERATIONS =====
    @FXML
    void handleAddLayer(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("Layer " + (layerManager.getLayerCount() + 1));
        dialog.setTitle("New Layer");
        dialog.setHeaderText("Enter layer name:");
        dialog.setContentText("Name:");
        
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isEmpty()) {
                layerManager.createLayer(name);
                updateLayerList();
                StatusLabel.setText("Layer added: " + name);
            }
        });
    }

    @FXML
    void handleDeleteLayer(ActionEvent event) {
        if (layerManager.getLayerCount() > 1) {
            layerManager.deleteLayer(layerManager.getLayerCount() - 1);
            updateLayerList();
            updateShapeList();
            redrawCanvas();
            StatusLabel.setText("Layer deleted");
        } else {
            StatusLabel.setText("Cannot delete last layer");
        }
    }

    @FXML
    void handleLayerUp(ActionEvent event) {
        int index = LayerList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            layerManager.moveLayerUp(index);
            updateLayerList();
            redrawCanvas();
        }
    }

    @FXML
    void handleLayerDown(ActionEvent event) {
        int index = LayerList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            layerManager.moveLayerDown(index);
            updateLayerList();
            redrawCanvas();
        }
    }

    // ===== SHAPE OPERATIONS =====
    @FXML
    void handleDeleteShape(ActionEvent event) {
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        Layer currentLayer = layerManager.getCurrentLayer();
        if (index >= 0 && currentLayer != null) {
            currentLayer.removeShapeAt(index);
            updateShapeList();
            redrawCanvas();
            StatusLabel.setText("Shape deleted");
        }
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        StatusLabel.setText("Feature: Shape manipulation coming soon");
    }

    // ===== THEME MANAGEMENT =====
    private void applyTheme() {
        String selectedTheme = ThemeBox.getValue();
        ThemeManager.setCurrentTheme(selectedTheme);
        try {
            String cssUrl = ThemeManager.getThemeCSS(selectedTheme);
            CanvasBox.getScene().getStylesheets().clear();
            CanvasBox.getScene().getStylesheets().add(cssUrl);
            StatusLabel.setText("Theme: " + selectedTheme);
        } catch (Exception e) {
            e.printStackTrace();
            StatusLabel.setText("Error loading theme");
        }
    }

    // ===== NETWORKING =====
    @FXML
    void handleHost(ActionEvent event) {
        StatusLabel.setText("Hosting on port 5000...");
        NetworkManager.getInstance().startServer(this);
    }

    @FXML
    void handleConnect(ActionEvent event) {
        String targetIP = IPField.getText();
        StatusLabel.setText("Connecting to " + targetIP + "...");
        NetworkManager.getInstance().connectToPeer(targetIP, this);
    }

    @FXML
    void handleAIEnhance(ActionEvent event) {
        String shapeType = ShapeBox.getValue();
        if (shapeType == null || shapeType.isEmpty()) {
            shapeType = "creative Geometry";
        }
        String prompt = "A clean digital illustration of a " + shapeType;
        
        StatusLabel.setText("Generating AI image...");
        new Thread(() -> {
            String url = ImageGenService.generateImage(prompt);
            if (url != null) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    StatusLabel.setText("AI image opened in browser");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    // ===== NETWORK CALLBACKS =====
    @Override
    public void addExternalShape(iShape shape) {
        layerManager.addShapeToCurrentLayer(shape);
        redrawCanvas();
        updateShapeList();
        StatusLabel.setText("Shape received from peer");
    }

    @Override
    public void clearAllShapes() {
        layerManager.clearAllLayers();
        redrawCanvas();
        updateShapeList();
    }

    @Override
    public void undoLastShape() {
        handleUndo(null);
    }

    @Override
    public void updateConnectionStatus(String status) {
        StatusLabel.setText(status);
    }
}
