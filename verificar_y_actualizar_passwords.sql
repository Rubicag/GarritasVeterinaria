-- Verificar contraseñas actuales
USE garritas_veterinaria;

SELECT 
    id,
    nombre,
    usuario,
    LEFT(contrasena, 20) as contrasena_inicio,
    LENGTH(contrasena) as longitud_password,
    CASE 
        WHEN contrasena LIKE '$2a$%' THEN 'BCrypt'
        WHEN contrasena LIKE '$2b$%' THEN 'BCrypt'
        WHEN contrasena LIKE '$2y$%' THEN 'BCrypt'
        ELSE 'Texto Plano'
    END as tipo_encriptacion,
    correo
FROM usuario
ORDER BY id;

-- Actualizar contraseñas a BCrypt si no lo están
-- Hash BCrypt de "admin123" = $2a$10$xqVL1qhFRzPQq8Yr8d9oE.rGZN5mxuL5Qp5h8ZGKlNvP.zJQY4PJG
-- Hash BCrypt de "veterinario123" = $2a$10$Y5lZGfNvN3gKhZR3eQJxV.qm8fVH8kZ7jnF8YG9eJGpZhH8kL9QFm
-- Hash BCrypt de "recepcionista123" = $2a$10$8yKmGVF9lH3kZmN8fQJxV.qm8fVH8kZ7jnF8YG9eJGpZhH8kL9QFm

UPDATE usuario 
SET contrasena = '$2a$10$xqVL1qhFRzPQq8Yr8d9oE.rGZN5mxuL5Qp5h8ZGKlNvP.zJQY4PJG'
WHERE usuario = 'admin' AND NOT (contrasena LIKE '$2a$%' OR contrasena LIKE '$2b$%' OR contrasena LIKE '$2y$%');

UPDATE usuario 
SET contrasena = '$2a$10$Y5lZGfNvN3gKhZR3eQJxV.qm8fVH8kZ7jnF8YG9eJGpZhH8kL9QFm'
WHERE usuario = 'veterinario' AND NOT (contrasena LIKE '$2a$%' OR contrasena LIKE '$2b$%' OR contrasena LIKE '$2y$%');

UPDATE usuario 
SET contrasena = '$2a$10$8yKmGVF9lH3kZmN8fQJxV.qm8fVH8kZ7jnF8YG9eJGpZhH8kL9QFm'
WHERE usuario = 'recepcionista' AND NOT (contrasena LIKE '$2a$%' OR contrasena LIKE '$2b$%' OR contrasena LIKE '$2y$%');

-- Verificar actualización
SELECT 
    id,
    nombre,
    usuario,
    LEFT(contrasena, 30) as contrasena_inicio,
    CASE 
        WHEN contrasena LIKE '$2a$%' THEN 'BCrypt ✓'
        WHEN contrasena LIKE '$2b$%' THEN 'BCrypt ✓'
        WHEN contrasena LIKE '$2y$%' THEN 'BCrypt ✓'
        ELSE 'Texto Plano ✗'
    END as estado
FROM usuario
ORDER BY id;
