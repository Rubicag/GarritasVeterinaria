-- Verificar datos en la base de datos
SELECT COUNT(*) as total_usuarios FROM usuario;

SELECT COUNT(*) as total_mascotas FROM mascota;

SELECT COUNT(*) as total_citas FROM cita;

-- Mostrar algunos registros de muestra
SELECT id, username, email, rol FROM usuario LIMIT 5;

SELECT id, nombre, especie, raza, edad, propietario_id FROM mascota LIMIT 5;

SELECT id, fecha, estado, mascota_id FROM cita LIMIT 5;

-- Verificar estructura de tablas
SHOW TABLES;