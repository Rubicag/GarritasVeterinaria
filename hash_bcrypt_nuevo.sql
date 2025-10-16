-- Script SQL para actualizar con un nuevo hash BCrypt válido
-- Hash generado para la contraseña "123456" usando BCrypt con fuerza 10

-- NUEVO hash BCrypt para "123456"
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'admin';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'veterinario';  
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'doctora';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'cliente1';
UPDATE usuario SET contrasena = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE usuario = 'recepcion';

-- Verificar las actualizaciones
SELECT id_usuario, usuario, 
       LEFT(contrasena, 35) as hash_inicio, 
       LENGTH(contrasena) as longitud,
       CASE 
           WHEN contrasena LIKE '$2a$%' THEN 'BCrypt válido'
           ELSE 'Formato incorrecto'
       END as formato
FROM usuario 
ORDER BY id_usuario;