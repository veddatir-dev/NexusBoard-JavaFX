@echo off
REM Build Executable JAR for Nexus Board
REM This script will create an executable JAR file

echo ========================================
echo Nexus Board - Build Executable JAR
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo.
    echo Please install Maven from: https://maven.apache.org/download.cgi
    echo And add it to your system PATH
    echo.
    pause
    exit /b 1
)

echo Step 1: Cleaning previous builds...
call mvn clean

echo.
echo Step 2: Compiling source code...
call mvn compile

echo.
echo Step 3: Creating executable JAR files...
call mvn package

echo.
echo ========================================
echo Build Complete!
echo ========================================
echo.
echo Executable JAR files created in: target\
echo.
echo Files created:
echo   - NexusBoard-fat.jar (shade plugin)
echo   - NexusBoard-jar-with-dependencies.jar (assembly plugin)
echo.
echo To run the application:
echo   java -jar target\NexusBoard-fat.jar
echo or
echo   java -jar target\NexusBoard-jar-with-dependencies.jar
echo.
pause
