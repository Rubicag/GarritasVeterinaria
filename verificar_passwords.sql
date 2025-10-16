-- Script para verificar las contraseñas almacenadas en la base de datos
SELECT id, usuario, contrasena, LENGTH(contrasena) as longitud_password
FROM usuarios 
ORDER BY id;

-- Verificar si las contraseñas empiezan con $2a$ (BCrypt)
SELECT id, usuario, 
       CASE 
           WHEN contrasena LIKE '$2a$%' THEN 'BCrypt - Correcto'
           WHEN contrasena LIKE '$2b$%' THEN 'BCrypt - Correcto'
           WHEN contrasena LIKE '$2y$%' THEN 'BCrypt - Correcto'
           ELSE 'Texto plano - INCORRECTO'
       END as tipo_password,
       LEFT(contrasena, 20) as inicio_password
FROM usuarios 
ORDER BY id;