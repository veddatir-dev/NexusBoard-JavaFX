# Nexus Board v3.0 - Professional AI-Enhanced Collaborative Drawing Platform

A sophisticated collaborative drawing application built with Java and JavaFX, featuring AI-powered image generation, intelligent shape recognition, real-time peer-to-peer synchronization, professional theming, advanced layer management, and multi-format export capabilities. Demonstrates advanced OOP principles, mathematical algorithms, and concurrent programming patterns.

## Table of Contents
1. [Project Overview](#project-overview)
2. [What's New in v3.0](#whats-new-in-v30)
3. [Technology Stack](#technology-stack)
4. [Java Features & OOP Design](#java-features--oop-design)
5. [Mathematical Implementation](#mathematical-implementation)
6. [Features](#features)
7. [Architecture](#architecture)
8. [Getting Started](#getting-started)

---

## Project Overview

Nexus Board is a professional-grade collaborative drawing application that allows multiple users to draw and collaborate in real-time over a peer-to-peer network. The application features intelligent shape recognition that converts freehand drawings into perfect geometric shapes using advanced mathematical algorithms, AI integration for generating artwork, comprehensive layer management system, four professional themes, and multi-format export capabilities.

### Key Objectives
- **Real-time Collaboration**: Multiple users draw simultaneously across network
- **Intelligent Recognition**: Convert rough sketches to perfect shapes using geometry algorithms
- **Professional UI**: Modern, responsive interface with 4 professional themes
- **Advanced Editing**: Layer system, shape transformation, text & eraser tools
- **Multi-Format Export**: Save as PNG, JPEG, BMP, or SVG
- **Project Management**: Save/load complete projects with all layer information
- **AI Enhancement**: Generate artistic images from shapes using AI
- **Robust Networking**: Reliable P2P communication with error handling

---

## What's New in v3.0

### UI Enhancements
✨ **Professional Themes** - 4 carefully designed themes
- Professional Dark (cyan accents)
- Light Modern (clean blue)
- Dark Ultimate (orange accents)
- Modern Gradient (pink/purple)

🎨 **Enhanced Toolbar** - Reorganized into 7 logical sections
📚 **Layer System** - Full layer management with visibility and lock controls
⚙️ **Transformation Controls** - Rotation and scaling sliders
📊 **Expanded Sidebar** - Layers, history, and shape options

### New Drawing Tools
- **📝 Text Tool** - Add text at any position with font size control
- **🧹 Eraser Tool** - Click and drag to erase areas
- **✓ Selection Tool** - Select and transform individual shapes

### Advanced Features
- **Layer Management** - Unlimited layers with naming, visibility, locking
- **Shape Transformation** - Rotation (0-360°) and scaling (0.1x-2.0x)
- **Save/Load Projects** - Complete project serialization (.nbx format)
- **Multi-Format Export** - PNG, JPEG, BMP, or SVG export

### Documentation
📖 See **ENHANCEMENTS.md** for complete feature documentation  
⚡ See **QUICK_START.md** for getting started guide

---

## Technology Stack

### Core Framework
- **Java 17+**: Modern Java features, strong typing, null safety
- **JavaFX 21**: Rich UI components, graphics rendering, scene graph
- **Maven**: Project build management and dependency resolution

### Libraries & APIs
- **DeepAI REST API**: AI image generation service
- **Java 2D Graphics**: Canvas rendering and vector graphics
- **Java Networking**: TCP sockets for P2P communication
- **Java Streams API**: Functional programming and data processing
- **Java ThreadPool**: Concurrent network operations
- **Java Serialization**: Object persistence for projects

### Development Environment
- **Eclipse IDE**: Primary development environment
- **CSS Styling**: Modern UI themes and responsive design
- **FXML**: XML-based UI description language
- **Git**: Version control

---

## Java Features & OOP Design

### 1. **Package Structure** (`com.nexus.*`)
```
com.nexus.controller/    - UI controllers and event handling
com.nexus.model/         - Core drawing model and shapes
com.nexus.network/       - Networking and communication
```

### 2. **Interface Design** - `iShape.java`
```java
public interface iShape {
    void draw(Canvas canvas);
    void setColor(Color color);
    Map<String, Double> getProperties();
    void setProperties(Map<String, Double> properties);
}
```
- **Polymorphism**: All shapes implement common interface
- **Abstraction**: Hides implementation details
- **Serialization Contract**: Enables network transmission

### 3. **Abstract Classes** - `Shape.java`
```java
public abstract class Shape implements iShape {
    protected double x1, y1, x2, y2;
    protected Color color;
    
    public void setPoints(double x1, double y1, double x2, double y2)
    public void setColor(Color color)
}
```
- **Template Method Pattern**: Abstract implementation for all geometric shapes
- **Protected Fields**: Accessible to subclasses, hidden from external code
- **Code Reuse**: Common functionality for all shape types

### 4. **Inheritance Hierarchy**
```
iShape (interface)
  └── Shape (abstract class)
      ├── Line (concrete)
      │   ├── DottedLine
      │   ├── DoubleLine
      │   └── Arrow
      ├── Rectangle (concrete)
      │   └── Square
      ├── Circle (concrete)
      ├── Ellipse (concrete)
      ├── Triangle (concrete)
      └── Freehand (concrete)
```

### 5. **Concrete Shape Classes**
Each shape extends `Shape` or `Line`:
- **`Line.java`**: Basic line with x1, y1, x2, y2 coordinates
- **`DottedLine.java`**: Extends Line with dashed pattern
- **`DoubleLine.java`**: Extends Line with dual parallel lines
- **`Arrow.java`**: Extends Line with arrowhead geometry
- **`Rectangle.java`**: Rectangular shape with width/height
- **`Square.java`**: Extends Rectangle with equal sides
- **`Circle.java`**: Perfect circle rendering
- **`Ellipse.java`**: Oval shape with aspect ratio
- **`Triangle.java`**: Tri-point shape with path drawing
- **`Freehand.java`**: User-defined path collection

### 6. **Constructors** 
All shapes use implicit default constructors from `Shape`:
```java
public Circle() {
    // Inherits from Shape
    // Fields initialized to default: x1=0, y1=0, x2=0, y2=0, color=null
}
```

### 7. **Threading & Concurrency**
```java
// Server Thread - Accept incoming connections
new Thread(() -> {
    ServerSocket serverSocket = new ServerSocket(PORT);
    socket = serverSocket.accept();  // Blocking wait
    setupStreams(controller);
}).start();

// Client Thread - Connect to peer
new Thread(() -> {
    socket = new Socket(ip, PORT);
    setupStreams(controller);
}).start();

// Listener Thread - Receive messages
new Thread(() -> {
    while (true) {
        String type = (String) in.readObject();
        Platform.runLater(() -> { /* UI Update */ });
    }
}).start();

// Broadcast Thread - Send shapes
new Thread(() -> {
    out.writeObject(shape.getClass().getSimpleName());
    out.writeObject(shape.getProperties());
    out.flush();
}).start();
```
- **Multi-threading**: Non-blocking network I/O
- **FX Thread Safety**: `Platform.runLater()` for UI updates
- **Thread Pool Pattern**: Each operation gets dedicated thread
- **Synchronization**: Uses synchronized access via streams

### 8. **Collections Usage**
- **`List<iShape> allShapes`**: ArrayList for shape storage
- **`Map<String, Double> properties`**: HashMap for shape serialization
- **`ObservableList<String> shapeList`**: JavaFX binding for UI

### 9. **Exception Handling**
```java
try {
    socket = new Socket(ip, PORT);
    setupStreams(controller);
} catch (IOException e) {
    e.printStackTrace();  // Connection failed
    Platform.runLater(() -> controller.updateConnectionStatus("Failed"));
}
```

### 10. **Factory Pattern** - `ShapeFactory.java`
```java
public static iShape getShape(String shapeType) {
    switch (shapeType.toLowerCase().replace(" ", "")) {
        case "line": return new Line();
        case "dottedline": return new DottedLine();
        case "arrow": return new Arrow();
        case "circle": return new Circle();
        case "square": return new Square();
        // ... more shapes
        default: return null;
    }
}
```
- **Creational Pattern**: Centralized shape instantiation
- **Loose Coupling**: UI doesn't know concrete shape classes
- **Extensibility**: Add new shapes without modifying controller

---

## Mathematical Implementation

### 1. **Shape Factory Logic**
```java
// Creates appropriate shape based on type string
// Maps UI selections to concrete class instances
// Handles case-insensitive input with space removal
```

### 2. **GeometryAgent - Shape Recognition Algorithm**

#### **2.1 Point Preprocessing**
```java
// Remove duplicate points (distance < 2 pixels)
for (Point2D p : points) {
    if (cleaned.isEmpty() || cleaned.get(cleaned.size()-1).distance(p) > 2) {
        cleaned.add(p);
    }
}
```
- Clean noise from user input
- Reduce computational overhead
- Improve accuracy

#### **2.2 Bounding Box Calculation**
```java
double minX = points.stream().mapToDouble(p -> p.getX()).min().orElse(0);
double maxX = points.stream().mapToDouble(p -> p.getX()).max().orElse(0);
double minY = points.stream().mapToDouble(p -> p.getY()).min().orElse(0);
double maxY = points.stream().mapToDouble(p -> p.getY()).max().orElse(0);

double width = maxX - minX;
double height = maxY - minY;
double aspectRatio = width / height;
```
- **Aspect Ratio**: width/height - used to distinguish squares from rectangles, circles from ellipses
- **Minimum Size**: Reject shapes < 10x10 pixels

#### **2.3 Closure Distance (Line Detection)**
```java
Point2D start = points.get(0);
Point2D end = points.get(points.size()-1);
double closureDist = start.distance(end);

// Open shape (closureDist > threshold) = LINE
// Closed shape (closureDist < threshold) = POLYGON
```
- **Euclidean Distance**: $d = \sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$
- Determines if shape is open (line) or closed (polygon)

#### **2.4 Line Score Calculation**
```java
double calculateLineScore(List<Point2D> points, Point2D a, Point2D b) {
    double maxDev = 0;
    for (Point2D p : points) {
        double dev = distanceToLine(p, a, b) / lineLength;
        maxDev = Math.max(maxDev, dev);
    }
    return maxDev;  // Score < 0.05 = good line
}
```
- **Point-to-Line Distance**: Perpendicular distance
- **Normalization**: Divide by line length to make scale-independent
- **Perfect Line**: Score = 0, Noisy line: Score > 0.05

#### **2.5 Circle Fitting Algorithm**
```java
CircleFit fitCircle(List<Point2D> points) {
    // Calculate geometric center
    double cx = 0, cy = 0;
    for (Point2D p : points) {
        cx += p.getX();
        cy += p.getY();
    }
    cx /= points.size();
    cy /= points.size();
    
    // Calculate average radius
    double radius = 0;
    for (Point2D p : points) {
        radius += Math.sqrt((p.getX()-cx)² + (p.getY()-cy)²);
    }
    radius /= points.size();
    
    // Calculate fitting error
    double error = 0;
    for (Point2D p : points) {
        double dist = Math.sqrt((p.getX()-cx)² + (p.getY()-cy)²);
        error += Math.abs(dist - radius);
    }
    error /= (points.size() * radius);  // Normalized
    
    return new CircleFit(cx, cy, radius, error);
}
```
- **Least Squares Approximation**: Find best-fit circle
- **Error Metric**: Average deviation from ideal radius
- **Normalized Error**: Divide by radius for scale independence
- **Threshold**: error < 0.15 = valid circle/ellipse

#### **2.6 Rectangle/Square Detection**
```java
double calculateRectangleScore(List<Point2D> points, 
                               double minX, double maxX, 
                               double minY, double maxY) {
    int cornerPoints = 0;
    double margin = 0.15;  // 15% from edges
    
    for (Point2D p : points) {
        boolean inCorner = 
            (p.getX() < minX + width*margin && 
             p.getY() < minY + height*margin) ||
            // ... other corners
        if (inCorner) cornerPoints++;
    }
    return cornerPoints / 4.0;  // Score 0-1
}
```
- **Corner Detection**: Rectangles have points in 4 corners
- **Expected Corners**: Typically 4 (one per corner)
- **Score Calculation**: cornerCount / 4.0
- **Threshold**: score > 0.8 = valid rectangle

#### **2.7 Triangle Detection**
```java
double calculateTriangleScore(List<Point2D> points) {
    List<Double> angles = new ArrayList<>();
    
    // Calculate direction changes
    for (int i = 1; i < points.size()-1; i++) {
        Point2D prev = points.get(i-1);
        Point2D curr = points.get(i);
        Point2D next = points.get(i+1);
        
        double angle1 = Math.atan2(curr.getY()-prev.getY(), 
                                   curr.getX()-prev.getX());
        double angle2 = Math.atan2(next.getY()-curr.getY(), 
                                   next.getX()-curr.getX());
        double diff = Math.abs(angle2 - angle1);
        if (diff > Math.PI) diff = 2*Math.PI - diff;
        angles.add(diff);
    }
    
    // Count significant changes (> 30°)
    int changes = 0;
    for (double a : angles) {
        if (a > Math.PI/6) changes++;  // π/6 ≈ 30°
    }
    return changes > 2 ? 0.8 : 0.0;
}
```
- **Angle Calculation**: $\theta = \arctan2(dy, dx)$
- **Direction Change**: Acute angle indicates corner
- **Triangle Signature**: 3 significant direction changes
- **Expected Changes**: ~3 for triangle, ~4 for rectangle

#### **2.8 Circle vs Ellipse Discrimination**
```java
double aspectRatio = width / height;
if (circleFit.error < 0.15) {
    if (Math.abs(aspectRatio - 1.0) < 0.2) {
        return "Circle";      // Ratio near 1.0
    } else {
        return "Ellipse";     // Ratio far from 1.0
    }
}
```
- **Circle**: aspectRatio ≈ 1.0 (difference < 0.2)
- **Ellipse**: aspectRatio ≠ 1.0 (difference > 0.2)

#### **2.9 Square vs Rectangle Discrimination**
```java
if (rectScore > 0.8) {
    double aspectRatio = width / height;
    if (Math.abs(aspectRatio - 1.0) < 0.15) {
        return "Square";      // Ratio very close to 1.0
    } else {
        return "Rectangle";   // Ratio differs significantly
    }
}
```
- **Square**: aspectRatio ≈ 1.0 (difference < 0.15)
- **Rectangle**: aspectRatio ≠ 1.0 (difference > 0.15)

### 3. **Shape Drawing Mathematics**

#### **Circle Drawing**
```java
double radius = Math.min(Width, Height) / 2;
gc.strokeOval(Upperx + (Width - radius*2)/2, 
              Uppery + (Height - radius*2)/2, 
              radius*2, radius*2);
```
- **Center Calculation**: $(width - diameter) / 2$ for centering
- **Diameter**: $2 \times radius$

#### **Arrow Head Geometry**
```java
double angle = Math.atan2(y2-y1, x2-x1);  // Line angle
double arrowLength = 10;
double arrowAngle = Math.PI/6;             // 30°

double x3 = x2 - arrowLength * Math.cos(angle - arrowAngle);
double y3 = y2 - arrowLength * Math.sin(angle - arrowAngle);
double x4 = x2 - arrowLength * Math.cos(angle + arrowAngle);
double y4 = y2 - arrowLength * Math.sin(angle + arrowAngle);

gc.strokeLine(x2, y2, x3, y3);
gc.strokeLine(x2, y2, x4, y4);
```
- **Line Angle**: $\theta = \arctan2(dy, dx)$
- **Arrowhead Points**: Rotate by ±30° from line angle
- **Point Calculation**: $(x,y) = (x_0 - r\cos\theta, y_0 - r\sin\theta)$

#### **Double Line Rendering**
```java
double dx = x2 - x1, dy = y2 - y1;
double length = Math.sqrt(dx*dx + dy*dy);
double ux = dx / length, uy = dy / length;
double px = -uy * offset, py = ux * offset;

gc.strokeLine(x1+px, y1+py, x2+px, y2+py);
gc.strokeLine(x1-px, y1-py, x2-px, y2-py);
```
- **Unit Vector**: $(ux, uy) = (dx/length, dy/length)$
- **Perpendicular Vector**: $(px, py) = (-uy, ux)$
- **Parallel Lines**: Offset by perpendicular distance

#### **Triangle Path**
```java
double topX = (x1 + x2) / 2;
double topY = y1;
double bottomLeftX = x1, bottomLeftY = y2;
double bottomRightX = x2, bottomRightY = y2;

gc.beginPath();
gc.moveTo(topX, topY);
gc.lineTo(bottomLeftX, bottomLeftY);
gc.lineTo(bottomRightX, bottomRightY);
gc.lineTo(topX, topY);
gc.stroke();
```
- **Top Point**: Midpoint of x-coordinates at top
- **Base Points**: Bottom-left and bottom-right of bounding box
- **Path Drawing**: Closed polygon

---

## Features

### 🎨 **Advanced Drawing Tools**
- **10 Shape Types**: 
  - Lines: Line, Dotted Line, Double Line, Arrow
  - Rectangles: Rectangle, Square
  - Ovals: Circle, Ellipse
  - Other: Triangle, Freehand
- **Smart Shape Recognition**: Automatically converts rough sketches to perfect shapes
- **Color Picker**: Full RGB color palette support
- **Real-time Rendering**: Smooth, responsive canvas with 60fps target

### 🤖 **AI Integration**
- **DeepAI Integration**: Generate artistic images from shape descriptions
- **Shape-to-Image**: Convert geometric concepts to visual art
- **Browser Integration**: Opens generated images in default browser

### 🌐 **Collaboration Features**
- **Peer-to-Peer Network**: Direct TCP socket communication
- **Synchronized Drawing**: All shapes sync across connected peers in real-time
- **Synchronized Commands**: Clear All and Undo operations sync across network
- **Connection Management**: Host mode, connect mode, status feedback
- **Error Resilience**: Automatic disconnection handling

### 🎯 **Modern UI/UX**
- **Professional Design**: Clean, modern interface with dark theme
- **Organized Toolbar**: Logically grouped tools (Drawing, Edit, Collaboration, AI)
- **Responsive Layout**: Adaptive design with resizable panels
- **Visual Feedback**: Hover animations, tooltips, status bar updates
- **Shape History**: Lists all drawn shapes with visual organization

---

## Architecture

### **Model-View-Controller (MVC) Pattern**

#### **Model (`com.nexus.model`)**
- `iShape`: Interface defining shape contract
- `Shape`: Abstract base class for geometric shapes
- `[Concrete Shapes]`: Line, Circle, Rectangle, Triangle, etc.
- `ShapeFactory`: Factory for creating shapes
- `GeometryAgent`: Shape recognition engine
- `RecognitionResult`: Data class for recognition results

#### **View (`src/main/resources`)**
- `MainView.fxml`: XML UI layout
- `style.css`: Professional styling and theming

#### **Controller (`com.nexus.controller`)**
- `FXMLDocumentController`: Main UI event handler
- `Paint`: JavaFX Application entry point
- `Starter`: JVM entry point

#### **Network (`com.nexus.network`)**
- `NetworkManager`: Singleton managing P2P communication
- `ImageGenService`: REST client for DeepAI API

### **Communication Flow**
```
User Input (Mouse Events)
    ↓
Controller (FXMLDocumentController)
    ↓
Model (Shape Creation)
    ↓
Network (Broadcasting)
    ↓
Remote Peer (NetworkManager Listen Thread)
    ↓
Remote Controller (Update Canvas)
```

### **Thread Model**
```
Main Thread (UI/FX Thread)
    ↓ Event Dispatch
   GUI Component Handlers
    ↓
├─ Server Accept Thread (NetworkManager)
├─ Client Connect Thread (NetworkManager)
├─ Network Listen Thread (reading incoming data)
├─ Broadcast Thread (sending shapes)
└─ AI Generation Thread (REST API calls)

All updates back to UI via Platform.runLater()
```

---

## Getting Started

### **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- Internet connection (for AI features)

### **Installation**
```bash
git clone <repository>
cd Board
mvn clean install
```

### **Build**
```bash
mvn clean compile
```

### **Run (Development)**
```bash
mvn javafx:run
```

### **Build Executable JAR**
To create a standalone executable JAR file for distribution:

#### Quick Build (Windows)
```batch
build-executable.bat
```

#### Quick Build (PowerShell)
```powershell
.\build-executable.ps1
```

#### Manual Build
```bash
mvn clean compile package
```

This creates two JAR files in `target/`:
- `NexusBoard-fat.jar` (Recommended - single file)
- `NexusBoard-jar-with-dependencies.jar`

### **Run from JAR (Standalone)**
```bash
# Using the fat JAR
java -jar target/NexusBoard-fat.jar

# Or using assembly version
java -jar target/NexusBoard-jar-with-dependencies.jar
```

For detailed JAR building instructions, see **[JAR-BUILD-GUIDE.md](JAR-BUILD-GUIDE.md)**

### **Running Multiple Instances (Collaboration)**
1. Terminal 1: `mvn javafx:run` or `java -jar target/NexusBoard-fat.jar` → Click "Host Board"
2. Terminal 2: `mvn javafx:run` or `java -jar target/NexusBoard-fat.jar` → Enter IP address and click "Connect"
3. Draw in either window - shapes sync in real-time

### **Project Structure**
```
Board/
├── pom.xml                          (Maven configuration)
├── README.md                        (Project documentation)
├── JAR-BUILD-GUIDE.md              (JAR building guide)
├── build-executable.bat             (Windows build script)
├── build-executable.ps1             (PowerShell build script)
├── run-jar.bat                      (Windows run script)
├── run-jar.ps1                      (PowerShell run script)
├── src/
│   ├── main/
│   │   ├── java/com/nexus/
│   │   │   ├── controller/          (MVC Controllers)
│   │   │   │   ├── Paint.java       (JavaFX Application)
│   │   │   │   ├── Starter.java     (Main entry)
│   │   │   │   └── FXMLDocumentController.java
│   │   │   ├── model/               (Core shapes & logic)
│   │   │   │   ├── iShape.java      (Interface)
│   │   │   │   ├── Shape.java       (Abstract base)
│   │   │   │   ├── [Shapes].java    (Concrete implementations)
│   │   │   │   ├── ShapeFactory.java
│   │   │   │   ├── GeometryAgent.java
│   │   │   │   └── RecognitionResult.java
│   │   │   └── network/
│   │   │       ├── NetworkManager.java
│   │   │       └── ImageGenService.java
│   │   └── resources/
│   │       ├── fxml/MainView.fxml
│   │       └── css/style.css
│   └── test/                        (Unit tests)
└── target/                          (Build artifacts)
    ├── NexusBoard-fat.jar          (Executable - Recommended)
    ├── NexusBoard-jar-with-dependencies.jar
    └── classes/                     (Compiled classes)
```

### **Configuration**
- **Port**: 5000 (for P2P networking)
- **AI Service**: DeepAI (https://deepai.org)
- **Update API_KEY in**: ImageGenService.java

---

## Key Learning Outcomes

This project demonstrates:
- ✅ **Object-Oriented Design**: Interfaces, abstract classes, inheritance
- ✅ **Design Patterns**: Factory, Observer, Singleton, MVC
- ✅ **Concurrency**: Multi-threading, thread pools, synchronized operations
- ✅ **Collections**: Lists, Maps, Streams API
- ✅ **Mathematical Algorithms**: Geometry, shape fitting, recognition
- ✅ **Network Programming**: TCP sockets, serialization, P2P communication
- ✅ **UI Development**: JavaFX, CSS, FXML, event handling
- ✅ **API Integration**: REST client, JSON parsing
- ✅ **Software Architecture**: MVC pattern, separation of concerns
- ✅ **Professional Development**: Error handling, logging, code organization