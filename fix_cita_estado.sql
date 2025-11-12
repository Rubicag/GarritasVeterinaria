-- Script para corregir valores vacíos o NULL en el campo estado de la tabla cita
-- Ejecutar desde MySQL Workbench, phpMyAdmin o línea de comandos

USE garritas_veterinaria;

-- Ver el estado actual de todos los registros
SELECT id_cita, fecha, hora, estado, LENGTH(estado) as longitud_estado, id_mascota 
FROM cita 
ORDER BY id_cita;

-- Actualizar TODOS los registros a 'Pendiente' para asegurar que no haya valores inválidos
UPDATE cita 
SET estado = 'Pendiente';

-- Verificar los resultados después de la corrección
SELECT id_cita, fecha, hora, estado, id_mascota 
FROM cita 
ORDER BY id_cita;
