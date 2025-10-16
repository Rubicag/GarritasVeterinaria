@echo off
echo ====================================
echo  GARRITAS VETERINARIA - CARGA DE DATOS
echo ====================================
echo.

echo 1. Compilando proyecto...
call .\mvnw.cmd clean compile -q

if %errorlevel% neq 0 (
    echo ERROR: No se pudo compilar el proyecto
    echo Verifica que no haya errores en el código
    pause
    exit /b 1
)

echo 2. Iniciando aplicación con datos actualizados...
echo    - Datos de database_complete_data.sql integrados
echo    - Perfil MySQL configurado
echo    - JWT deshabilitado para pruebas
echo.
echo Presiona Ctrl+C para detener cuando se complete la carga

call .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql -q

pause