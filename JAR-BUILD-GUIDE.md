# Building and Running Nexus Board as JAR

This guide explains how to create and run an executable JAR file for Nexus Board v2.0.

## Prerequisites

### Required
- **Java 17 or higher** - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - Download from [maven.apache.org](https://maven.apache.org/download.cgi)

### Verification
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

Both commands should return version information without errors.

---

## Building the Executable JAR

### Option 1: Using PowerShell Script (Recommended for Windows 10+)

```powershell
# Navigate to project directory
cd C:\Eclipse-workspace\Board

# Run the build script
.\build-executable.ps1
```

### Option 2: Using Batch Script (Windows)

```batch
cd C:\Eclipse-workspace\Board
build-executable.bat
```

### Option 3: Manual Maven Command

```bash
cd C:\Eclipse-workspace\Board
mvn clean compile package
```

---

## Build Output

The build process creates two JAR files in the `target/` directory:

### 1. **NexusBoard-fat.jar** (Recommended)
- **Size**: ~50-60 MB (includes all dependencies)
- **Method**: Maven Shade Plugin
- **Usage**: `java -jar target/NexusBoard-fat.jar`
- **Advantages**: Single file, relocates classes to avoid conflicts

### 2. **NexusBoard-jar-with-dependencies.jar**
- **Size**: ~50-60 MB (includes all dependencies)
- **Method**: Maven Assembly Plugin  
- **Usage**: `java -jar target/NexusBoard-jar-with-dependencies.jar`
- **Advantages**: Simple assembly, all jars included

---

## Running the Application

### Option 1: Using PowerShell Script (Recommended)

```powershell
.\run-jar.ps1
```

### Option 2: Using Batch Script

```batch
run-jar.bat
```

### Option 3: Direct Command

```bash
# Use NexusBoard-fat.jar (recommended)
java -jar target\NexusBoard-fat.jar

# Or use the assembly version
java -jar target\NexusBoard-jar-with-dependencies.jar
```

### Option 4: With Custom JVM Options

```bash
# Allocate more memory (e.g., 2GB)
java -Xmx2G -jar target\NexusBoard-fat.jar

# Enable detailed logging
java -Djavafx.debug -jar target\NexusBoard-fat.jar

# Combined options
java -Xmx2G -Djavafx.debug -jar target\NexusBoard-fat.jar
```

---

## Troubleshooting

### Error: "Maven is not installed or not in PATH"

**Solution**: 
1. Download Maven from [maven.apache.org](https://maven.apache.org/)
2. Extract to a directory (e.g., `C:\Maven`)
3. Add to system PATH:
   - Right-click "This PC" → Properties
   - Advanced system settings → Environment Variables
   - New system variable: `MAVEN_HOME = C:\Maven`
   - Edit PATH and add `%MAVEN_HOME%\bin`
4. Restart terminal and verify: `mvn -version`

### Error: "Java is not installed or outdated"

**Solution**:
1. Download Java 17+ from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
2. Install to default location
3. Add to system PATH if needed
4. Restart terminal and verify: `java -version`

### Error: "JAR file not found"

**Solution**:
1. Ensure build completed successfully (check for error messages)
2. Verify `target/` directory exists
3. Run build again with verbose output: `mvn package -X`

### Application won't start

**Solution**:
1. Verify Java path: `java -jar target\NexusBoard-fat.jar`
2. Check Java version supports JavaFX 21
3. Try with increased memory: `java -Xmx4G -jar target\NexusBoard-fat.jar`
4. Check for port conflicts (port 5000 must be available)

---

## JAR File Distribution

Once built, you can distribute the JAR file:

### Sharing the JAR
- Copy `target/NexusBoard-fat.jar` to any location
- Users only need Java 17+ installed
- No Maven or IDE required on target machine

### Creating Windows Shortcut
1. Create a `.bat` file:
```batch
@echo off
java -jar "C:\path\to\NexusBoard-fat.jar"
pause
```
2. Double-click to run

### Creating macOS/Linux Run Script
1. Create file named `run-nexus`:
```bash
#!/bin/bash
java -jar "$(dirname "$0")/NexusBoard-fat.jar"
```
2. Make executable: `chmod +x run-nexus`
3. Run: `./run-nexus`

---

## Performance Tips

### Memory Allocation
For better performance with large drawings:
```bash
java -Xmx4G -jar target\NexusBoard-fat.jar
```

### Faster Startup
Pre-compile with tiered compilation:
```bash
java -XX:+TieredCompilation -XX:TieredStopAtLevel=4 -jar target\NexusBoard-fat.jar
```

### Disable Unnecessary Logging
```bash
java -Dcom.sun.javafx.debug=false -jar target\NexusBoard-fat.jar
```

---

## File Structure

```
Board/
├── pom.xml                    (Maven configuration)
├── build-executable.bat       (Windows build script)
├── build-executable.ps1       (PowerShell build script)
├── run-jar.bat               (Windows run script)
├── run-jar.ps1               (PowerShell run script)
├── JAR-BUILD-GUIDE.md        (This file)
├── src/                       (Source code)
└── target/                    (Build artifacts)
    ├── NexusBoard-fat.jar    (Recommended JAR)
    ├── NexusBoard-jar-with-dependencies.jar
    └── classes/              (Compiled classes)
```

---

## Build Process Details

The `pom.xml` is configured with multiple plugins:

### 1. Compiler Plugin
- Compiles Java source files
- Target: Java 17

### 2. JAR Plugin
- Creates basic JAR with manifest
- Main-Class: `com.nexus.controller.Starter`

### 3. Assembly Plugin
- Creates JAR with all dependencies
- Output: `NexusBoard-jar-with-dependencies.jar`

### 4. Shade Plugin
- Creates fat JAR with shaded dependencies
- Avoids JAR conflicts
- Output: `NexusBoard-fat.jar`

### 5. JavaFX Maven Plugin
- Supports running with `mvn javafx:run`
- Development convenience

---

## Integrated Collaboration (P2P)

When running from JAR, P2P features work identically:

1. **Host a Session**
   - Run first instance: `java -jar target\NexusBoard-fat.jar`
   - Click "Host Board"
   - Get your IP address

2. **Connect to Peer**
   - Run second instance: `java -jar target\NexusBoard-fat.jar`
   - Enter host IP in IP field
   - Click "Connect"
   - Real-time synchronization starts

---

## System Requirements

### Minimum
- Java 17+
- 200 MB free disk space
- 512 MB RAM
- 1 Mbps network (for P2P)

### Recommended
- Java 21+
- 500 MB free disk space
- 2 GB RAM
- 100 Mbps network

---

## Firewall Configuration

For P2P collaboration, allow port 5000:

### Windows Firewall
```powershell
New-NetFirewallRule -DisplayName "Nexus Board" -Direction Inbound `
  -LocalPort 5000 -Protocol TCP -Action Allow
```

### Linux (UFW)
```bash
sudo ufw allow 5000/tcp
```

### macOS
- System Preferences → Security & Privacy → Firewall Options
- Add `java` to allowed applications

---

## Support

For issues or questions:
1. Check this guide's troubleshooting section
2. Verify Java and Maven versions
3. Review build log for detailed errors
4. Ensure port 5000 is available for networking features
