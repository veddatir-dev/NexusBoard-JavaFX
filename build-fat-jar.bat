@echo off
REM Nexus Board Build Script - Run after installing Maven
REM This script builds the fat JAR executable

echo ========================================
echo Nexus Board v3.0 - Build Script
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo.
    echo Please install Maven first:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to C:\apache-maven-3.9.9
    echo 3. Add to PATH: set PATH=%%PATH%%;C:\apache-maven-3.9.9\bin
    echo.
    echo Or use full path in this script.
    echo.
    pause
    exit /b 1
)

echo Maven found! Starting build...
echo.

echo Step 1: Cleaning previous builds...
call mvn clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Building fat JAR (this may take 1-2 minutes)...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Build failed!
    echo Check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.

REM Check if JAR was created
if exist "target\NexusBoard-3.0.0-executable.jar" (
    echo ✓ Fat JAR created: target\NexusBoard-3.0.0-executable.jar
    echo.
    echo To run the application:
    echo   java -jar target\NexusBoard-3.0.0-executable.jar
    echo.
    echo Or run the included script:
    echo   .\run-jar.bat
    echo.
) else (
    echo ✗ ERROR: JAR file not found!
    echo Check build output for errors.
)

echo.
pause