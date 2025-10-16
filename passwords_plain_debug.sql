-- ALTERNATIVA: Usar contraseñas en texto plano temporalmente para debug
-- SOLO PARA PRUEBAS - NO USAR EN PRODUCCIÓN

UPDATE usuario SET contrasena = '123456' WHERE usuario = 'admin';
UPDATE usuario SET contrasena = '123456' WHERE usuario = 'veterinario';
UPDATE usuario SET contrasena = '123456' WHERE usuario = 'doctora';
UPDATE usuario SET contrasena = '123456' WHERE usuario = 'cliente1';
UPDATE usuario SET contrasena = '123456' WHERE usuario = 'recepcion';

-- Verificar
SELECT id_usuario, usuario, contrasena, LENGTH(contrasena) as longitud
FROM usuario 
ORDER BY id_usuario;