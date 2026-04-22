@echo off
REM Run Nexus Board from JAR file

echo ========================================
echo Nexus Board - Run from JAR
echo ========================================
echo.

REM Check if JAR files exist
if not exist "target\NexusBoard-fat.jar" (
    if not exist "target\NexusBoard-jar-with-dependencies.jar" (
        echo ERROR: JAR files not found!
        echo.
        echo Please build the project first by running: build-executable.bat
        echo.
        pause
        exit /b 1
    )
    set JAR_FILE=target\NexusBoard-jar-with-dependencies.jar
) else (
    set JAR_FILE=target\NexusBoard-fat.jar
)

echo Starting Nexus Board v2.0...
echo JAR file: %JAR_FILE%
echo.

java -jar %JAR_FILE%

if errorlevel 1 (
    echo.
    echo ERROR: Failed to start application
    echo Please ensure Java 17+ is installed and in PATH
    pause
)
