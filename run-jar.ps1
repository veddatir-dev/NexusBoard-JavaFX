#!/usr/bin/env pwsh
# Run Nexus Board from JAR file

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Nexus Board - Run from JAR" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if JAR files exist
$fatJar = "target\NexusBoard-fat.jar"
$assemblyJar = "target\NexusBoard-jar-with-dependencies.jar"

if (Test-Path $fatJar) {
    $jarFile = $fatJar
} elseif (Test-Path $assemblyJar) {
    $jarFile = $assemblyJar
} else {
    Write-Host "ERROR: JAR files not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please build the project first by running:" -ForegroundColor Yellow
    Write-Host "   .\build-executable.ps1" -ForegroundColor Yellow
    Write-Host "or" -ForegroundColor Yellow
    Write-Host "   build-executable.bat" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Starting Nexus Board v2.0..." -ForegroundColor Green
Write-Host "JAR file: $jarFile" -ForegroundColor Cyan
Write-Host ""

java -jar $jarFile

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Failed to start application" -ForegroundColor Red
    Write-Host "Please ensure Java 17+ is installed and in PATH" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
}
