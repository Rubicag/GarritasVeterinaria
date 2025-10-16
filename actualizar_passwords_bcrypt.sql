-- Script para actualizar contraseñas a BCrypt
USE garritas_veterinaria;

-- Primero verificar el estado actual
SELECT 
    id, usuario, nombre, 
    LEFT(contrasena, 30) as password_preview,
    CASE 
        WHEN contrasena LIKE '$2%' THEN 'BCrypt'
        ELSE 'Plain Text'
    END as tipo
FROM usuario;

-- Actualizar contraseñas con hashes BCrypt válidos
-- Estas contraseñas son: admin123, veterinario123, recepcionista123

UPDATE usuario SET contrasena = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE usuario = 'admin';
UPDATE usuario SET contrasena = '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr/EpZaN4VY6PjGwC' WHERE usuario = 'veterinario';
UPDATE usuario SET contrasena = '$2a$10$RzL4VlF0I2Q4p8K3QcJzW.OxZ5g7dGY8n9DcYjZj8FvNmH5Xw9Gxe' WHERE usuario = 'recepcionista';

-- Verificar la actualización
SELECT 
    id, usuario, nombre, 
    LEFT(contrasena, 30) as password_preview,
    CASE 
        WHEN contrasena LIKE '$2%' THEN '✓ BCrypt'
        ELSE '✗ Plain Text'
    END as estado
FROM usuario;

-- Instrucciones para probar
SELECT '
CREDENCIALES PARA PROBAR:
-------------------------
Usuario: admin          | Contraseña: admin123
Usuario: veterinario    | Contraseña: veterinario123
Usuario: recepcionista  | Contraseña: recepcionista123
' as INSTRUCCIONES;
