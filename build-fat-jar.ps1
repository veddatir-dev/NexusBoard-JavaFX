# Nexus Board Build Script - PowerShell
# Run after installing Maven

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Nexus Board v3.0 - Build Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is available
$mvnPath = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnPath) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Maven first:" -ForegroundColor Yellow
    Write-Host "1. Download from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "2. Extract to C:\apache-maven-3.9.9" -ForegroundColor Yellow
    Write-Host "3. Add to PATH: `$env:PATH += `";C:\apache-maven-3.9.9\bin`"" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Or edit this script to use full path." -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Maven found! Starting build..." -ForegroundColor Green
Write-Host ""

Write-Host "Step 1: Cleaning previous builds..." -ForegroundColor Green
mvn clean
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Clean failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Step 2: Building fat JAR (this may take 1-2 minutes)..." -ForegroundColor Green
mvn package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed!" -ForegroundColor Red
    Write-Host "Check the error messages above." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "BUILD SUCCESSFUL!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if JAR was created
if (Test-Path "target\NexusBoard-3.0.0-executable.jar") {
    Write-Host "✓ Fat JAR created: target\NexusBoard-3.0.0-executable.jar" -ForegroundColor Green
    Write-Host ""
    Write-Host "To run the application:" -ForegroundColor Green
    Write-Host "  java -jar target\NexusBoard-3.0.0-executable.jar" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Or run the included script:" -ForegroundColor Green
    Write-Host "  .\run-jar.ps1" -ForegroundColor Yellow
    Write-Host ""
} else {
    Write-Host "✗ ERROR: JAR file not found!" -ForegroundColor Red
    Write-Host "Check build output for errors." -ForegroundColor Red
}

Write-Host ""
Read-Host "Press Enter to exit"