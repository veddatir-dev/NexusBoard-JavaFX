# Build and Cleanup Guide - Nexus Board v3.0

## Project Cleanup Summary

### Issues Found & Fixed ✓

#### 1. **Unused Imports (FIXED)**
- `Paint.java`: Removed unused `java.net.URL` import

#### 2. **Unwanted Files to Clean Up**
Remove these files/directories from your local workspace (they'll be in .gitignore):
```bash
# Windows Command Prompt
del src\main\java\com\nexus\model\*.class
del javafx-sdk.zip

# Or in PowerShell
Remove-Item src/main/java/com/nexus/model/*.class -Force
Remove-Item javafx-sdk.zip -Force
```

#### 3. **IDE Configuration Files** 
These files are now ignored in `.gitignore`:
- `.classpath`
- `.project`
- `.settings/` directory

If using Visual Studio Code, also ignored:
- `.vscode/`

#### 4. **Redundant Build Plugins (FIXED)**
Consolidated in `pom.xml`:
- ✓ Removed conflicting Assembly plugin
- ✓ Kept single Shade plugin for fat JAR creation
- ✓ Updated JavaFX Maven plugin to optional (dev-only)

#### 5. **Dependencies Optimized**
- ✓ Added `javafx-swing` for export functionality
- ✓ Removed duplicate dependencies
- ✓ Used centralized version property

---

## Building the Fat JAR

### Option 1: Build Fat JAR with Maven (Recommended)

This creates a single executable JAR with all dependencies bundled:

```bash
# Windows Command Prompt
cd c:\Eclipse-workspace\Board
mvn clean package

# Or with PowerShell
cd 'c:\Eclipse-workspace\Board'
mvn clean package
```

**Output:**
- Standard JAR: `target/Board-3.0.0.jar`
- **Fat JAR (executable)**: `target/NexusBoard-3.0.0-executable.jar` ← Use this one

### Option 2: Skip Tests (Faster Build)

```bash
mvn clean package -DskipTests
```

### Option 3: Rebuild Without Clean

```bash
mvn package -DskipTests
```

---

## Running the Fat JAR

### Direct Execution

```bash
# Windows Command Prompt
java -jar target\NexusBoard-3.0.0-executable.jar

# PowerShell
java -jar target/NexusBoard-3.0.0-executable.jar

# Bash/Linux/Mac
java -jar target/NexusBoard-3.0.0-executable.jar
```

### With Custom JVM Memory Settings

```bash
# Allocate 2GB max memory to the application
java -Xmx2G -jar target/NexusBoard-3.0.0-executable.jar

# Allocate 1GB min, 4GB max
java -Xms1G -Xmx4G -jar target/NexusBoard-3.0.0-executable.jar
```

### Batch Script (Windows)

**File: `run-app.bat`**
```batch
@echo off
java -Xmx2G -jar target\NexusBoard-3.0.0-executable.jar
pause
```

### PowerShell Script (Windows)

**File: `run-app.ps1`**
```powershell
$jar = "target/NexusBoard-3.0.0-executable.jar"
if (Test-Path $jar) {
    & java -Xmx2G -jar $jar
} else {
    Write-Host "FAT JAR not found. Run: mvn clean package"
}
```

---

## Shade Plugin Configuration Details

### What the Shade Plugin Does:

1. **Packages all dependencies** into a single JAR file
2. **Includes JavaFX libraries and modules** required for runtime
3. **Sets the main class** in the JAR manifest automatically
4. **Removes unnecessary files** to keep JAR size manageable:
   - Removes duplicate signatures
   - Removes Maven metadata
   - Removes signing certificates

### Fat JAR Features:

✓ **Single file distribution** - No need for separate dependencies
✓ **Cross-platform compatible** - Windows, Mac, Linux
✓ **Includes all JavaFX modules**:
  - javafx-controls
  - javafx-fxml
  - javafx-graphics
  - javafx-swing (for exports)

### Minimization (Optional):

To reduce JAR size by removing unused classes, update `pom.xml`:
```xml
<minimizeJar>true</minimizeJar>
```

**Warning:** This may cause issues if classes are loaded dynamically. Leave `false` for safety.

---

## Build Verification

### Check Files in Build Output

```bash
# PowerShell - List all JARs created
Get-ChildItem target/*.jar

# Windows Command Prompt
dir target\*.jar

# Bash/Linux/Mac
ls -lh target/*.jar
```

### Verify JAR Contents

```bash
# PowerShell - View JAR contents
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::OpenRead('target\NexusBoard-3.0.0-executable.jar')
$zip.Entries | Select-Object -First 20

# Or on any platform with Python
python -m zipfile -l target/NexusBoard-3.0.0-executable.jar | head -20

# Or with jar command (requires Java)
jar tf target/NexusBoard-3.0.0-executable.jar | head -20
```

### Test the Fat JAR

```bash
# Quick test to ensure JAR runs
java -jar target/NexusBoard-3.0.0-executable.jar

# If it starts successfully, close the application (Ctrl+C or close window)
```

---

## Troubleshooting

### Build Fails: "Invalid parameter"

**Cause:** Command not properly formatted
**Solution:** Ensure paths don't have spaces, or quote them:
```bash
cd "c:\Eclipse-workspace\Board"
mvn clean package
```

### JAR Won't Execute: "No such file or directory"

**Cause:** JAR path incorrect
**Solution:** Verify file exists:
```bash
dir target\NexusBoard*.jar
```

### JAR Runs But Application Doesn't Appear

**Cause:** Check for errors in console
**Solution:** Run with verbose output:
```bash
java -verbose:class -jar target/NexusBoard-3.0.0-executable.jar 2>&1 | head -50
```

### Out of Memory Error

**Cause:** Default JVM memory insufficient
**Solution:** Allocate more memory:
```bash
java -Xmx4G -jar target/NexusBoard-3.0.0-executable.jar
```

### Maven Not Found

**Cause:** Maven not installed or not in PATH
**Solution:** Install Maven or use Maven wrapper:
```bash
# Windows
mvnw clean package

# Linux/Mac
./mvnw clean package
```

---

## Distribution & Deployment

### Creating Installation Package

1. **Copy the fat JAR to distribution location:**
   ```bash
   mkdir release
   copy target\NexusBoard-3.0.0-executable.jar release\
   copy run-app.bat release\
   copy README.md release\
   ```

2. **Create batch launcher:**
   ```batch
   @echo off
   "java" -Xmx2G -jar "%~dp0NexusBoard-3.0.0-executable.jar"
   ```

3. **Create shortcut or installer** using tool like NSIS or InnoSetup

### File Size Comparison

- Standard JAR (without deps): ~200 KB
- **Fat JAR (with all deps): ~150-200 MB** ← Distribution package

---

## Maven Build Lifecycle

```
mvn clean                  # Remove target/ directory
mvn compile                # Compile source code only
mvn package                # Compile + Package into JAR
mvn package -DskipTests    # Faster: Skip tests
mvn clean package          # Clean first, then package (recommended)
mvn clean package deploy   # Package + Upload to repository
```

---

## pom.xml Configuration Summary

### Key Properties:
```xml
<javafx.version>21</javafx.version>                    <!-- JavaFX version -->
<maven.compiler.source>17</maven.compiler.source>      <!-- Java source version -->
<maven.compiler.target>17</maven.compiler.target>      <!-- Java target version -->
```

### Shade Plugin Configuration:
- **finalName:** `NexusBoard-${project.version}` → Creates `NexusBoard-3.0.0-executable.jar`
- **mainClass:** `com.nexus.controller.Starter` → Entry point
- **shadedClassifierName:** `executable` → Appends `-executable` to filename
- **minimizeJar:** `false` → Keeps all classes (safer)

---

## Next Steps

1. ✓ **Clean up .class files:**
   ```bash
   cd src\main\java\com\nexus\model
   del *.class
   ```

2. ✓ **Commit clean project:**
   ```bash
   git add -A
   git commit -m "Clean build config: removed unused imports, consolidated plugins, updated pom.xml for fat JAR"
   ```

3. ✓ **Build the fat JAR:**
   ```bash
   cd c:\Eclipse-workspace\Board
   mvn clean package
   ```

4. ✓ **Test the executable:**
   ```bash
   java -jar target\NexusBoard-3.0.0-executable.jar
   ```

5. ✓ **Distribute:** Copy `target/NexusBoard-3.0.0-executable.jar` to end users

---

## Additional Resources

- [Maven Documentation](https://maven.apache.org/)
- [Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/)
- [JavaFX Documentation](https://openjfx.io/)
- [Java Command-Line Tools](https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html)
