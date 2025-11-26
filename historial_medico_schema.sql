-- Script SQL para crear la tabla historial_medico
-- Base de datos: garritas_veterinaria
-- Fecha: 19/11/2025
-- Compatible con estructura existente

USE garritas_veterinaria;

-- Eliminar tabla si existe (solo para testing)
DROP TABLE IF EXISTS historial_medico;

-- Crear tabla historial_medico
CREATE TABLE historial_medico (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Foreign Keys (usando los nombres correctos de tu BD)
    id_cita BIGINT NOT NULL,
    
    -- Fechas
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_consulta DATETIME DEFAULT NULL,
    
    -- Clasificación de la consulta
    tipo_consulta ENUM('CONSULTA', 'VACUNACION', 'CIRUGIA', 'EMERGENCIA', 'CONTROL') 
        DEFAULT 'CONSULTA'
        COMMENT 'Tipo de consulta realizada',
    
    -- Información médica
    motivo_consulta VARCHAR(500) DEFAULT NULL 
        COMMENT 'Motivo de la consulta',
    diagnostico VARCHAR(500) NOT NULL 
        COMMENT 'Diagnóstico del veterinario',
    tratamiento TEXT DEFAULT NULL 
        COMMENT 'Tratamiento prescrito',
    medicamentos VARCHAR(500) DEFAULT NULL 
        COMMENT 'Medicamentos recetados',
    observaciones TEXT DEFAULT NULL 
        COMMENT 'Notas y observaciones adicionales del veterinario',
    
    -- Estado y seguimiento
    estado ENUM('COMPLETADO', 'PENDIENTE', 'EN_PROGRESO') 
        DEFAULT 'COMPLETADO'
        COMMENT 'Estado del historial médico',
    
    -- Información financiera
    costo DECIMAL(10, 2) DEFAULT 0.00
        COMMENT 'Costo de la consulta/tratamiento',
    
    -- Archivos adjuntos (opcional - análisis, radiografías, etc.)
    archivo LONGBLOB DEFAULT NULL
        COMMENT 'Archivo adjunto (radiografía, análisis, etc.)',
    nombre_archivo VARCHAR(255) DEFAULT NULL
        COMMENT 'Nombre original del archivo',
    tipo_archivo VARCHAR(100) DEFAULT NULL
        COMMENT 'MIME type del archivo (image/jpeg, application/pdf, etc.)',
    tamanio_archivo BIGINT DEFAULT NULL
        COMMENT 'Tamaño del archivo en bytes',
    
    -- Foreign key constraint (referencia a tabla cita)
    CONSTRAINT fk_historial_cita 
        FOREIGN KEY (id_cita) 
        REFERENCES cita(id_cita) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    -- Índices para mejorar rendimiento de búsquedas
    INDEX idx_cita (id_cita),
    INDEX idx_fecha_registro (fecha_registro DESC),
    INDEX idx_tipo_consulta (tipo_consulta),
    INDEX idx_estado (estado)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Historial médico de las citas atendidas';

-- ========================================
-- DATOS DE EJEMPLO (basados en citas existentes)
-- ========================================

INSERT INTO historial_medico 
(id_cita, fecha_registro, fecha_consulta, tipo_consulta, motivo_consulta, 
 diagnostico, tratamiento, medicamentos, observaciones, estado, costo) 
VALUES
-- Cita 17: Consulta general - Todo normal
(17, '2025-10-05 10:30:00', '2025-10-05 10:00:00', 'CONSULTA', 
 'Consulta general de rutina', 
 'Mascota en buen estado general de salud', 
 'Continuar con dieta actual y ejercicio regular', 
 'Vitaminas múltiples (1 tableta diaria)', 
 'Todo normal. Próxima consulta en 6 meses', 
 'COMPLETADO', 50.00),

-- Cita 18: Limpieza dental exitosa
(18, '2025-10-12 11:45:00', '2025-10-12 11:30:00', 'CONSULTA', 
 'Limpieza dental programada', 
 'Presencia de sarro leve en molares', 
 'Limpieza dental realizada. Cepillado diario recomendado', 
 'Pasta dental canina', 
 'Procedimiento exitoso sin complicaciones', 
 'COMPLETADO', 75.00),

-- Cita 19: Vacunación aplicada
(19, '2025-10-08 09:15:00', '2025-10-08 09:00:00', 'VACUNACION', 
 'Vacunación antirrábica anual', 
 'Aplicación de vacuna antirrábica', 
 'Observación 30 minutos post-vacuna. Próxima dosis en 1 año', 
 'Vacuna antirrábica', 
 'Sin reacciones adversas', 
 'COMPLETADO', 35.00),

-- Cita 20: Emergencia atendida
(20, '2025-10-15 15:30:00', '2025-10-15 15:00:00', 'EMERGENCIA', 
 'Vómitos y diarrea desde la madrugada', 
 'Gastroenteritis aguda', 
 'Hidratación IV, dieta blanda 3 días, reposo', 
 'Metronidazol 250mg (cada 12h por 5 días)', 
 'Emergencia atendida a tiempo. Control en 48 horas', 
 'COMPLETADO', 120.00),

-- Cita 21: Consulta de rutina
(21, '2025-10-10 14:15:00', '2025-10-10 14:00:00', 'CONSULTA', 
 'Revisión de rutina', 
 'Estado general bueno', 
 'Continuar cuidados normales', 
 NULL, 
 'Todo en orden', 
 'COMPLETADO', 50.00),

-- Cita 22: Cirugía menor - Recuperación exitosa
(22, '2025-10-20 11:00:00', '2025-10-20 10:30:00', 'CIRUGIA', 
 'Extirpación de quiste pequeño', 
 'Quiste benigno en pata delantera', 
 'Cirugía menor exitosa. Antibiótico 7 días, reposo 10 días', 
 'Amoxicilina 500mg (cada 8h por 7 días), Analgésico', 
 'Procedimiento exitoso. Retiro de puntos en 10 días', 
 'COMPLETADO', 200.00),

-- Cita 23: Control anual
(23, '2025-09-05 10:20:00', '2025-09-05 10:00:00', 'CONTROL', 
 'Control anual de salud', 
 'Estado general óptimo', 
 'Continuar con cuidados actuales', 
 'Multivitamínico', 
 'Próximo control en 1 año', 
 'COMPLETADO', 50.00),

-- Cita 24: Desparasitación
(24, '2025-09-12 13:15:00', '2025-09-12 13:00:00', 'CONSULTA', 
 'Desparasitación programada', 
 'Desparasitación interna aplicada', 
 'Repetir en 3 meses', 
 'Antiparasitario interno (dosis única)', 
 'Procedimiento completado sin problemas', 
 'COMPLETADO', 40.00),

-- Cita 25: Vacunación
(25, '2025-09-18 11:10:00', '2025-09-18 11:00:00', 'VACUNACION', 
 'Vacuna múltiple', 
 'Aplicación de vacuna séxtuple', 
 'Observación 30 minutos. Próxima dosis según calendario', 
 'Vacuna séxtuple', 
 'Sin reacciones adversas', 
 'COMPLETADO', 35.00),

-- Cita 26: Radiografía
(26, '2025-09-25 16:20:00', '2025-09-25 16:00:00', 'CONSULTA', 
 'Cojera en pata trasera', 
 'Radiografía sin fracturas. Posible esguince leve', 
 'Reposo 7 días, antiinflamatorio', 
 'Meloxicam 5mg (cada 24h por 5 días)', 
 'Radiografía - Sin problemas óseos', 
 'COMPLETADO', 80.00);

-- ========================================
-- CONSULTAS ÚTILES
-- ========================================

-- Ver todos los historiales con información de citas
SELECT 
    h.id_historial,
    h.fecha_registro,
    h.tipo_consulta,
    h.diagnostico,
    h.estado,
    h.costo,
    c.fecha as fecha_cita,
    m.nombre as mascota,
    m.especie,
    u.nombre as veterinario,
    s.nombre as servicio
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN mascota m ON c.id_mascota = m.id_mascota
INNER JOIN usuario u ON c.id_veterinario = u.id_usuario
INNER JOIN servicio s ON c.id_servicio = s.id_servicio
ORDER BY h.fecha_registro DESC;

-- Ver historial completo de una mascota específica (ejemplo: id_mascota = 1)
SELECT 
    h.id_historial,
    h.fecha_registro,
    h.tipo_consulta,
    h.motivo_consulta,
    h.diagnostico,
    h.tratamiento,
    h.medicamentos,
    h.estado,
    h.costo,
    c.fecha as fecha_cita,
    u.nombre as veterinario,
    s.nombre as servicio
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN usuario u ON c.id_veterinario = u.id_usuario
INNER JOIN servicio s ON c.id_servicio = s.id_servicio
WHERE c.id_mascota = 1
ORDER BY h.fecha_registro DESC;

-- Historiales por tipo de consulta
SELECT 
    tipo_consulta,
    COUNT(*) as total_consultas,
    SUM(costo) as ingreso_total,
    AVG(costo) as costo_promedio
FROM historial_medico
WHERE estado = 'COMPLETADO'
GROUP BY tipo_consulta
ORDER BY total_consultas DESC;

-- Historiales con archivos adjuntos
SELECT 
    h.id_historial,
    m.nombre as mascota,
    h.diagnostico,
    h.nombre_archivo,
    h.tipo_archivo,
    ROUND(h.tamanio_archivo / 1024, 2) as tamanio_kb
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN mascota m ON c.id_mascota = m.id_mascota
WHERE h.archivo IS NOT NULL
ORDER BY h.fecha_registro DESC;

-- Historiales pendientes
SELECT 
    h.id_historial,
    m.nombre as mascota,
    h.motivo_consulta,
    h.diagnostico,
    c.fecha as fecha_cita
FROM historial_medico h
INNER JOIN cita c ON h.id_cita = c.id_cita
INNER JOIN mascota m ON c.id_mascota = m.id_mascota
WHERE h.estado = 'PENDIENTE'
ORDER BY c.fecha DESC;
