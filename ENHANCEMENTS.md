# Nexus Board v3.0 - Enhancement Documentation

## Overview
This document details all the UI enhancements, new drawing tools, advanced shape features, and export/import functionality added to Nexus Board.

---

## 1. UI ENHANCEMENTS & THEMES

### 1.1 Theme System
**Location:** `com.nexus.view.ThemeManager.java`

Four professional themes available:
- **Professional Theme**: Dark blue with cyan accents (default)
- **Light Theme**: Clean white with blue buttons
- **Dark Theme**: Pure black/dark gray with orange accents  
- **Modern Theme**: Gradient purple with pink accents

**How to use:**
- Select theme from dropdown in toolbar
- Themes applied instantly without restart
- CSS files located in `src/main/resources/css/`

**CSS Files:**
- `theme-professional.css` - Dark professional look
- `theme-light.css` - Bright, minimal design
- `theme-dark.css` - High contrast dark mode
- `theme-modern.css` - Gradient modern gradient design

### 1.2 Enhanced UI Layout
**New Features:**
- **Expanded Toolbar** - Two rows with organized sections:
  - File operations (Save, Open, Export)
  - Edit tools (Undo, Redo, Clear)
  - Theme selector
  - Shape tools with color/size controls
  - New drawing tools (Text, Eraser, Selection)
  - Transform controls (Rotation, Scaling)
  - Network & AI tools

- **Right Sidebar** with tabbed sections:
  - Layer management panel
  - Shape history
  - Selected shape transformation options

- **Status Bar** showing:
  - Current status messages
  - Zoom level
  - Layer count

---

## 2. NEW DRAWING TOOLS

### 2.1 Text Tool
**Class:** `com.nexus.model.TextBox.java`

**Features:**
- Add text at any position on canvas
- Configurable font size
- Text color controlled by stroke color picker
- Text alignment options (Left, Center, Right)
- Serializable for save/load operations

**Usage:**
1. Click "📝 Text" button in toolbar
2. Click on canvas where you want text
3. Enter text in dialog
4. Text appears on canvas

**Implementation Details:**
- Extends Shape base class
- Uses JavaFX Font rendering
- Properties stored for serialization

### 2.2 Eraser Tool
**Class:** `com.nexus.model.Eraser.java`

**Features:**
- Configurable eraser size
- Click and drag to erase areas
- Non-destructive erasing (clears only the overlapping pixels)
- Tracks points for smooth erasing

**Usage:**
1. Click "🧹 Eraser" button in toolbar
2. Click and drag on canvas to erase
3. Eraser size can be adjusted via stroke size slider

**Implementation Details:**
- Implements iShape interface
- Uses clearRect() for pixel clearing
- Maintains list of erased points

### 2.3 Selection Tool
**Class:** `com.nexus.model.SelectionBox.java`

**Features:**
- Select shapes on canvas
- Visual selection box with dashed outline
- Resize handles at corners
- Enable transformation of selected shapes
- Deselect by clicking outside

**Usage:**
1. Click "✓ Select" button in toolbar
2. Click on a shape to select it
3. Selection box appears with handles
4. Use transformation controls to modify

**Implementation Details:**
- Custom shape class for showing selection
- Stores reference to selected shape
- Provides resize handle positions

---

## 3. ADVANCED SHAPE FEATURES

### 3.1 Layer System
**Classes:**
- `com.nexus.model.Layer.java` - Individual layer
- `com.nexus.model.LayerManager.java` - Layer management

**Features:**
- **Unlimited Layers** - Create as many layers as needed
- **Layer Visibility** - Show/hide layers independently  
- **Layer Locking** - Prevent accidental edits
- **Z-Order Management** - Move layers up/down
- **Layer Naming** - Custom names for organization
- **Opacity Control** - Adjust layer transparency

**Usage:**
1. Click "+" button in Layers panel to add new layer
2. Right-click layer to set properties
3. Click eye icon to toggle visibility
4. Click lock icon to toggle lock state
5. Use up/down arrows to reorder layers

**Methods Available:**
```java
layerManager.createLayer(String name)
layerManager.deleteLayer(int index)
layerManager.moveLayerUp(int index)
layerManager.moveLayerDown(int index)
layerManager.toggleLayerVisibility(int index)
layerManager.toggleLayerLock(int index)
layerManager.setOpacity(double opacity)
```

### 3.2 Rotation & Scaling
**Class:** `com.nexus.model.TransformableShape.java`

**Features:**
- **Rotation** - Rotate shapes 0-360 degrees
- **Uniform Scaling** - Scale shapes uniformly
- **Non-Uniform Scaling** - Scale X and Y independently
- **Transform Center** - Define pivot point for rotation

**Usage:**
1. Select a shape using Selection Tool
2. Use "Rotate" slider to rotate (0-360°)
3. Use "Scale" slider to scale (0.1x - 2.0x)
4. Changes apply in real-time

**Implementation Details:**
```java
shape.setRotation(double degrees);        // 0-360
shape.setScaleUniform(double scale);      // 0.1-2.0
shape.setScale(double scaleX, double scaleY);
shape.rotateBy(double degrees);           // Relative rotation
```

### 3.3 Shape History
**Features:**
- **Real-Time List** - Shows all shapes in current layer
- **Selection** - Click to select and edit
- **Numbering** - Index shows creation order
- **Type Display** - Shows shape class name

---

## 4. EXPORT/IMPORT FUNCTIONALITY

### 4.1 Project Save/Load
**Classes:**
- `com.nexus.model.ProjectFile.java` - Project serialization
- `com.nexus.model.ProjectManager.java` - File I/O operations

**File Format:** `.nbx` (Nexus Board XML-based format)

**What Gets Saved:**
- All layers and their properties
- All shapes with their properties
- Layer visibility, lock, and opacity states
- Canvas dimensions
- Project metadata (creation date, modification date)

**Usage:**
```
File → Save    (Ctrl+S) - Save current project
File → Open    (Ctrl+O) - Load saved project
```

**Implementation:**
- Implements Serializable interface
- Uses Java ObjectOutputStream for persistence
- Maintains recent file history

**Code Example:**
```java
// Save project
ProjectFile project = ProjectManager.createProjectFromLayers(...);
ProjectManager.saveProjectFile(project, "/path/to/file.nbx");

// Load project
ProjectFile loaded = ProjectManager.loadProjectFile("/path/to/file.nbx");
ProjectManager.loadProjectToLayers(loaded, layerManager);
```

### 4.2 Export to Multiple Formats
**Class:** `com.nexus.model.ExportManager.java`

**Supported Formats:**

#### PNG (Portable Network Graphics)
- Lossless compression
- Supports transparency
- Best for sharing
- Perfect for web
```java
ExportManager.exportToPNG(canvas, "output.png");
```

#### JPEG (Joint Photographic Experts Group)
- Lossy compression
- Smaller file size
- No transparency
- Good for photos
```java
ExportManager.exportToJPEG(canvas, "output.jpg");
```

#### BMP (Bitmap)
- Uncompressed format
- Large file size
- Maximum quality
- Legacy format support
```java
ExportManager.exportToBMP(canvas, "output.bmp");
```

#### SVG (Scalable Vector Graphics)
- Vector format
- Infinitely scalable
- Text-based
- Editable in vector editors
```java
ExportManager.exportToSVG(canvas, layerManager, "output.svg");
```

**Usage:**
1. Click "📤 Export" button
2. Select desired format from dialog
3. Choose save location
4. File is exported immediately

**Dialog Flow:**
```
Export Button → Format Selection → File Chooser → Save
```

---

## 5. UPDATED FILE STRUCTURE

### New Model Classes
```
src/main/java/com/nexus/model/
├── TextBox.java              # Text drawing tool
├── Eraser.java               # Eraser tool
├── SelectionBox.java         # Selection tool
├── Layer.java                # Individual layer
├── LayerManager.java         # Layer management system
├── TransformableShape.java   # Rotation/scaling support
├── ProjectFile.java          # Project serialization
├── ProjectManager.java       # Save/load operations
└── ExportManager.java        # Multi-format export
```

### New View Classes
```
src/main/java/com/nexus/view/
└── ThemeManager.java         # Theme switching
```

### Enhanced CSS Files
```
src/main/resources/css/
├── style.css                 # Base styles
├── theme-professional.css    # Professional dark theme
├── theme-light.css           # Light theme
├── theme-dark.css            # Dark theme
└── theme-modern.css          # Modern gradient theme
```

### Updated FXML
```
src/main/resources/fxml/
└── MainView.fxml             # Enhanced UI with new controls
```

---

## 6. USAGE EXAMPLES

### Creating a Drawing with Layers
```java
// Layers are created automatically with LayerManager
// Add shapes to different layers
layerManager.createLayer("Background");
layerManager.createLayer("Main Drawing");
layerManager.createLayer("Annotations");

// Switch between layers
layerManager.setCurrentLayer("Main Drawing");

// Draw normally
```

### Using Text and Eraser
```java
// TextBox creation
TextBox text = new TextBox();
text.setPoints(100, 100, 300, 150);
text.setText("Hello World");
text.setFontSize(24.0);
text.setColor(Color.BLACK);
layerManager.addShapeToCurrentLayer(text);

// Eraser tool
Eraser eraser = new Eraser();
eraser.addPoint(150, 150);
eraser.setEraserSize(20.0);
```

### Rotation and Scaling
```java
// Note: Current shapes extend Shape, not TransformableShape
// TransformableShape is available for future shape extensions

// Example usage:
class MyTransformableRect extends TransformableShape {
    @Override
    public void draw(Canvas canvas) {
        // Drawing code with transform support
    }
}

MyTransformableRect rect = new MyTransformableRect();
rect.setRotation(45.0);           // 45 degrees
rect.setScaleUniform(1.5);        // 1.5x zoom
```

### Saving and Loading
```java
// Save current work
ProjectFile project = ProjectManager.createProjectFromLayers(
    "My Drawing", 
    layerManager, 
    (int)canvas.getWidth(), 
    (int)canvas.getHeight()
);
ProjectManager.saveProjectFile(project, "drawing.nbx");

// Load previous work
ProjectFile loaded = ProjectManager.loadProjectFile("drawing.nbx");
ProjectManager.loadProjectToLayers(loaded, layerManager);
redrawCanvas();
```

### Exporting Artwork
```java
// Export to PNG
ExportManager.exportToPNG(canvas, "artwork.png");

// Export to SVG (vector)
ExportManager.exportToSVG(canvas, layerManager, "artwork.svg");

// Export to JPEG (web)
ExportManager.exportToJPEG(canvas, "artwork.jpg");
```

---

## 7. KEYBOARD SHORTCUTS (Recommended)

| Shortcut | Action |
|----------|--------|
| Ctrl+S | Save Project |
| Ctrl+O | Open Project |
| Ctrl+E | Export |
| Ctrl+Z | Undo |
| Ctrl+Y | Redo |
| Ctrl+T | Text Tool |
| Ctrl+E (Alt+E) | Eraser Tool |
| Escape | Deselect / Cancel |

---

## 8. TECHNICAL IMPLEMENTATION NOTES

### Architecture Improvements
1. **Layer-Based Rendering** - Shapes organized by layers
2. **Observer Pattern** - UI updates automatically
3. **Factory Pattern** - ShapeFactory manages creation
4. **Serialization** - Objects saved/loaded automatically
5. **MVC Pattern** - Clean separation of concerns

### Performance Considerations
- **Layer Visibility** - Hidden layers not rendered
- **Efficient Redrawing** - Only visible layers drawn
- **Property Caching** - Transform properties cached
- **Memory Management** - Old projects unloaded

### Thread Safety
- Network operations run on separate threads
- UI updates marshaled to JavaFX thread
- No blocking operations on render thread

---

## 9. FUTURE ENHANCEMENTS

Potential features for future versions:
- [ ] Undo/Redo history with multiple steps
- [ ] Gradient fills
- [ ] Custom shape creation
- [ ] Image import/embedding
- [ ] Animation timeline
- [ ] Collaborative editing with real-time sync
- [ ] Keyboard shortcuts dialog
- [ ] Zoom and pan controls
- [ ] Pattern fills
- [ ] Group/ungroup shapes
- [ ] Alignment tools
- [ ] Measurement tools

---

## 10. TROUBLESHOOTING

### Layers Not Showing
- Ensure layer visibility is enabled (eye icon)
- Check current layer selection

### Export Not Working
- Verify file permissions
- Check disk space
- Ensure valid file path

### Theme Not Applied
- Reload application
- Check CSS file location
- Verify theme name in dropdown

### Text Not Visible
- Check text color vs canvas background
- Verify font size is reasonable
- Ensure text position on canvas

---

## Version History
- **v3.0** - Added themes, new tools, layers, export/import
- **v2.0** - Network collaboration & AI enhancement
- **v1.0** - Initial release with basic shapes

---

**Last Updated:** April 2026
**Maintainer:** Nexus Team
