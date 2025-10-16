@echo off
echo ===============================================
echo    DIAGNOSTICO DEL SISTEMA VETERINARIO
echo ===============================================
echo.
echo 1. Verificando Java...
java -version
echo.
echo 2. Verificando Maven...
.\mvnw.cmd -version
echo.
echo 3. Verificando estructura de archivos...
if exist pom.xml (
    echo [OK] pom.xml encontrado
) else (
    echo [ERROR] pom.xml no encontrado
)
echo.
if exist src\main\java\com\mycompany\VeterinariaApplication.java (
    echo [OK] Clase principal encontrada
) else (
    echo [ERROR] Clase principal no encontrada
)
echo.
echo 4. Verificando conexion a MySQL (puerto 3306)...
netstat -an | findstr :3306
if %errorlevel% equ 0 (
    echo [OK] MySQL parece estar corriendo
) else (
    echo [WARNING] MySQL podria no estar corriendo
)
echo.
echo 5. Intentando compilar...
.\mvnw.cmd compile -q
if %errorlevel% equ 0 (
    echo [OK] Compilacion exitosa
) else (
    echo [ERROR] Error en compilacion
)
echo.
echo ===============================================
echo Diagnostico completado
echo ===============================================
pause