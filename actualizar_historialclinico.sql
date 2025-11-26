-- Script para agregar campos de archivo a la tabla historialclinico existente
-- Solo ejecutar si quieres habilitar la funcionalidad de archivos adjuntos
-- Si no necesitas esta funcionalidad, puedes omitir este script

USE garritas_veterinaria;

-- Agregar columnas para archivos adjuntos (OPCIONAL)
-- Descomenta estas líneas solo si quieres guardar archivos en la base de datos
/*
ALTER TABLE historialclinico
ADD COLUMN archivo LONGBLOB COMMENT 'Archivo adjunto (radiografía, análisis, etc.)',
ADD COLUMN nombre_archivo VARCHAR(255) COMMENT 'Nombre original del archivo',
ADD COLUMN tipo_archivo VARCHAR(100) COMMENT 'MIME type del archivo',
ADD COLUMN tamanio_archivo BIGINT COMMENT 'Tamaño del archivo en bytes';
*/

-- Verificar estructura actual
DESCRIBE historialclinico;

-- Query útil: Ver historiales recientes
SELECT 
    h.id_historial,
    m.nombre as mascota,
    u.nombre as veterinario,
    h.fecha_consulta,
    h.tipo_consulta,
    h.diagnostico,
    h.tratamiento,
    h.estado,
    h.costo
FROM historialclinico h
JOIN mascota m ON h.id_mascota = m.id_mascota
LEFT JOIN usuario u ON h.id_veterinario = u.id_usuario
ORDER BY h.fecha_consulta DESC
LIMIT 10;
