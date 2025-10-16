@echo off
echo ========================================
echo   SISTEMA VETERINARIO - MODO PERFORMANCE
echo ========================================
echo.

REM Configuración de memoria optimizada para desarrollo
set MAVEN_OPTS=-Xmx2048m -Xms1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -Dspring.devtools.restart.enabled=false

echo Configuración PERFORMANCE aplicada:
echo - Heap máximo: 2GB
echo - Heap inicial: 1GB
echo - Garbage Collector: G1GC optimizado
echo - DevTools restart: DESHABILITADO
echo - String deduplication: HABILITADO
echo.

REM Verificar puerto
echo Verificando disponibilidad del puerto 8080...
netstat -ano | findstr :8080 >nul
if %ERRORLEVEL% == 0 (
    echo.
    echo ⚠️  PUERTO 8080 EN USO - Liberando...
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /f /pid %%a >nul 2>&1
    timeout /t 2 >nul
    echo ✅ Puerto liberado
)

echo.
echo 🚀 Iniciando aplicación en MODO PERFORMANCE...
echo 📊 Métricas habilitadas en: http://localhost:8080/actuator/health
echo 🌐 Aplicación disponible en: http://localhost:8080
echo.
echo ⏹️  Presiona Ctrl+C para detener
echo.

REM Compilar y ejecutar con configuración de performance
.\mvnw.cmd clean spring-boot:run -Dspring-boot.run.profiles=dev -Dspring.devtools.livereload.enabled=false

echo.
echo 🔄 Aplicación detenida
pause