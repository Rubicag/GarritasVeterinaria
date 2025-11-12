-- =====================================================
-- SCRIPT PARA ASIGNAR VETERINARIOS A CITAS
-- =====================================================
-- Ejecutar DESPUÉS de insert_veterinarios.sql
-- =====================================================
-- Tu tabla tiene estados: 'Pendiente', 'Atendida', 'Cancelada'
-- Citas existentes: 6 (id_cita 1-6)
-- =====================================================

-- Paso 1: Actualizar citas existentes (id_cita 1-6)
-- Las citas 1, 2, 4, 6 ya tienen veterinario asignado
-- Vamos a redistribuir para tener más variedad

-- Mantener Dr. Juan Perez (id=2) en cita 1
-- Cambiar cita 2 a Dra. Maria Lopez (id=3)
UPDATE cita SET id_veterinario = 3 WHERE id_cita = 2;

-- Cita 3 ya tiene veterinario 3, cambiar a Dr. Carlos Ramirez (nuevo)
UPDATE cita SET id_veterinario = (SELECT id_usuario FROM usuario WHERE usuario = 'drcarlos'), 
                estado = 'Pendiente' WHERE id_cita = 3;

-- Cita 4 cambiar a Dra. Laura Martinez (nuevo)
UPDATE cita SET id_veterinario = (SELECT id_usuario FROM usuario WHERE usuario = 'dralaura') 
WHERE id_cita = 4;

-- Cita 5 cambiar a Dr. Roberto Sanchez (nuevo) y establecer estado
UPDATE cita SET id_veterinario = (SELECT id_usuario FROM usuario WHERE usuario = 'drroberto'),
                estado = 'Pendiente' WHERE id_cita = 5;

-- Cita 6 cambiar a Dra. Patricia Torres (nuevo)
UPDATE cita SET id_veterinario = (SELECT id_usuario FROM usuario WHERE usuario = 'drapatricia') 
WHERE id_cita = 6;

-- =====================================================
-- Paso 2: Crear nuevas citas con veterinarios asignados
-- =====================================================
-- Estados válidos: 'Pendiente', 'Atendida', 'Cancelada'
-- Basado en tus 6 mascotas existentes (id_mascota 1-6)
-- Servicios existentes (id_servicio 1-6)

-- Citas para Dr. Carlos Ramirez - Noviembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-11-15 10:00:00', '10:00:00', 'Pendiente', 1, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'drcarlos'), 'Vacunación anual'),
('2025-11-16 14:30:00', '14:30:00', 'Pendiente', 2, 2, (SELECT id_usuario FROM usuario WHERE usuario = 'drcarlos'), 'Revisión dental');

-- Citas para Dra. Laura Martinez - Noviembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-11-17 09:00:00', '09:00:00', 'Pendiente', 3, 3, (SELECT id_usuario FROM usuario WHERE usuario = 'dralaura'), 'Cirugía programada'),
('2025-11-18 11:00:00', '11:00:00', 'Pendiente', 4, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'dralaura'), 'Control post-operatorio');

-- Citas para Dr. Roberto Sanchez - Noviembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-11-19 15:00:00', '15:00:00', 'Pendiente', 5, 4, (SELECT id_usuario FROM usuario WHERE usuario = 'drroberto'), 'Emergencia'),
('2025-11-20 10:30:00', '10:30:00', 'Pendiente', 1, 2, (SELECT id_usuario FROM usuario WHERE usuario = 'drroberto'), 'Consulta general');

-- Citas para Dra. Patricia Torres - Noviembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-11-21 13:00:00', '13:00:00', 'Pendiente', 2, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'drapatricia'), 'Vacunación antirrábica'),
('2025-11-22 16:00:00', '16:00:00', 'Pendiente', 3, 6, (SELECT id_usuario FROM usuario WHERE usuario = 'drapatricia'), 'Baño y peluquería');

-- Citas para Dr. Miguel Fernandez - Noviembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-11-23 09:30:00', '09:30:00', 'Pendiente', 4, 3, (SELECT id_usuario FROM usuario WHERE usuario = 'drmiguel'), 'Ecografía'),
('2025-11-24 14:00:00', '14:00:00', 'Pendiente', 5, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'drmiguel'), 'Vacunación múltiple');

-- =====================================================
-- Paso 3: Crear citas pasadas para reportes históricos
-- =====================================================
-- Estado 'Atendida' para citas completadas del pasado

-- Citas de octubre 2025 con diferentes veterinarios
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
-- Dr. Juan Perez (id=2)
('2025-10-05 10:00:00', '10:00:00', 'Atendida', 1, 1, 2, 'Consulta general - Todo normal'),
('2025-10-12 11:30:00', '11:30:00', 'Atendida', 2, 2, 2, 'Limpieza dental exitosa'),
-- Dra. Maria Lopez (id=3)
('2025-10-08 09:00:00', '09:00:00', 'Atendida', 3, 1, 3, 'Vacunación aplicada'),
('2025-10-15 15:00:00', '15:00:00', 'Atendida', 4, 4, 3, 'Emergencia atendida a tiempo'),
-- Dr. Carlos Ramirez
('2025-10-10 14:00:00', '14:00:00', 'Atendida', 5, 2, (SELECT id_usuario FROM usuario WHERE usuario = 'drcarlos'), 'Consulta de rutina'),
('2025-10-20 10:30:00', '10:30:00', 'Atendida', 1, 3, (SELECT id_usuario FROM usuario WHERE usuario = 'drcarlos'), 'Cirugía menor - Recuperación exitosa');

-- Citas de septiembre 2025
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-09-05 10:00:00', '10:00:00', 'Atendida', 2, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'dralaura'), 'Control anual'),
('2025-09-12 13:00:00', '13:00:00', 'Atendida', 3, 2, (SELECT id_usuario FROM usuario WHERE usuario = 'drroberto'), 'Desparasitación'),
('2025-09-18 11:00:00', '11:00:00', 'Atendida', 4, 1, (SELECT id_usuario FROM usuario WHERE usuario = 'drapatricia'), 'Vacunación'),
('2025-09-25 16:00:00', '16:00:00', 'Atendida', 5, 3, (SELECT id_usuario FROM usuario WHERE usuario = 'drmiguel'), 'Radiografía - Sin problemas');

-- Algunas citas canceladas para variedad
INSERT INTO cita (fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
('2025-10-25 14:00:00', '14:00:00', 'Cancelada', 6, 1, 2, 'Cliente canceló con anticipación'),
('2025-10-28 09:00:00', '09:00:00', 'Cancelada', 1, 2, 3, 'Mascota enferma, reprogramar');

-- =====================================================
-- Verificar asignaciones
-- =====================================================

-- Ver distribución de citas por veterinario
SELECT 
    u.nombre,
    u.apellido,
    COUNT(c.id_cita) as total_citas,
    SUM(CASE WHEN c.estado = 'Completada' THEN 1 ELSE 0 END) as completadas,
    SUM(CASE WHEN c.estado = 'Programada' THEN 1 ELSE 0 END) as programadas
FROM usuario u
LEFT JOIN cita c ON u.id_usuario = c.id_veterinario
WHERE u.id_rol = 2
GROUP BY u.id_usuario, u.nombre, u.apellido
ORDER BY total_citas DESC;

-- Ver citas sin veterinario asignado
SELECT COUNT(*) as citas_sin_veterinario 
FROM cita 
WHERE id_veterinario IS NULL;
