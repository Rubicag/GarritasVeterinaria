-- ====================================
-- INICIALIZACIÓN DE BASE DE DATOS
-- Sistema Veterinario Garritas
-- ====================================

-- Usar la base de datos
USE garritas_veterinaria;

-- Limpiar datos existentes (opcional)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE historialclinico;
TRUNCATE TABLE cita;
TRUNCATE TABLE mascota;
TRUNCATE TABLE producto;
TRUNCATE TABLE servicio;
TRUNCATE TABLE reporte;
TRUNCATE TABLE usuario;
TRUNCATE TABLE rol;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar roles
INSERT INTO rol (nombre, descripcion) VALUES
('Administrador', 'Acceso completo al sistema'),
('Veterinario', 'Acceso a citas, historiales y mascotas'),
('Cliente', 'Acceso limitado a sus mascotas y citas'),
('Recepcionista', 'Gestión de citas y clientes');

-- Insertar usuarios de prueba (contraseñas: 123456)
INSERT INTO usuario (nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol) VALUES
('Carlos', 'Administrador', '12345678', 'admin@garritas.com', '987654321', 'Av. Principal 123', 'admin', '$2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K', 1),
('María', 'Veterinaria', '87654321', 'maria@garritas.com', '987654322', 'Calle Veterinaria 456', 'veterinario', '$2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K', 2),
('Juan', 'Pérez', '11111111', 'juan@email.com', '987654323', 'Av. Los Olivos 789', 'cliente1', '$2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K', 3),
('Ana', 'García', '22222222', 'ana@email.com', '987654324', 'Jr. Las Flores 321', 'cliente2', '$2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K', 3),
('Sofia', 'Recepcionista', '33333333', 'sofia@garritas.com', '987654325', 'Calle Recepción 654', 'recepcion', '$2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K', 4);

-- Insertar mascotas
INSERT INTO mascota (nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario) VALUES
('Max', 'Perro', 'Golden Retriever', 'Macho', '2020-05-15', 30.50, 3),
('Luna', 'Gato', 'Siames', 'Hembra', '2021-03-20', 4.20, 3),
('Rocky', 'Perro', 'Bulldog', 'Macho', '2019-08-10', 25.00, 4),
('Mia', 'Gato', 'Persa', 'Hembra', '2022-01-05', 3.80, 4),
('Toby', 'Perro', 'Labrador', 'Macho', '2020-12-25', 28.70, 3),
('Nala', 'Gato', 'Angora', 'Hembra', '2021-07-14', 5.10, 4);

-- Insertar servicios
INSERT INTO servicio (nombre, descripcion, precio) VALUES
('Consulta General', 'Revisión médica general de la mascota', 50.00),
('Vacunación', 'Aplicación de vacunas según cronograma', 35.00),
('Desparasitación', 'Tratamiento antiparasitario interno y externo', 40.00),
('Cirugía Menor', 'Procedimientos quirúrgicos menores', 200.00),
('Radiografía', 'Estudio radiológico', 80.00),
('Análisis de Sangre', 'Exámenes de laboratorio', 60.00),
('Limpieza Dental', 'Profilaxis dental para mascotas', 120.00),
('Esterilización', 'Cirugía de esterilización', 250.00);

-- Insertar productos (inventario)
INSERT INTO producto (nombre, descripcion, precio, stock, stock_minimo, categoria) VALUES
('Alimento Premium Perros', 'Alimento balanceado para perros adultos', 85.00, 50, 10, 'Alimentos'),
('Alimento Premium Gatos', 'Alimento balanceado para gatos adultos', 75.00, 30, 8, 'Alimentos'),
('Vacuna Múltiple Perros', 'Vacuna pentavalente para perros', 45.00, 25, 5, 'Medicamentos'),
('Vacuna Triple Gatos', 'Vacuna triple felina', 40.00, 20, 5, 'Medicamentos'),
('Antiparasitario Interno', 'Desparasitante oral', 25.00, 40, 10, 'Medicamentos'),
('Champú Antipulgas', 'Shampoo medicado antipulgas', 35.00, 15, 5, 'Higiene'),
('Collar Antipulgas', 'Collar repelente de pulgas', 20.00, 25, 8, 'Accesorios'),
('Correa Ajustable', 'Correa ajustable para perros', 30.00, 20, 5, 'Accesorios'),
('Transportadora', 'Jaula transportadora para mascotas', 120.00, 8, 2, 'Accesorios'),
('Antibiótico Amoxicilina', 'Antibiótico veterinario', 55.00, 15, 3, 'Medicamentos');

-- Insertar citas de ejemplo
INSERT INTO cita (id_mascota, id_usuario, fecha_hora, motivo, estado, observaciones) VALUES
(1, 3, '2025-10-15 09:00:00', 'Consulta General', 'Programada', 'Primera consulta del año'),
(2, 3, '2025-10-15 10:30:00', 'Vacunación', 'Programada', 'Vacuna anual'),
(3, 4, '2025-10-16 14:00:00', 'Revisión Post-Cirugía', 'Programada', 'Control después de esterilización'),
(4, 4, '2025-10-17 11:00:00', 'Limpieza Dental', 'Programada', 'Limpieza programada'),
(5, 3, '2025-10-14 16:00:00', 'Consulta General', 'Completada', 'Revisión general - todo normal'),
(6, 4, '2025-10-13 15:30:00', 'Desparasitación', 'Completada', 'Tratamiento aplicado correctamente');

-- Insertar historiales clínicos
INSERT INTO historialclinico (id_mascota, fecha_consulta, diagnostico, tratamiento, observaciones, id_veterinario) VALUES
(1, '2025-10-13 15:30:00', 'Estado saludable', 'Vitaminas y desparasitante', 'Mascota en excelente estado', 2),
(2, '2025-10-12 10:00:00', 'Vacunación rutinaria', 'Vacuna triple felina', 'Sin reacciones adversas', 2),
(3, '2025-10-10 14:30:00', 'Esterilización exitosa', 'Cirugía de esterilización', 'Procedimiento sin complicaciones', 2),
(4, '2025-10-08 11:15:00', 'Limpieza dental', 'Profilaxis dental completa', 'Dientes en buen estado', 2),
(5, '2025-10-05 09:45:00', 'Control de peso', 'Dieta y ejercicio', 'Sobrepeso leve', 2);

-- Insertar reportes de ejemplo
INSERT INTO reporte (titulo, descripcion, fecha_generacion, id_usuario) VALUES
('Reporte Mensual de Citas', 'Estadísticas de citas del mes de octubre', NOW(), 1),
('Inventario Bajo Stock', 'Productos con stock bajo el mínimo', NOW(), 1),
('Ingresos del Mes', 'Reporte de ingresos mensuales', NOW(), 1),
('Mascotas Atendidas', 'Reporte de mascotas atendidas este mes', NOW(), 2),
('Vacunaciones Realizadas', 'Control de vacunaciones del mes', NOW(), 2);

-- Verificar datos insertados
SELECT 'Roles insertados' as Tabla, COUNT(*) as Total FROM rol
UNION ALL
SELECT 'Usuarios insertados', COUNT(*) FROM usuario
UNION ALL
SELECT 'Mascotas insertadas', COUNT(*) FROM mascota
UNION ALL
SELECT 'Servicios insertados', COUNT(*) FROM servicio
UNION ALL
SELECT 'Productos insertados', COUNT(*) FROM producto
UNION ALL
SELECT 'Citas insertadas', COUNT(*) FROM cita
UNION ALL
SELECT 'Historiales insertados', COUNT(*) FROM historialclinico
UNION ALL
SELECT 'Reportes insertados', COUNT(*) FROM reporte;

-- Mensaje de confirmación
SELECT 'Base de datos inicializada correctamente!' as Estado;