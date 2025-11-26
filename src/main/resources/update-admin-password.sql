-- Actualiza la contraseña del usuario admin a texto plano
UPDATE usuario SET contrasena = '123456' WHERE usuario = 'admin';