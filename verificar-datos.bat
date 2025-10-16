@echo off
echo ====================================
echo  VERIFICACIÓN DE DATOS - GARRITAS VETERINARIA
echo ====================================
echo.

echo 1. Compilando proyecto...
call .\mvnw.cmd clean compile -q

if %errorlevel% neq 0 (
    echo ERROR: No se pudo compilar el proyecto
    pause
    exit /b 1
)

echo 2. Iniciando con H2 (base de datos en memoria)...
echo    - Los datos de database_complete_data.sql se cargarán automáticamente
echo    - Accede a: http://localhost:8080/web/dashboard
echo    - Para H2 Console: http://localhost:8080/h2-console
echo.
echo DATOS ESPERADOS:
echo   - 3 Usuarios: Admin, Juan Perez, Dra. Maria Lopez
echo   - 3 Mascotas: Firulais (Canino), Michi (Felino), Luna (Felino)
echo   - 4 Citas programadas para octubre 2025
echo   - 5 Productos en inventario
echo   - 3 Registros de historial clínico
echo   - 3 Reportes del sistema
echo.
echo Presiona Ctrl+C para detener cuando termines de verificar

call .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=h2

pause