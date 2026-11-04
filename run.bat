@echo off
title HealthOS Desktop Application
echo ====================================================================
echo  Launching HealthOS Desktop Application (Java 21 / JavaFX)
echo ====================================================================
if exist "%~dp0target\healthos-app.jar" (
    java -jar "%~dp0target\healthos-app.jar"
) else if exist "%~dp0target\healthos-desktop-1.0.0.jar" (
    java -jar "%~dp0target\healthos-desktop-1.0.0.jar"
) else (
    echo HealthOS JAR not found. Building now...
    call "%~dp0mvnw.cmd" package -DskipTests
    if exist "%~dp0target\healthos-app.jar" (
        java -jar "%~dp0target\healthos-app.jar"
    ) else (
        java -jar "%~dp0target\healthos-desktop-1.0.0.jar"
    )
)
pause
