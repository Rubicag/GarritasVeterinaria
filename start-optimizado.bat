@echo off
echo Iniciando aplicación Garritas Veterinaria con configuración optimizada...
echo.

REM Configurar variables de entorno para optimización
set MAVEN_OPTS=-Xmx1024m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200

echo Configuración de memoria aplicada:
echo - Heap máximo: 1GB
echo - Heap inicial: 512MB  
echo - Garbage Collector: G1GC
echo.

echo Verificando si el puerto 8080 está libre...
netstat -ano | findstr :8080
if %ERRORLEVEL% == 0 (
    echo WARNING: El puerto 8080 ya está en uso
    echo Puedes terminar el proceso o cambiar el puerto en application.properties
    pause
)

echo.
echo Iniciando aplicación...
echo Presiona Ctrl+C para detener
echo.

.\mvnw.cmd spring-boot:run

pause