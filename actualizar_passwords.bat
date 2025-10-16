@echo off
echo ========================================
echo   ACTUALIZACION DE PASSWORDS A BCRYPT
echo ========================================
echo.

REM Ejecutar el script SQL
mysql -u root -p < actualizar_passwords_bcrypt.sql

echo.
echo ========================================
echo   PROCESO COMPLETADO
echo ========================================
echo.
echo Las contraseñas han sido actualizadas a BCrypt.
echo.
echo CREDENCIALES PARA PROBAR:
echo Usuario: admin          ^| Password: admin123
echo Usuario: veterinario    ^| Password: veterinario123
echo Usuario: recepcionista  ^| Password: recepcionista123
echo.
pause
