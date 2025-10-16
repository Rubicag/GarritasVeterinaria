-- Script para migrar todas las contraseñas a BCrypt
-- Usa hashes BCrypt válidos generados correctamente

-- Hash BCrypt para "admin" (para el usuario admin)
UPDATE usuario SET contrasena = '$2a$10$N.zmdr9k7uOCQb07YxePSeEeU.H6JKpxGOB4z2uTLtS8HAJ5KUyLO' WHERE usuario = 'admin';

-- Hash BCrypt para "123456" (para los demás usuarios)
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'veterinario';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'doctora';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'cliente1';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'recepcion';

-- Verificar que todas las contraseñas estén en formato BCrypt
SELECT 
    id_usuario, 
    usuario, 
    LEFT(contrasena, 35) as hash_inicio, 
    LENGTH(contrasena) as longitud,
    CASE 
        WHEN contrasena LIKE '$2a$%' THEN 'BCrypt válido'
        WHEN contrasena LIKE '$2b$%' THEN 'BCrypt válido'
        WHEN contrasena LIKE '$2y$%' THEN 'BCrypt válido'
        ELSE 'Texto plano - Necesita migración'
    END as estado_password
FROM usuario 
ORDER BY id_usuario;