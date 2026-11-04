@REM ----------------------------------------------------------------------------
@REM Maven Wrapper for HealthOS Desktop Application
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "MAVEN_SCRATCH=C:\Users\admin\.gemini\antigravity\scratch\maven\apache-maven-3.9.16\bin\mvn.cmd"

if exist "%MAVEN_SCRATCH%" (
    call "%MAVEN_SCRATCH%" %*
) else (
    mvn %*
)
