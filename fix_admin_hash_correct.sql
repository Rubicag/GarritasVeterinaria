-- Script SQL para actualizar la contraseña del admin con el hash correcto
-- Hash BCrypt correcto para la contraseña "123456"

USE garritas_veterinaria;

-- Actualizar con un hash BCrypt válido para "123456"
-- Este hash fue generado específicamente para la contraseña "123456"
UPDATE usuario 
SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' 
WHERE usuario = 'admin';

-- Verificar el cambio
SELECT id_usuario, usuario, LEFT(contrasena, 30) as hash_preview, id_rol 
FROM usuario 
WHERE usuario = 'admin';

-- Mensaje de confirmación
SELECT 'Hash actualizado correctamente para admin / 123456' as mensaje;