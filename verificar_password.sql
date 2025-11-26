-- Verificar el estado actual de las contraseñas en MySQL
USE garritas_veterinaria;

-- Ver la contraseña del usuario admin
SELECT 
    id_usuario,
    usuario,
    nombre,
    CHAR_LENGTH(contrasena) as 'Longitud Password',
    LEFT(contrasena, 15) AS 'Primeros 15 chars',
    CASE 
        WHEN LEFT(contrasena, 4) = '$2a$' THEN 'BCrypt (CORRECTO)'
        WHEN CHAR_LENGTH(contrasena) = 6 THEN 'Texto plano (INCORRECTO)'
        ELSE 'Formato desconocido'
    END AS 'Tipo'
FROM usuario 
WHERE usuario = 'admin';

-- Ver todos los usuarios para comparar
SELECT 
    id_usuario,
    usuario,
    CHAR_LENGTH(contrasena) as 'Long',
    LEFT(contrasena, 10) AS 'Inicio'
FROM usuario 
ORDER BY id_usuario;
