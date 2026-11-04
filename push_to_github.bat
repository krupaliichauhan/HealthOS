@echo off
setlocal
title Push HealthOS to GitHub
cls
echo ====================================================================
echo  HealthOS - Push to GitHub
echo  Repository: https://github.com/krupaliichauhan/HealthOS
echo ====================================================================
echo.
echo Choose an authentication method:
echo   [1] Sign in via Browser
echo   [2] Paste GitHub Personal Access Token (PAT)
echo.
set "choice="
set /p "choice=Enter choice [1 or 2] (Default is 1): "

if "%choice%"=="2" goto use_token
goto use_browser

:use_token
echo.
echo ====================================================================
echo  Paste your GitHub Personal Access Token below:
echo  (Right-click in this window or press Ctrl+V to paste)
echo ====================================================================
set "token="
set /p "token=GitHub Token: "

if "%token%"=="" (
    echo [ERROR] No token was entered.
    goto failed
)

echo.
echo Pushing code to GitHub...
git push "https://%token%@github.com/krupaliichauhan/HealthOS.git" main --progress
if %ERRORLEVEL% EQU 0 goto success
goto failed

:use_browser
echo.
echo ====================================================================
echo  Connecting to GitHub...
echo  (Please check your browser or taskbar for the authorization tab)
echo ====================================================================
git push -u origin main --progress
if %ERRORLEVEL% EQU 0 goto success
goto failed

:success
echo.
echo ====================================================================
echo  SUCCESS! HealthOS has been successfully pushed to GitHub!
echo  View your repository: https://github.com/krupaliichauhan/HealthOS
echo ====================================================================
goto end

:failed
echo.
echo ====================================================================
echo  Push was not completed.
echo ====================================================================
goto end

:end
echo.
pause
