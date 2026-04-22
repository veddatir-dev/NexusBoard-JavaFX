#!/usr/bin/env pwsh
# Build Executable JAR for Nexus Board
# PowerShell script to create an executable JAR file

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Nexus Board - Build Executable JAR" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mvnPath = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnPath) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "And add it to your system PATH" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Step 1: Cleaning previous builds..." -ForegroundColor Green
mvn clean

Write-Host ""
Write-Host "Step 2: Compiling source code..." -ForegroundColor Green
mvn compile

Write-Host ""
Write-Host "Step 3: Creating executable JAR files..." -ForegroundColor Green
mvn package

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Build Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

if (Test-Path "target\NexusBoard-fat.jar") {
    Write-Host "Executable JAR files created in: target\" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Files created:" -ForegroundColor Green
    Get-Item "target\NexusBoard-*.jar" | ForEach-Object { Write-Host "   - $($_.Name)" }
    Write-Host ""
    Write-Host "To run the application:" -ForegroundColor Green
    Write-Host "   java -jar target\NexusBoard-fat.jar" -ForegroundColor Yellow
    Write-Host "or" -ForegroundColor Green
    Write-Host "   .\run-jar.ps1" -ForegroundColor Yellow
} else {
    Write-Host "ERROR: Build failed - JAR files not created" -ForegroundColor Red
}

Write-Host ""
Read-Host "Press Enter to exit"
