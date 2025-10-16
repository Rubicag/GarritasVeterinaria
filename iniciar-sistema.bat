@echo off
echo ===============================================
echo    INICIANDO SISTEMA VETERINARIO GARRITAS
echo ===============================================
echo.
echo Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no encontrado
    pause
    exit /b 1
)
echo.
echo Iniciando sistema Spring Boot...
echo.
.\mvnw.cmd spring-boot:run
pause