@echo off
cls
echo ====================================================
echo   INICIALIZANDO SISTEMA GARRITAS VETERINARIA
echo   MODO ULTRA PERFORMANCE - JVM OPTIMIZADA  
echo ====================================================
echo.

REM Variables de configuración
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot
set MAVEN_HOME=C:\apache-maven-3.9.9
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
set PROJECT_DIR=%~dp0

REM Configuración JVM ultra optimizada
set JAVA_OPTS=-Xms3G -Xmx4G ^
-XX:+UseG1GC ^
-XX:MaxGCPauseMillis=50 ^
-XX:G1HeapRegionSize=16m ^
-XX:+UseStringDeduplication ^
-XX:+OptimizeStringConcat ^
-XX:+UseCompressedOops ^
-XX:+UseCompressedClassPointers ^
-XX:+TieredCompilation ^
-XX:TieredStopAtLevel=4 ^
-XX:+UseCodeCacheFlushing ^
-XX:ReservedCodeCacheSize=256m ^
-XX:InitialCodeCacheSize=64m ^
-Djava.awt.headless=true ^
-Dspring.profiles.active=performance ^
-Dserver.tomcat.basedir=./target/tomcat ^
-Dspring.jpa.show-sql=false ^
-Dspring.devtools.restart.enabled=false

echo.
echo [INFO] Verificando puerto 8080...
netstat -ano | findstr :8080 > nul
if %ERRORLEVEL% == 0 (
    echo [WARNING] Puerto 8080 ocupado. Liberando...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
        echo Terminando proceso %%a
        taskkill /F /PID %%a > nul 2>&1
    )
    timeout /t 2 > nul
    echo [OK] Puerto liberado
) else (
    echo [OK] Puerto 8080 disponible
)

echo.
echo [INFO] Compilando proyecto con optimizaciones...
cd /d "%PROJECT_DIR%"

REM Compilación ultra rápida
call mvnw.cmd clean compile -DskipTests -T 4 -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Fallo en compilación
    pause
    exit /b 1
)

echo [OK] Compilación exitosa
echo.
echo [INFO] Iniciando servidor con JVM optimizada...
echo [INFO] Heap: 4GB, GC: G1, Profiles: performance
echo [INFO] Acceso: http://localhost:8080
echo [INFO] Usuario: admin / Contraseña: admin123
echo.
echo ====================================================
echo   SERVIDOR INICIANDO - PERFORMANCE MODE ACTIVO
echo ====================================================
echo.

REM Ejecutar con todas las optimizaciones
call mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="%JAVA_OPTS%" -Dspring-boot.run.profiles=performance -q

echo.
echo [INFO] Servidor detenido
pause