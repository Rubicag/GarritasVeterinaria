@echo off
REM Wrapper for Maven (Windows)
set DIRNAME=%~dp0
rem Remove trailing backslash if present
if "%DIRNAME:~-1%"=="\" set DIRNAME=%DIRNAME:~0,-1%
java -Dmaven.multiModuleProjectDirectory="%DIRNAME%" -cp "%DIRNAME%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
