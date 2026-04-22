# Quick Terminal Commands - Build & Cleanup

## 🧹 STEP 1: Cleanup (.class files from source)

### PowerShell
```powershell
# Remove .class files from source directory
Remove-Item src/main/java/com/nexus/model/*.class -Force

# Optional: Remove the JavaFX SDK zip if you have space concerns
Remove-Item javafx-sdk.zip -Force

# Verify cleanup
Get-ChildItem src/main/java/com/nexus/model -Filter *.class
```

### Windows Command Prompt
```batch
REM Navigate to project
cd c:\Eclipse-workspace\Board

REM Remove .class files
del src\main\java\com\nexus\model\*.class /Q

REM Optional: Remove JavaFX SDK zip
del javafx-sdk.zip /Q

REM Verify
dir src\main\java\com\nexus\model\*.class
```

### Bash/Linux/Mac
```bash
cd ~/path/to/Board

# Remove .class files
find src/main/java/com/nexus/model -name "*.class" -delete

# Optional: Remove JavaFX SDK zip
rm -f javafx-sdk.zip

# Verify
ls -la src/main/java/com/nexus/model/*.class 2>/dev/null || echo "✓ All .class files cleaned"
```

---

## 📦 STEP 2: Build Fat JAR

### Standard Build (with tests)
```bash
cd c:\Eclipse-workspace\Board
mvn clean package
```

### Fast Build (skip tests)
```bash
cd c:\Eclipse-workspace\Board
mvn clean package -DskipTests
```

### Multi-platform (Bash/PowerShell)
```bash
cd c:\Eclipse-workspace\Board
mvn clean package -DskipTests
```

### Build with Maven Wrapper (if available)
```bash
# Windows
mvnw clean package -DskipTests

# Linux/Mac
./mvnw clean package -DskipTests
```

---

## ✅ STEP 3: Verify Build Completed

### PowerShell
```powershell
# List all JARs created
Get-ChildItem target/*.jar -ErrorAction SilentlyContinue

# Show only executable JAR
Get-ChildItem target/*-executable.jar

# Show file size
Get-ChildItem target/*-executable.jar | Select-Object Name, @{Label="Size (MB)";Expression={"{0:F2}" -f ($_.Length/1MB)}}
```

### Windows Command Prompt
```batch
REM List all JARs
dir target\*.jar

REM Show executable JAR Only
dir target\*-executable.jar
```

### Bash/Linux/Mac
```bash
# List all JARs with size
ls -lh target/*.jar

# Show only executable JAR
ls -lh target/*-executable.jar

# Display size in human-readable format
du -h target/*-executable.jar
```

---

## 🚀 STEP 4: Run the Fat JAR

### Basic Execution (All Platforms)
```bash
java -jar target/NexusBoard-3.0.0-executable.jar
```

### PowerShell
```powershell
java -jar target/NexusBoard-3.0.0-executable.jar
```

### Windows Command Prompt
```batch
java -jar target\NexusBoard-3.0.0-executable.jar
```

### With Console Output (Linux/Mac/Git Bash)
```bash
java -jar target/NexusBoard-3.0.0-executable.jar &
```

---

## 💾 Advanced: Run with Memory Configuration

### Default (Auto-detect)
```bash
java -jar target/NexusBoard-3.0.0-executable.jar
```

### 2GB Max Memory
```bash
java -Xmx2G -jar target/NexusBoard-3.0.0-executable.jar
```

### 1GB Min, 4GB Max
```bash
java -Xms1G -Xmx4G -jar target/NexusBoard-3.0.0-executable.jar
```

### High Performance (8GB)
```bash
java -Xms2G -Xmx8G -XX:+UseG1GC -jar target/NexusBoard-3.0.0-executable.jar
```

### Verbose Output (for debugging)
```bash
java -verbose:class -jar target/NexusBoard-3.0.0-executable.jar 2>&1 | head -50
```

---

## 📋 STEP 5: Inspect Fat JAR Contents

### PowerShell - Quick Peek
```powershell
# Using Python
python -m zipfile -l target/NexusBoard-3.0.0-executable.jar | Select-Object -First 30

# Or using jar tool
jar tf target/NexusBoard-3.0.0-executable.jar | Select-Object -First 30
```

### Windows Command Prompt - using jar tool
```batch
jar tf target\NexusBoard-3.0.0-executable.jar | more
```

### Bash - List and grep
```bash
# List all files
jar tf target/NexusBoard-3.0.0-executable.jar | head -30

# Count total files
jar tf target/NexusBoard-3.0.0-executable.jar | wc -l

# Find specific library
jar tf target/NexusBoard-3.0.0-executable.jar | grep javafx

# Check manifest
jar xf target/NexusBoard-3.0.0-executable.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
rm -rf META-INF
```

---

## 🔍 STEP 6: Test Build Quality

### Verify Main Class in Manifest
```bash
# PowerShell
jar tf target/NexusBoard-3.0.0-executable.jar META-INF/MANIFEST.MF

# Windows Command Prompt
jar xf target\NexusBoard-3.0.0-executable.jar META-INF\MANIFEST.MF && type META-INF\MANIFEST.MF

# Linux/Mac
jar xf target/NexusBoard-3.0.0-executable.jar META-INF/MANIFEST.MF && cat META-INF/MANIFEST.MF
```

### Quick Runtime Test
```bash
# Just start and close (Ctrl+C or close window)
java -jar target/NexusBoard-3.0.0-executable.jar
```

### Full Test Suite
```bash
# Run with tests included
mvn clean package

# Or just run tests
mvn test
```

---

## 📤 STEP 7: Prepare for Distribution

### PowerShell - Copy to Release Directory
```powershell
# Create release folder
New-Item -ItemType Directory -Path "release" -Force

# Copy JAR
Copy-Item target/NexusBoard-3.0.0-executable.jar release/

# Create launch script
@"
@echo off
java -Xmx2G -jar "%~dp0NexusBoard-3.0.0-executable.jar"
pause
"@ | Set-Content release/run.bat

# Create README
Copy-Item README.md release/

# List release contents
Get-ChildItem release/
```

### Windows Command Prompt - Copy to Release Directory
```batch
REM Create release folder
mkdir release

REM Copy JAR
copy target\NexusBoard-3.0.0-executable.jar release\

REM Copy documentation
copy README.md release\
copy BUILD_AND_CLEANUP_GUIDE.md release\

REM List contents
dir release\
```

### Bash - Copy to Release Directory
```bash
# Create release folder
mkdir -p release

# Copy JAR
cp target/NexusBoard-3.0.0-executable.jar release/

# Create launch script
cat > release/run.sh << 'EOF'
#!/bin/bash
java -Xmx2G -jar "$(dirname "$0")/NexusBoard-3.0.0-executable.jar"
EOF

chmod +x release/run.sh

# Copy documentation
cp README.md release/
cp BUILD_AND_CLEANUP_GUIDE.md release/

# List contents
ls -lh release/
```

---

## 🔄 Clean Build Cycle

### Complete Clean & Rebuild
```bash
cd c:\Eclipse-workspace\Board
mvn clean
mvn dependency:resolve
mvn package -DskipTests
```

### One-liner (All Platforms)
```bash
cd c:\Eclipse-workspace\Board && mvn clean package -DskipTests
```

### If Maven has Issues
```bash
# Clear Maven cache
rm -rf ~/.m2/repository/org/openjfx  # Linux/Mac
rmdir %USERPROFILE%\.m2\repository\org\openjfx /s  # Windows

# Then rebuild
mvn clean package -DskipTests
```

---

## 🛠️ Troubleshooting Commands

### Check Maven Version
```bash
mvn --version
```

### Check Java Version
```bash
java -version
```

### Check if Java is in PATH
```bash
# PowerShell
where java

# Command Prompt
where java

# Bash
which java
```

### Download All Dependencies (without building)
```bash
mvn dependency:resolve
mvn dependency:resolve-plugins
```

### Show Dependency Tree
```bash
mvn dependency:tree
```

### Compile Only (no packaging)
```bash
mvn compile
```

### Skip Failing Tests but Continue Build
```bash
mvn package -Dtest=*SkipTests
```

### Run Specific Test
```bash
mvn test -Dtest=MyTestClass
```

### Check Plugin Versions
```bash
mvn help:describe -Dplugin=org.apache.maven.plugins:maven-shade-plugin
```

---

## 📊 One-Command Complete Workflow

### PowerShell - Full workflow
```powershell
# 1. Navigate
cd c:\Eclipse-workspace\Board

# 2. Clean .class files
Remove-Item src/main/java/com/nexus/model/*.class -Force -ErrorAction SilentlyContinue

# 3. Build
mvn clean package -DskipTests

# 4. Verify
Get-ChildItem target/*-executable.jar -ErrorAction SilentlyContinue

# 5. Test run (optional - will open app)
# java -jar target/NexusBoard-3.0.0-executable.jar
```

### Batch Script - saved as `build.bat`
```batch
@echo off
setlocal enabledelayedexpansion

echo Cleaning .class files...
del src\main\java\com\nexus\model\*.class /Q 2>nul

echo Building Nexus Board...
call mvn clean package -DskipTests

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✓ Build successful!
    echo ✓ JAR Location: target\NexusBoard-3.0.0-executable.jar
    echo.
    pause
) else (
    echo.
    echo ✗ Build failed!
    pause
)
```

### Bash Script - saved as `build.sh`
```bash
#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "Cleaning .class files..."
find src/main/java/com/nexus/model -name "*.class" -delete 2>/dev/null || true

echo "Building Nexus Board..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Build successful!"
    echo "✓ JAR Location: target/NexusBoard-3.0.0-executable.jar"
    echo ""
else
    echo ""
    echo "✗ Build failed!"
    exit 1
fi
```

---

## 📝 Summary of Commands by Task

| Task | Command |
|------|---------|
| **Clean .class files** | `Remove-Item src/main/java/com/nexus/model/*.class -Force` |
| **Build with tests** | `mvn clean package` |
| **Build fast (no tests)** | `mvn clean package -DskipTests` |
| **List JARs** | `Get-ChildItem target/*.jar` or `ls -lh target/*.jar` |
| **Run JAR** | `java -jar target/NexusBoard-3.0.0-executable.jar` |
| **Run with 2GB RAM** | `java -Xmx2G -jar target/NexusBoard-3.0.0-executable.jar` |
| **Inspect JAR** | `jar tf target/NexusBoard-3.0.0-executable.jar \| head -20` |
| **Check Dependencies** | `mvn dependency:tree` |
| **Show pom.xml Info** | `mvn help:describe -Dplugin=org.apache.maven.plugins:maven-shade-plugin` |
| **Clean everything** | `mvn clean` |
| **Just compile** | `mvn compile` |
| **Run tests only** | `mvn test` |
