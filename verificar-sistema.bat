@echo off
echo ========================================
echo   VERIFICACION DEL SISTEMA
echo   Garritas Veterinaria
echo ========================================
echo.

echo [1] Verificando estado de MySQL...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] MySQL esta corriendo
) else (
    echo [ERROR] MySQL NO esta corriendo - Inicia XAMPP
)

echo.
echo [2] Verificando aplicacion Spring Boot...
tasklist /FI "IMAGENAME eq java.exe" 2>NUL | find /I /N "java.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] Aplicacion Java esta corriendo
) else (
    echo [AVISO] Aplicacion Java NO esta corriendo
)

echo.
echo [3] Verificando datos en la base de datos...
echo Ejecutando consulta SQL...
mysql -u root garritas_veterinaria -e "SELECT usuario, LEFT(contrasena, 20) as hash_preview, id_rol FROM usuario WHERE usuario = 'admin';" 2>NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] Base de datos accesible
) else (
    echo [ERROR] No se puede conectar a la base de datos
)

echo.
echo ========================================
echo   CREDENCIALES PARA PROBAR
echo ========================================
echo.
echo Usuario: admin          ^| Password: 123456
echo Usuario: veterinario    ^| Password: 123456
echo Usuario: doctora        ^| Password: 123456
echo Usuario: cliente1       ^| Password: 123456
echo Usuario: recepcion      ^| Password: 123456
echo.
echo ========================================
echo   URL DE ACCESO
echo ========================================
echo.
echo Login: http://localhost:8080/web/login
echo Dashboard: http://localhost:8080/dashboard
echo.
echo ========================================
echo   PROXIMOS PASOS
echo ========================================
echo.
echo 1. Si MySQL NO esta corriendo: Inicia XAMPP
echo 2. Si la app NO esta corriendo: Ejecuta 'iniciar-sistema.bat'
echo 3. Abre: http://localhost:8080/web/login
echo 4. Usa: admin / 123456
echo 5. Presiona Ctrl+F5 para limpiar cache del navegador
echo.
pause
