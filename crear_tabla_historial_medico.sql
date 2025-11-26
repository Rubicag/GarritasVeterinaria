-- ========================================
-- CREAR TABLA historial_medico
-- ========================================
-- Esta tabla reemplaza a historialclinico con campos mejorados
-- Incluye enums para tipo_consulta y estado, relación con veterinario, 
-- campos financieros y soporte opcional para archivos adjuntos

DROP TABLE IF EXISTS historial_medico;

CREATE TABLE historial_medico (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Relaciones (Foreign Keys)
    id_mascota BIGINT NOT NULL,
    id_veterinario BIGINT DEFAULT NULL,
    
    -- Fechas
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_consulta DATETIME DEFAULT NULL,
    
    -- Clasificación de la consulta
    tipo_consulta ENUM('CONSULTA', 'VACUNACION', 'CIRUGIA', 'EMERGENCIA', 'CONTROL') 
        DEFAULT 'CONSULTA',
    
    -- Información médica
    motivo_consulta VARCHAR(500) DEFAULT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    tratamiento VARCHAR(255) DEFAULT NULL,
    medicamentos VARCHAR(500) DEFAULT NULL,
    observaciones VARCHAR(255) DEFAULT NULL,
    
    -- Estado y seguimiento
    estado ENUM('COMPLETADO', 'PENDIENTE', 'EN_PROGRESO') 
        DEFAULT 'COMPLETADO',
    
    -- Información financiera
    costo DECIMAL(10, 2) DEFAULT 0.00,
    
    -- Archivos adjuntos (opcional - análisis, radiografías, etc.)
    archivo LONGBLOB DEFAULT NULL,
    nombre_archivo VARCHAR(255) DEFAULT NULL,
    tipo_archivo VARCHAR(100) DEFAULT NULL,
    tamanio_archivo BIGINT DEFAULT NULL,
    
    -- Foreign key constraints
    CONSTRAINT fk_historial_mascota 
        FOREIGN KEY (id_mascota) REFERENCES mascota(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_historial_veterinario 
        FOREIGN KEY (id_veterinario) REFERENCES usuario(id) 
        ON DELETE SET NULL,
    
    -- Índices para mejorar rendimiento
    INDEX idx_mascota (id_mascota),
    INDEX idx_veterinario (id_veterinario),
    INDEX idx_fecha (fecha),
    INDEX idx_tipo_consulta (tipo_consulta),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- DATOS DE EJEMPLO (opcional)
-- ========================================
-- Puedes descomentar estas líneas para insertar datos de prueba


INSERT INTO historial_medico 
(id_mascota, id_veterinario, fecha, fecha_consulta, tipo_consulta, motivo_consulta, 
 diagnostico, tratamiento, medicamentos, estado, costo) 
VALUES
(1, 2, '2024-01-15 10:30:00', '2024-01-15 10:30:00', 'CONSULTA', 
 'Revisión de rutina', 'Mascota en buen estado general', 
 'Continuar con dieta actual', 'Vitaminas multiples', 'COMPLETADO', 45.00),

(1, 2, '2024-02-20 14:00:00', '2024-02-20 14:00:00', 'VACUNACION', 
 'Vacuna antirrábica anual', 'Aplicación de vacuna antirrábica', 
 'Observación 30 minutos post-vacuna', 'Vacuna antirrábica', 'COMPLETADO', 35.00),

(2, 3, '2024-03-10 09:15:00', '2024-03-10 09:15:00', 'EMERGENCIA', 
 'Vómitos y diarrea', 'Gastroenteritis aguda', 
 'Dieta blanda 3 días, hidratación', 'Metronidazol 250mg', 'COMPLETADO', 85.00),

(3, 2, '2024-04-05 11:00:00', '2024-04-05 11:00:00', 'CIRUGIA', 
 'Esterilización programada', 'Procedimiento exitoso sin complicaciones', 
 'Antibiótico 7 días, reposo 10 días', 'Amoxicilina 500mg', 'COMPLETADO', 250.00),

(4, 3, '2024-05-12 16:30:00', '2024-05-12 16:30:00', 'CONTROL', 
 'Control post-cirugía', 'Herida cicatrizando correctamente', 
 'Retiro de puntos en 3 días', NULL, 'COMPLETADO', 25.00),

(5, 2, '2024-11-18 10:00:00', NULL, 'CONSULTA', 
 'Dolor en pata trasera', 'Pendiente exámenes radiográficos', 
 'Antiinflamatorio temporal', 'Meloxicam 5mg', 'PENDIENTE', 0.00);


-- ========================================
-- CONSULTAS ÚTILES
-- ========================================

-- Ver historial completo de una mascota (ejemplo: id_mascota = 1)
-- SELECT h.*, m.nombre as nombre_mascota, u.nombre as nombre_veterinario
-- FROM historial_medico h
-- LEFT JOIN mascota m ON h.id_mascota = m.id
-- LEFT JOIN usuario u ON h.id_veterinario = u.id
-- WHERE h.id_mascota = 1
-- ORDER BY h.fecha DESC;

-- Consultas pendientes
-- SELECT * FROM historial_medico 
-- WHERE estado = 'PENDIENTE' 
-- ORDER BY fecha DESC;

-- Ingresos por tipo de consulta
-- SELECT tipo_consulta, COUNT(*) as total, SUM(costo) as ingreso_total
-- FROM historial_medico
-- WHERE estado = 'COMPLETADO'
-- GROUP BY tipo_consulta;

-- Historial con archivos adjuntos
-- SELECT id_historial, id_mascota, diagnostico, nombre_archivo, 
--        ROUND(tamanio_archivo/1024, 2) as tamanio_kb
-- FROM historial_medico
-- WHERE archivo IS NOT NULL;
