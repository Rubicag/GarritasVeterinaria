-- ========================================
-- ACTUALIZACIÓN DE CONTRASEÑAS CON BCRYPT
-- Base de datos: garritas_veterinaria
-- ========================================
-- Este script actualiza las contraseñas en texto plano a BCrypt hashes
-- BCrypt hash de "123456" = $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- ========================================

USE garritas_veterinaria;

-- Actualizar todas las contraseñas a BCrypt hash de "123456"
UPDATE usuario 
SET contrasena = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE id_usuario IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

-- Verificar la actualización
SELECT id_usuario, usuario, nombre, apellido, 
       SUBSTRING(contrasena, 1, 20) AS 'Hash (primeros 20 chars)'
FROM usuario
ORDER BY id_usuario;

-- ========================================
-- RESULTADO ESPERADO:
-- Todos los usuarios ahora tendrán contraseña hasheada
-- Para iniciar sesión usar:
--   Usuario: admin, veterinario, doctora, cliente1, recepcion, etc.
--   Contraseña: 123456
-- ========================================
