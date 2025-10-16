-- SOLUCION RAPIDA: Actualizar contraseñas directamente
USE garritas_veterinaria;

-- Hash BCrypt conocido que funciona para "admin123"
UPDATE usuario SET contrasena = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE usuario = 'admin';

-- Verificar
SELECT id, usuario, nombre, LEFT(contrasena, 30) FROM usuario WHERE usuario = 'admin';
