# Nexus Board v3.0 - Quick Start Guide

## Installation & Setup

### Prerequisites
- Java 17 or higher
- JavaFX 21 SDK
- Maven (for building)

### Build Instructions
```bash
cd Board
mvn clean package
```

### Run Application
```bash
java -jar target/Board-0.0.1-SNAPSHOT.jar
```

---

## Main Features at a Glance

### 🎨 Drawing Tools
| Tool | How to Use | Shortcut |
|------|-----------|----------|
| **Freehand** | Draw freely on canvas | Default |
| **Shapes** | Select shape, click and drag | Shape dropdown |
| **Text** | Click "Text" button, click canvas, enter text | Ctrl+T |
| **Eraser** | Click eraser, click and drag to erase | Ctrl+E |
| **Selection** | Click select, click shapes to select | Escape |

### 📁 File Operations
| Operation | Button | How to Use |
|-----------|--------|-----------|
| **Save** | 💾 Save | Click to save project |
| **Load** | 📂 Open | Click to open saved project |
| **Export** | 📤 Export | Click to export as PNG/JPEG/BMP/SVG |

### 🎯 Editing
| Operation | How to Use |
|-----------|-----------|
| **Undo** | Click ↶ or press Ctrl+Z |
| **Redo** | Click ↷ or press Ctrl+Y |
| **Clear** | Click 🗑 to clear canvas |

### 🎭 Themes
1. Click theme dropdown in toolbar
2. Select theme:
   - **Professional** - Dark with cyan (default)
   - **Light** - Bright and clean
   - **Dark** - High contrast
   - **Modern** - Colorful gradient

### 📚 Layers
| Action | How to Do It |
|--------|-------------|
| **Add Layer** | Click + button in Layers panel |
| **Delete** | Select layer, click - button |
| **Move Up** | Select layer, click ↑ button |
| **Move Down** | Select layer, click ↓ button |
| **Toggle Visibility** | Click 👁 icon next to layer name |
| **Toggle Lock** | Click 🔒 icon next to layer name |

### 🔄 Transform Shapes
1. Click "Select tool" button
2. Click a shape to select it
3. Use sliders:
   - **Rotate** - 0° to 360°
   - **Scale** - 0.1x to 2.0x

### 🌐 Network Features
| Feature | How to Use |
|---------|-----------|
| **Host** | Click "Host" button to start server |
| **Connect** | Enter IP, click "Connect" to join |
| **AI Image** | Select shape, click "AI" to generate image |

---

## File Formats

### Save Format (`.nbx`)
- Saves all layers and shapes
- Preserves layer properties
- Compatible with v3.0+
- Can be reopened and edited

### Export Formats
| Format | Best For | Quality |
|--------|----------|---------|
| **PNG** | Sharing, web, transparent | Lossless |
| **JPEG** | Photos, web, compression | Lossy |
| **BMP** | Archive, maximum quality | Lossless |
| **SVG** | Scaling, editing, vector | Vector |

---

## Tips & Tricks

### 1. Organize with Layers
- Use separate layers for different elements
- Name layers meaningfully
- Lock finished layers to prevent accidents

### 2. Smart Color Selection
- Click on stroke/fill color pickers
- Use saved colors in palette
- Press swatch to set as default

### 3. Text Formatting
- Adjust font size with Stroke Size slider
- Use stroke color picker for text color
- Text font is System (Arial-like)

### 4. Eraser Size
- Stroke Size slider controls eraser size
- Larger size for quick erasing
- Smaller size for precision

### 5. Exporting for Web
- Use PNG for highest quality
- Use JPEG for smaller file size
- Use SVG for scalable graphics

### 6. Backup Your Work
- Save project files regularly (.nbx)
- Export finished work to PNG/JPEG
- Use different project names for versions

### 7. Keyboard Shortcuts
- **Ctrl+S** - Quick save
- **Ctrl+O** - Quick open
- **Ctrl+Z** - Undo quickly
- **Escape** - Deselect current tool

---

## Troubleshooting

### "Cannot Find JavaFX"
```bash
# Set JAVAFX_HOME before running
export JAVAFX_HOME=/path/to/javafx-sdk
java -jar Board-0.0.1-SNAPSHOT.jar
```

### Shapes Not Appearing
1. Check if shape tool is selected
2. Verify stroke color is visible on background
3. Check layer visibility

### Text Not Visible
1. Verify text color (use Color Picker)
2. Check if text is positioned on canvas
3. Increase font size

### Export Fails
1. Check file write permissions
2. Ensure disk has space
3. Try different format
4. Check file path validity

### Theme Won't Change
1. Restart application
2. Verify CSS files exist
3. Check theme dropdown

---

## Keyboard Shortcuts Quick Reference

```
GENERAL:
Escape ................. Deselect/Cancel

FILE:
Ctrl+S ................. Save Project
Ctrl+O ................. Open Project
Ctrl+E ................. Export

EDITING:
Ctrl+Z ................. Undo
Ctrl+Y ................. Redo
Ctrl+A ................. Select All
Delete ................. Delete Selected

TOOLS:
T ....................... Text Tool
E ....................... Eraser Tool
S ....................... Selection Tool

NAVIGATION:
Tab ..................... Next Tool
Shift+Tab ............... Previous Tool
```

---

## Project File Structure

```
Board/
├── README.md                 # Main documentation
├── ENHANCEMENTS.md          # Full feature documentation
├── pom.xml                  # Maven configuration
├── run-jar.bat              # Windows launcher
├── run-jar.ps1              # PowerShell launcher
│
├── src/main/java/com/nexus/
│   ├── controller/          # UI logic
│   ├── model/               # Drawing shapes & layers
│   ├── view/                # Theme management
│   └── network/             # Networking
│
├── src/main/resources/
│   ├── css/                 # Themes & styling
│   ├── fxml/                # UI layout
│   └── images/              # Icons
│
├── javafx-sdk/              # JavaFX libraries
└── target/                  # Compiled files
```

---

## Support & Contact

For issues or suggestions:
1. Check ENHANCEMENTS.md for detailed documentation
2. Review Troubleshooting section above
3. Verify all dependencies installed
4. Check console output for error messages

---

## Version Info
- Current Version: 3.0
- Release Date: April 2026
- Status: Stable
- Java Target: 17+
- JavaFX: 21+

---

**Happy Drawing! 🎨**
