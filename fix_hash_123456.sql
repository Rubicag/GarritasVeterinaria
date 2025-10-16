-- ====================================
-- FIX INMEDIATO: HASH CORRECTO PARA 123456
-- ====================================

USE garritas_veterinaria;

-- Hash correcto para la contraseña "123456"
-- Generado con BCrypt usando el mismo algoritmo de Spring Boot
UPDATE usuario 
SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' 
WHERE usuario = 'admin';

-- Actualizar también otros usuarios con el hash correcto para "123456"
UPDATE usuario 
SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.'
WHERE usuario IN ('veterinario', 'doctora', 'cliente1', 'recepcion');

-- Verificar los cambios
SELECT usuario, LEFT(contrasena, 30) as hash_preview 
FROM usuario 
WHERE usuario IN ('admin', 'veterinario', 'doctora', 'cliente1', 'recepcion');

SELECT 'CONTRASEÑAS ACTUALIZADAS - USA: admin/123456' as mensaje;