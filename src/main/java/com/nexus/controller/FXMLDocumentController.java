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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    private String currentTool = "Freehand";
    private double x1, y1;
    
    // For Diagramming Connections and Dragging
    private DiagramShape selectedShapeForDrag = null;
    private DiagramShape connectionStartShape = null;
    private double dragOffsetX, dragOffsetY;

    // FXML Controls - Tabs & Main Area
    @FXML private StackPane TabStackPane;
    @FXML private AnchorPane WhiteboardTab;
    @FXML private Canvas GhostCanvas;
    @FXML private TextArea CodeEditorTab;
    @FXML private TextArea DocEditorTab;
    @FXML private Button TabWhiteboardBtn;
    @FXML private Button TabCodeBtn;
    @FXML private Button TabDocBtn;
    
    // FXML Controls - Top Bar
    @FXML private Canvas CanvasBox;
    @FXML private Button SaveProjectBtn;
    @FXML private Button LoadProjectBtn;
    @FXML private Button ExportBtn;
    @FXML private ToggleButton ThemeToggleBtn;
    
    // FXML Controls - 60px Sidebar Palette
    @FXML private Button SelectionBtn;
    @FXML private Button FreehandBtn;
    @FXML private Button LineBtn;
    @FXML private Button TextBtn;
    @FXML private Button EraserBtn;
    @FXML private Button PaletteClassBtn;
    @FXML private Button PaletteDecisionBtn;
    @FXML private Button PaletteActorBtn;
    
    // FXML Controls - Standard Shapes
    @FXML private ComboBox<String> ShapeSelector;
    
    // FXML Controls - Right Sidebar Properties
    @FXML private ColorPicker ColorBox;
    @FXML private ColorPicker FillColorBox;
    @FXML private Slider StrokeSizeSlider;
    @FXML private Slider ScaleSlider;
    @FXML private Slider RotationSlider;
    @FXML private Slider TranslateXSlider;
    @FXML private Slider TranslateYSlider;
    
    // FXML Controls - Layer Management
    @FXML private ListView<String> LayerList;
    @FXML private Button AddLayerBtn;
    @FXML private Button DeleteLayerBtn;
    @FXML private Button LayerUpBtn;
    @FXML private Button LayerDownBtn;
    
    // FXML Controls - Network
    @FXML private TextField IPField;
    @FXML private TextField PortField;
    @FXML private Button HostBtn;
    @FXML private Button ConnectBtn;
    @FXML private Button AIBtn;
    
    // FXML Controls - Status
    @FXML private Label StatusLabel;
    
    @FXML
    public void initialize() {
        layerManager = new LayerManager();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        selectionBox = new SelectionBox();
        
        ColorBox.setValue(Color.BLACK);
        FillColorBox.setValue(Color.TRANSPARENT);
        
        ScaleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select".equals(currentTool) && selectedShapeForDrag != null) {
                selectedShapeForDrag.setScale(newVal.doubleValue(), newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(selectedShapeForDrag);
            } else if ("Select".equals(currentTool) && currentShape instanceof Shape) {
                ((Shape) currentShape).setScale(newVal.doubleValue(), newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
        });
        
        RotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select".equals(currentTool) && selectedShapeForDrag != null) {
                selectedShapeForDrag.setRotation(newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(selectedShapeForDrag);
            } else if ("Select".equals(currentTool) && currentShape instanceof Shape) {
                ((Shape) currentShape).setRotation(newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
        });
        
        TranslateXSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select".equals(currentTool) && selectedShapeForDrag != null) {
                selectedShapeForDrag.setTransformTranslation(newVal.doubleValue(), TranslateYSlider.getValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(selectedShapeForDrag);
            } else if ("Select".equals(currentTool) && currentShape instanceof Shape) {
                ((Shape) currentShape).setTransformTranslation(newVal.doubleValue(), TranslateYSlider.getValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
        });
        
        TranslateYSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select".equals(currentTool) && selectedShapeForDrag != null) {
                selectedShapeForDrag.setTransformTranslation(TranslateXSlider.getValue(), newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(selectedShapeForDrag);
            } else if ("Select".equals(currentTool) && currentShape instanceof Shape) {
                ((Shape) currentShape).setTransformTranslation(TranslateXSlider.getValue(), newVal.doubleValue());
                redrawCanvas();
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
        });
        
        // Canvas sizing - ensure it scales but can also scroll
        CanvasBox.widthProperty().bind(WhiteboardTab.widthProperty());
        CanvasBox.heightProperty().bind(WhiteboardTab.heightProperty());
        GhostCanvas.widthProperty().bind(WhiteboardTab.widthProperty());
        GhostCanvas.heightProperty().bind(WhiteboardTab.heightProperty());
        
        // Initialize Shape Selector
        ObservableList<String> shapesList = FXCollections.observableArrayList(
            "Circle", "Rectangle", "Triangle", "Square", "Parallelogram", 
            "Rhombus", "Trapezoid", "Ellipse", "Equilateral Triangle", "Right Triangle"
        );
        ShapeSelector.setItems(shapesList);
        ShapeSelector.setOnAction(e -> {
            String selected = ShapeSelector.getValue();
            if (selected != null) {
                // Remove spaces to match ShapeFactory keys (e.g. "Right Triangle" -> "RightTriangle")
                setActiveTool(null, selected.replace(" ", ""));
                // Reset button borders since we are using combo box
                resetToolStyles();
                ShapeSelector.setStyle("-fx-border-color: #00e5ff; -fx-border-width: 2px; -fx-border-radius: 5px;");
            }
        });
        
        // DND for UML Palette
        PaletteClassBtn.setOnDragDetected(e -> handleDragDetected(e, "UMLClass", PaletteClassBtn));
        PaletteDecisionBtn.setOnDragDetected(e -> handleDragDetected(e, "Decision", PaletteDecisionBtn));
        PaletteActorBtn.setOnDragDetected(e -> handleDragDetected(e, "Actor", PaletteActorBtn));
        
        CanvasBox.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(javafx.scene.input.TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });
        
        CanvasBox.setOnDragDropped(e -> {
            javafx.scene.input.Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String shapeType = db.getString();
                iShape droppedShape = ShapeFactory.getShape(shapeType);
                if (droppedShape instanceof Shape) {
                    double x = snap10(e.getX());
                    double y = snap10(e.getY());
                    double width = ("UMLClass".equals(shapeType)) ? 150 : 100;
                    double height = ("UMLClass".equals(shapeType)) ? 150 : 100;
                    ((Shape) droppedShape).setPoints(x - width/2, y - height/2, x + width/2, y + height/2);
                    droppedShape.setColor(ColorBox.getValue());
                    ((Shape) droppedShape).setFillColor(FillColorBox.getValue());
                    ((Shape) droppedShape).setStrokeWidth(StrokeSizeSlider.getValue());
                    layerManager.addShapeToCurrentLayer(droppedShape);
                    NetworkManager.getInstance().broadcastShape(droppedShape);
                    redrawCanvas();
                    success = true;
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });
        
        // Sync text editors
        CodeEditorTab.textProperty().addListener((obs, oldText, newText) -> {
            if (!NetworkManager.getInstance().isRemoteChange) {
                NetworkManager.getInstance().broadcastText("TEXT_CODE", newText);
            }
        });
        
        DocEditorTab.textProperty().addListener((obs, oldText, newText) -> {
            if (!NetworkManager.getInstance().isRemoteChange) {
                NetworkManager.getInstance().broadcastText("TEXT_DOC", newText);
            }
        });
        
        updateLayerList();
        StatusLabel.setText("Ready - Nexus Board v3.0 Developer Edition");
    }

    private void handleDragDetected(MouseEvent event, String shapeType, Button btn) {
        javafx.scene.input.Dragboard db = btn.startDragAndDrop(javafx.scene.input.TransferMode.COPY);
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(shapeType);
        db.setContent(content);
        event.consume();
    }

    @FXML
    void handleTabSwitch(ActionEvent event) {
        Button source = (Button) event.getSource();
        WhiteboardTab.setVisible(false);
        CodeEditorTab.setVisible(false);
        DocEditorTab.setVisible(false);
        
        if (source == TabWhiteboardBtn) WhiteboardTab.setVisible(true);
        else if (source == TabCodeBtn) CodeEditorTab.setVisible(true);
        else if (source == TabDocBtn) DocEditorTab.setVisible(true);
    }
    
    @FXML
    void handleThemeToggle(ActionEvent event) {
        if (ThemeToggleBtn.isSelected()) {
            ThemeManager.setCurrentTheme(ThemeManager.LIGHT_MODERN);
            ThemeToggleBtn.setText("🌞 Light Mode");
        } else {
            ThemeManager.setCurrentTheme(ThemeManager.PROFESSIONAL_DARK);
            ThemeToggleBtn.setText("🌙 Dark Mode");
        }
        
        try {
            String cssUrl = ThemeManager.getThemeCSS(ThemeManager.getCurrentTheme());
            CanvasBox.getScene().getStylesheets().clear();
            CanvasBox.getScene().getStylesheets().add(cssUrl);
            StatusLabel.setText("Theme: " + ThemeManager.getCurrentTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== TOOLS PALETTE ACTIONS =====
    private void resetToolStyles() {
        String defaultStyle = "-fx-border-width: 0;";
        SelectionBtn.setStyle(defaultStyle);
        FreehandBtn.setStyle(defaultStyle);
        LineBtn.setStyle(defaultStyle);
        TextBtn.setStyle(defaultStyle);
        EraserBtn.setStyle(defaultStyle);
        PaletteClassBtn.setStyle(defaultStyle);
        PaletteDecisionBtn.setStyle(defaultStyle);
        PaletteActorBtn.setStyle(defaultStyle);
        if (ShapeSelector != null) {
            ShapeSelector.setStyle(defaultStyle);
        }
    }

    private void setActiveTool(Button btn, String toolName) {
        resetToolStyles();
        if (btn != null) {
            btn.setStyle("-fx-border-color: #00e5ff; -fx-border-width: 2px; -fx-border-radius: 5px;");
        }
        currentTool = toolName;
        StatusLabel.setText("Tool Active: " + toolName);
    }

    @FXML void handleSelectionTool(ActionEvent event) { setActiveTool(SelectionBtn, "Select"); }
    @FXML void handleFreehandTool(ActionEvent event) { setActiveTool(FreehandBtn, "Freehand"); }
    @FXML void handleLineTool(ActionEvent event) { setActiveTool(LineBtn, "Line"); }
    @FXML void handleTextTool(ActionEvent event) { setActiveTool(TextBtn, "Text"); }
    @FXML void handleEraserTool(ActionEvent event) { setActiveTool(EraserBtn, "Eraser"); }
    @FXML void handleClassTool(ActionEvent event) { setActiveTool(PaletteClassBtn, "UMLClass"); }
    @FXML void handleDecisionTool(ActionEvent event) { setActiveTool(PaletteDecisionBtn, "Decision"); }
    @FXML void handleActorTool(ActionEvent event) { setActiveTool(PaletteActorBtn, "Actor"); }

    // ===== MOUSE EVENTS =====
    
    private double snap10(double val) {
        return Math.round(val / 10.0) * 10.0;
    }

    private DiagramShape findDiagramShapeAt(double x, double y) {
        if (layerManager.getCurrentLayer() == null) return null;
        List<iShape> shapes = layerManager.getCurrentLayer().getShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            iShape s = shapes.get(i);
            if (s instanceof DiagramShape) {
                DiagramShape ds = (DiagramShape) s;
                if (x >= ds.getMinX() && x <= ds.getMaxX() && y >= ds.getMinY() && y <= ds.getMaxY()) {
                    return ds;
                }
            }
        }
        return null;
    }

    @FXML
    void startDrag(MouseEvent event) {
        x1 = snap10(event.getX());
        y1 = snap10(event.getY());
        
        if (event.getClickCount() == 2 && "Select".equals(currentTool)) {
            iShape clickedShape = null;
            if (layerManager.getCurrentLayer() != null) {
                List<iShape> shapes = layerManager.getCurrentLayer().getShapes();
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    iShape s = shapes.get(i);
                    if (s instanceof Shape && ((Shape)s).hitTest(x1, y1)) {
                        clickedShape = s;
                        break;
                    }
                }
            }
            if (clickedShape != null) {
                handleUmlTextEdit(clickedShape, x1, y1);
                return;
            }
        }
        
        if ("Select".equals(currentTool)) {
            selectedShapeForDrag = findDiagramShapeAt(x1, y1);
            if (selectedShapeForDrag != null) {
                dragOffsetX = selectedShapeForDrag.getMinX() - x1;
                dragOffsetY = selectedShapeForDrag.getMinY() - y1;
            } else {
                if (layerManager.getCurrentLayer() != null) {
                    List<iShape> shapes = layerManager.getCurrentLayer().getShapes();
                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        iShape s = shapes.get(i);
                        if (s instanceof Shape && ((Shape)s).hitTest(x1, y1)) {
                            currentShape = s;
                            dragOffsetX = x1;
                            dragOffsetY = y1;
                            break;
                        }
                    }
                }
            }
        } else if ("Line".equals(currentTool)) {
            connectionStartShape = findDiagramShapeAt(x1, y1);
            if (connectionStartShape == null) {
                // Normal free line if no shape found
                currentShape = new Line();
                ((Shape) currentShape).setPoints(x1, y1, x1, y1);
                ((Shape) currentShape).setColor(ColorBox.getValue());
                ((Shape) currentShape).setStrokeWidth(StrokeSizeSlider.getValue());
            }
        } else if ("Text".equals(currentTool)) {
            handleTextToolDraw(x1, y1);
        } else if ("Eraser".equals(currentTool)) {
            currentShape = new Eraser();
            ((Eraser) currentShape).addPoint(x1, y1);
            ((Eraser) currentShape).setEraserSize(StrokeSizeSlider.getValue() * 10);
        } else if ("Freehand".equals(currentTool)) {
            currentShape = new Freehand();
            ((Freehand) currentShape).setStrokeWidth(StrokeSizeSlider.getValue());
            ((Freehand) currentShape).setColor(ColorBox.getValue());
            ((Freehand) currentShape).addPoint(x1, y1);
        } else {
            // Palette Dragging (UMLClass, Decision, Actor)
            currentShape = ShapeFactory.getShape(currentTool);
            if (currentShape != null) {
                currentShape.setColor(ColorBox.getValue());
                if (currentShape instanceof Shape) {
                    Shape shape = (Shape) currentShape;
                    shape.setStrokeWidth(StrokeSizeSlider.getValue());
                    shape.setFillColor(FillColorBox.getValue());
                    shape.setPoints(x1, y1, x1, y1);
                }
            }
        }
    }

    @FXML
    void whileDragging(MouseEvent event) {
        double x2 = snap10(event.getX());
        double y2 = snap10(event.getY());
        
        GraphicsContext gcGhost = GhostCanvas.getGraphicsContext2D();
        gcGhost.clearRect(0, 0, GhostCanvas.getWidth(), GhostCanvas.getHeight());
        
        if ("Select".equals(currentTool)) {
            if (selectedShapeForDrag != null) {
                double width = selectedShapeForDrag.getMaxX() - selectedShapeForDrag.getMinX();
                double height = selectedShapeForDrag.getMaxY() - selectedShapeForDrag.getMinY();
                selectedShapeForDrag.setPoints(x2 + dragOffsetX, y2 + dragOffsetY, x2 + dragOffsetX + width, y2 + dragOffsetY + height);
                redrawCanvas(); 
                return;
            } else if (currentShape instanceof Shape) {
                double dx = x2 - dragOffsetX;
                double dy = y2 - dragOffsetY;
                ((Shape) currentShape).translate(dx, dy);
                dragOffsetX = x2;
                dragOffsetY = y2;
                redrawCanvas();
                return;
            }
        }
        
        if ("Line".equals(currentTool) && connectionStartShape != null) {
            gcGhost.setStroke(ColorBox.getValue());
            gcGhost.setLineWidth(StrokeSizeSlider.getValue());
            gcGhost.strokeLine(connectionStartShape.getAnchorRightX(), connectionStartShape.getAnchorRightY(), x2, y2);
            return;
        }
        
        if (currentShape == null) return;
        
        if (currentShape instanceof Freehand) {
            ((Freehand) currentShape).addPoint(event.getX(), event.getY());
            currentShape.draw(GhostCanvas);
        } else if (currentShape instanceof Eraser) {
            ((Eraser) currentShape).addPoint(event.getX(), event.getY());
            currentShape.draw(CanvasBox); 
        } else if (currentShape instanceof Shape) {
            if (currentShape instanceof DiagramShape) {
                // Fixed default dimensions for diagram shapes during drag preview
                double width = ("UMLClass".equals(currentTool)) ? 150 : 100;
                double height = ("UMLClass".equals(currentTool)) ? 150 : 100;
                ((Shape) currentShape).setPoints(x2 - width/2, y2 - height/2, x2 + width/2, y2 + height/2);
            } else {
                ((Shape) currentShape).setPoints(x1, y1, x2, y2);
            }
            currentShape.draw(GhostCanvas);
        }
    }

    @FXML
    void endDrag(MouseEvent event) {
        double x2 = snap10(event.getX());
        double y2 = snap10(event.getY());
        
        GraphicsContext gcGhost = GhostCanvas.getGraphicsContext2D();
        gcGhost.clearRect(0, 0, GhostCanvas.getWidth(), GhostCanvas.getHeight());
        
        if ("Select".equals(currentTool)) {
            if (selectedShapeForDrag != null) {
                NetworkManager.getInstance().broadcastShape(selectedShapeForDrag);
            } else if (currentShape instanceof Shape) {
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
            return;
        }
        
        if ("Line".equals(currentTool) && connectionStartShape != null) {
            DiagramShape connectionEndShape = findDiagramShapeAt(x2, y2);
            if (connectionEndShape != null && connectionEndShape != connectionStartShape) {
                ConnectorLine connector = new ConnectorLine();
                connector.bind(connectionStartShape, connectionEndShape, "Right", "Left");
                connector.setColor(ColorBox.getValue());
                connector.setStrokeWidth(StrokeSizeSlider.getValue());
                connector.setLineType("Solid");
                
                layerManager.addShapeToCurrentLayer(connector);
                NetworkManager.getInstance().broadcastConnectionEvent(
                    connectionStartShape.getId(), 
                    connectionEndShape.getId(), 
                    "Solid"
                );
            }
            connectionStartShape = null;
            redrawCanvas();
            return;
        }

        if (currentShape == null) return;
        
        if (currentShape instanceof Freehand) {
            Freehand freehand = (Freehand) currentShape;
            RecognitionResult result = GeometryAgent.recognize(freehand.getPointList());
            
            if (!"Freehand".equals(result.shapeType)) {
                iShape recognizedShape = ShapeFactory.getShape(result.shapeType);
                if (recognizedShape instanceof Shape) {
                    ((Shape) recognizedShape).setPoints(result.minX, result.minY, result.maxX, result.maxY);
                    recognizedShape.setColor(ColorBox.getValue());
                    ((Shape) recognizedShape).setFillColor(FillColorBox.getValue());
                    ((Shape) recognizedShape).setStrokeWidth(StrokeSizeSlider.getValue());
                    layerManager.addShapeToCurrentLayer(recognizedShape);
                    NetworkManager.getInstance().broadcastShape(recognizedShape);
                } else {
                    layerManager.addShapeToCurrentLayer(currentShape);
                    NetworkManager.getInstance().broadcastShape(currentShape);
                }
            } else {
                layerManager.addShapeToCurrentLayer(currentShape);
                NetworkManager.getInstance().broadcastShape(currentShape);
            }
        } else if (currentShape instanceof Eraser) {
            layerManager.addShapeToCurrentLayer(currentShape);
            NetworkManager.getInstance().broadcastShape(currentShape);
        } else if (currentShape instanceof Shape) {
            if (currentShape instanceof DiagramShape) {
                double width = ("UMLClass".equals(currentTool)) ? 150 : 100;
                double height = ("UMLClass".equals(currentTool)) ? 150 : 100;
                ((Shape) currentShape).setPoints(x2 - width/2, y2 - height/2, x2 + width/2, y2 + height/2);
            } else {
                ((Shape) currentShape).setPoints(x1, y1, x2, y2);
            }
            layerManager.addShapeToCurrentLayer(currentShape);
            NetworkManager.getInstance().broadcastShape(currentShape);
        }
        
        redrawCanvas();
        currentShape = null;
    }

    private void handleTextToolDraw(double x, double y) {
        TextField overlay = new TextField();
        overlay.setLayoutX(x);
        overlay.setLayoutY(y);
        overlay.setPrefWidth(150);
        overlay.setPromptText("Enter text...");
        WhiteboardTab.getChildren().add(overlay);
        overlay.requestFocus();

        overlay.setOnAction(e -> finalizeTextOverlay(overlay, x, y));
        overlay.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                finalizeTextOverlay(overlay, x, y);
            }
        });
    }

    private void finalizeTextOverlay(TextField overlay, double x, double y) {
        if (!WhiteboardTab.getChildren().contains(overlay)) return;
        WhiteboardTab.getChildren().remove(overlay);
        String text = overlay.getText();
        if (text != null && !text.trim().isEmpty()) {
            TextBox textBox = new TextBox();
            textBox.setPoints(x, y, x + 100, y + 30);
            textBox.setText(text);
            textBox.setColor(ColorBox.getValue());
            textBox.setFontSize(StrokeSizeSlider.getValue() * 2 + 10);
            layerManager.addShapeToCurrentLayer(textBox);
            redrawCanvas();
            NetworkManager.getInstance().broadcastShape(textBox);
        }
    }

    private void handleUmlTextEdit(iShape shape, double x, double y) {
        TextArea overlay = new TextArea(shape.getText());
        overlay.setLayoutX(x);
        overlay.setLayoutY(y);
        overlay.setPrefWidth(200);
        overlay.setPrefHeight(150);
        overlay.setWrapText(true);
        WhiteboardTab.getChildren().add(overlay);
        overlay.requestFocus();
        
        overlay.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (WhiteboardTab.getChildren().contains(overlay)) {
                    WhiteboardTab.getChildren().remove(overlay);
                    shape.setText(overlay.getText());
                    redrawCanvas();
                    NetworkManager.getInstance().broadcastShape(shape);
                }
            }
        });
    }

    private void redrawCanvas() {
        GraphicsContext gc = CanvasBox.getGraphicsContext2D();
        gc.clearRect(0, 0, CanvasBox.getWidth(), CanvasBox.getHeight());
        layerManager.drawAllLayers(CanvasBox);
    }

    // ===== FILE OPERATIONS =====
    @FXML
    void handleSaveProject(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save NexusBoard Project");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("NexusBoard Files (*.nbx)", "*.nbx"));
        File file = fileChooser.showSaveDialog(CanvasBox.getScene().getWindow());
        
        if (file != null) {
            try {
                ProjectFile project = ProjectManager.createProjectFromLayers(file.getName(), layerManager, (int)CanvasBox.getWidth(), (int)CanvasBox.getHeight());
                ProjectManager.saveProjectFile(project, file.getAbsolutePath());
                StatusLabel.setText("Project saved successfully to " + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                StatusLabel.setText("Failed to save project.");
            }
        }
    }

    @FXML
    void handleLoadProject(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NexusBoard Project");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("NexusBoard Files (*.nbx)", "*.nbx"));
        File file = fileChooser.showOpenDialog(CanvasBox.getScene().getWindow());
        
        if (file != null) {
            try {
                ProjectFile project = ProjectManager.loadProjectFile(file.getAbsolutePath());
                ProjectManager.loadProjectToLayers(project, layerManager);
                redrawCanvas();
                StatusLabel.setText("Project loaded successfully from " + file.getName());
                updateLayerList();
            } catch (Exception e) {
                e.printStackTrace();
                StatusLabel.setText("Failed to load project.");
            }
        }
    }

    @FXML
    void handleExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Canvas");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png"),
            new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(CanvasBox.getScene().getWindow());
        
        if (file != null) {
            try {
                String path = file.getAbsolutePath();
                if (path.toLowerCase().endsWith(".png")) {
                    ExportManager.exportToPNG(CanvasBox, path);
                } else if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg")) {
                    ExportManager.exportToJPEG(CanvasBox, path);
                }
                StatusLabel.setText("Exported successfully to " + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                StatusLabel.setText("Failed to export image.");
            }
        }
    }

    @FXML
    void handleClear(ActionEvent event) {
        layerManager.clearAllLayers();
        redrawCanvas();
        NetworkManager.getInstance().broadcastCommand("CLEAR");
        StatusLabel.setText("Canvas cleared");
    }

    // ===== LAYER OPERATIONS =====
    @FXML void handleAddLayer(ActionEvent event) { layerManager.createLayer("Layer"); updateLayerList(); }
    @FXML void handleDeleteLayer(ActionEvent event) { layerManager.deleteLayer(layerManager.getLayerCount() - 1); updateLayerList(); redrawCanvas(); }
    @FXML void handleLayerUp(ActionEvent event) { }
    @FXML void handleLayerDown(ActionEvent event) { }
    
    private void updateLayerList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Layer layer : layerManager.getAllLayers()) {
            items.add("👁 🔓 " + layer.getName() + " (" + layer.getShapeCount() + ")");
        }
        LayerList.setItems(items);
    }

    // ===== NETWORKING =====
    @FXML
    void handleHost(ActionEvent event) {
        int port = 5000;
        try {
            port = Integer.parseInt(PortField.getText());
        } catch (NumberFormatException e) {
            // keep 5000
        }
        StatusLabel.setText("Hosting on port " + port + "...");
        NetworkManager.getInstance().startServer(port, this);
    }

    @FXML
    void handleConnect(ActionEvent event) {
        String targetIP = IPField.getText();
        int port = 5000;
        try {
            port = Integer.parseInt(PortField.getText());
        } catch (NumberFormatException e) {
            // keep 5000
        }
        StatusLabel.setText("Connecting to " + targetIP + ":" + port + "...");
        NetworkManager.getInstance().connectToPeer(targetIP, port, this);
    }

    @FXML
    void handleAIEnhance(ActionEvent event) {
        StatusLabel.setText("AI logic activated.");
    }

    // ===== NETWORK CALLBACKS =====
    @Override
    public void addExternalShape(iShape shape) {
        layerManager.addShapeToCurrentLayer(shape);
        redrawCanvas();
        StatusLabel.setText("Shape received from peer");
    }

    @Override
    public void clearAllShapes() {
        layerManager.clearAllLayers();
        redrawCanvas();
    }

    @Override
    public void undoLastShape() { }

    @Override
    public void updateConnectionStatus(String status) {
        StatusLabel.setText(status);
    }
    
    @Override
    public void updateText(String type, String text) {
        if ("TEXT_CODE".equals(type)) CodeEditorTab.setText(text);
        else if ("TEXT_DOC".equals(type)) DocEditorTab.setText(text);
    }
}
