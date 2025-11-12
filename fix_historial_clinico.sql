-- Script para actualizar la tabla historialclinico agregando campos necesarios
-- Ejecutar desde MySQL Workbench, phpMyAdmin o línea de comandos

USE garritas_veterinaria;

-- Agregar nuevas columnas a la tabla historialclinico
ALTER TABLE historialclinico
ADD COLUMN IF NOT EXISTS fecha_consulta datetime(6) DEFAULT NULL AFTER fecha,
ADD COLUMN IF NOT EXISTS tipo_consulta enum('CONSULTA','VACUNACION','CIRUGIA','EMERGENCIA','CONTROL') COLLATE utf8mb4_unicode_ci DEFAULT 'CONSULTA' AFTER fecha_consulta,
ADD COLUMN IF NOT EXISTS motivo_consulta varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER tipo_consulta,
ADD COLUMN IF NOT EXISTS medicamentos varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER tratamiento,
ADD COLUMN IF NOT EXISTS estado enum('COMPLETADO','PENDIENTE','EN_PROGRESO') COLLATE utf8mb4_unicode_ci DEFAULT 'COMPLETADO' AFTER observaciones,
ADD COLUMN IF NOT EXISTS costo decimal(10,2) DEFAULT 0.00 AFTER estado,
ADD COLUMN IF NOT EXISTS id_veterinario int(10) UNSIGNED DEFAULT NULL AFTER id_mascota;

-- Agregar foreign key para veterinario
ALTER TABLE historialclinico
ADD CONSTRAINT fk_historial_veterinario 
FOREIGN KEY (id_veterinario) REFERENCES usuario (id_usuario) ON UPDATE CASCADE;

-- Migrar datos existentes: copiar fecha a fecha_consulta
UPDATE historialclinico 
SET fecha_consulta = fecha,
    tipo_consulta = 'CONSULTA',
    estado = 'COMPLETADO'
WHERE fecha_consulta IS NULL;

-- Verificar la estructura actualizada
DESCRIBE historialclinico;

-- Ver los datos migrados
SELECT id_historial, fecha_consulta, tipo_consulta, diagnostico, tratamiento, estado, id_mascota, id_veterinario
FROM historialclinico
ORDER BY id_historial;
