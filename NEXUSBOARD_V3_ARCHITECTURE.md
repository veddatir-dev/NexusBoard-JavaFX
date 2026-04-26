# NexusBoard v3.0: Technical Architecture & Engineering Depth

## 1. Executive Summary & Core Packages
**NexusBoard** is an AI-enhanced, P2P collaborative modeling platform designed for professional engineers and developers. It supports real-time synchronization, diagramming, code editing, and AI-driven generative art within a robust JavaFX ecosystem.

### Package Architecture
- **`com.nexus.controller`**: UI event handling and MVC orchestration. Maps user inputs from `MainView.fxml` to the underlying model. Handles drag-and-drop actions, window bounding constraints, and grid snapping logic.
- **`com.nexus.model`**: The hierarchical shape engine and GeometryAgent. Contains representations for primitive geometric shapes, freehand strokes, and specialized UML components.
- **`com.nexus.network`**: Multi-threaded TCP synchronization and REST API integration. Facilitates Peer-to-Peer real-time communication across multiple clients without requiring a centralized web server.
- **`com.nexus.view`**: Thematic state management. Dynamically applies CSS stylesheets (`Professional Dark` and `Light Modern`) across the JavaFX Scene Graph.

## 2. Advanced Java & OOP Implementation
### Java 17+ Features
- **Streams API**: Utilized heavily in point normalization routines within the `GeometryAgent`.
- **Lambda Expressions**: Drives fluid event handling for GUI interactions (`setOnAction`, `addListener`).
- **Modern Syntax**: Employs `var` types for concise local variable declarations during collection manipulations.

### OOP Pillars
- **Inheritance**: Implements a deep object hierarchy. The `DiagramShape` abstract class extends the core `Shape` engine, from which robust specialized classes like `UMLClassShape`, `DecisionShape`, and `ActorShape` derive.
- **Polymorphism**: The `iShape` interface ensures the UI `Canvas` can transparently render any object type (e.g., standard geometric shapes, dynamically updated `ConnectorLine`, or AI-generated `TextBox` elements).
- **Abstraction**: The abstract `Shape` class acts as the "Template", abstracting raw bounding box, dimension scaling, rotation, and translation logic away from concrete shape implementations.

## 3. Networking & Communication
### P2P Protocol
Bidirectional TCP Socket communication underpins the networking stack. Custom JSON payload formats over Java Serialization are used for real-time synchronization. `CONNECTION_EVENT` messages accurately broadcast source and target component identifiers to instantly reflect network bindings visually.

### Concurrency
The application relies on a robust **4-thread model**:
1. **Server Thread**: Awaits incoming TCP connections gracefully.
2. **Client Thread**: Handles outbound request pipelines.
3. **Listener Thread**: Blocks iteratively to process incoming data payloads.
4. **Broadcast Thread**: Offloads output transmission from the main application thread.

*Thread-Safety* is strictly maintained via `Platform.runLater()` for all GUI modifications.

## 4. Data Structures & Algorithms
### Collections Framework
- **`ArrayList<iShape>`**: Actively maintains the rendering z-order. Serves as the memory footprint for drawing operations via the `LayerManager`.
- **`HashMap<String, Double>`**: Used for serializing object properties efficiently.

### GeometryAgent Logic
The **Feature Extraction Pipeline** processes dense `Freehand` strokes to deduce shapes:
- **Centroid Analysis**: Determines structural circularity for automated circle detection.
- **Vertex Detection (\Delta\theta)**: Evaluates vector angles between sequential point segments to automatically detect polygons.
- **Grid Snapping**: Normalizes all interactive coordinate drops to the nearest 10px mathematical boundary.

## 5. Features & Technical Analysis
### P2P Diagramming
Intelligent `ConnectorLine` components track UUID-bound `DiagramShape`s (like UML or Flowchart items). During a drag operation, the connector utilizes the Anchor Points (`getAnchorRightX()`, `getAnchorLeftY()`, etc.) to redraw associative logic lines automatically.

### AI Integration
The **DeepAI REST API** integration permits rapid ideation. Generative requests execute asynchronously, rendering generated AI visual interpretations mapped straight to user input.

### Project Persistence
The **`.nbx`** (NexusBoard XML/Binary) format efficiently serializes object bounds, colors, nested layer IDs, and complex UML content. This format guarantees state preservation during complex Save/Load transactions, accommodating professional workflows across distributed devices.
