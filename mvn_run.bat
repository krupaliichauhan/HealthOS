@echo off
title HealthOS Desktop Application (Maven JavaFX)
echo ====================================================================
echo  Launching HealthOS with JavaFX Maven Plugin
echo ====================================================================
call "%~dp0mvnw.cmd" javafx:run
pause
