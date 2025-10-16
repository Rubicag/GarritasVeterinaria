-- Datos completos de la base de datos Garritas Veterinaria
-- Este archivo contiene todos los datos de prueba para el sistema

-- Roles
INSERT INTO rol (id_rol, nombre, descripcion) VALUES
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'VETERINARIO', 'Personal veterinario'),
(3, 'CLIENTE', 'Cliente / Propietario de mascotas')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Usuarios con contraseñas encriptadas para BCrypt
INSERT INTO usuario (id_usuario, nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol) VALUES
(1, 'Admin', 'Sistema', '00000000', 'admin@garritas.com', '999999999', 'Calle Principal 1', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4fktVK5qZWqxGK5ZfaWU2aEIRaUG2', 1),
(2, 'Juan', 'Perez', '12345678', 'juan.perez@example.com', '987654321', 'Av. Siempre Viva 123', 'jperez', '$2a$10$Xd1DUHaKHFBUWZLhBzCYs.qGKzRsn8K6Np8D3cFtdP7Hg6Qz9zJm2', 3),
(3, 'Dra. Maria', 'Lopez', '23456789', 'maria.lopez@garritas.com', '987001122', 'Calle Salud 45', 'mlopez', '$2a$10$Rz5f4.hhZ2aRKz3dBXi9Ae3fKz7jPgHx2fCXwv1H6xUm8V7KyEaQS', 2)
ON DUPLICATE KEY UPDATE 
    nombre = VALUES(nombre),
    apellido = VALUES(apellido),
    correo = VALUES(correo),
    telefono = VALUES(telefono),
    direccion = VALUES(direccion);

-- Mascotas
INSERT INTO mascota (id_mascota, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario) VALUES
(1, 'Firulais', 'Canino', 'Criollo', 'Macho', '2021-03-10', 12.50, 2),
(2, 'Michi', 'Felino', 'Siames', 'Hembra', '2022-05-20', 4.30, 2),
(3, 'Luna', 'Felino', 'Persa', 'Hembra', '2024-06-01', 3.20, 1)
ON DUPLICATE KEY UPDATE 
    nombre = VALUES(nombre),
    especie = VALUES(especie),
    raza = VALUES(raza),
    peso = VALUES(peso);

-- Servicios
INSERT INTO servicio (id_servicio, nombre, descripcion, precio) VALUES
(1, 'Consulta General', 'Revisión médica completa y consulta veterinaria', 15.00),
(2, 'Vacunación', 'Aplicación de vacunas preventivas', 25.00),
(3, 'Baño y Corte', 'Servicio de higiene y estética para mascotas', 30.00),
(4, 'Desparasitación', 'Tratamiento antiparasitario interno y externo', 20.00),
(5, 'Cirugía Menor', 'Procedimientos quirúrgicos ambulatorios', 80.00)
ON DUPLICATE KEY UPDATE 
    descripcion = VALUES(descripcion),
    precio = VALUES(precio);

-- Productos
INSERT INTO producto (id_producto, nombre, descripcion, precio, stock) VALUES
(1, 'Alimento Premium Perros 1kg', 'Alimento balanceado premium para perros adultos', 12.50, 50),
(2, 'Shampoo Antipulgas 250ml', 'Shampoo medicado para control de pulgas y garrapatas', 7.99, 30),
(3, 'Collar Antipulgas', 'Collar repelente de pulgas y garrapatas, 6 meses de protección', 15.00, 25),
(4, 'Juguete Kong Clásico', 'Juguete resistente para perros medianos y grandes', 18.50, 15),
(5, 'Alimento Gatos Adultos 1kg', 'Alimento balanceado para gatos adultos', 14.00, 40)
ON DUPLICATE KEY UPDATE 
    descripcion = VALUES(descripcion),
    precio = VALUES(precio),
    stock = VALUES(stock);

-- Citas
INSERT INTO cita (id_cita, fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
(1, '2025-10-15 09:30:00', '09:30:00', 'Pendiente', 1, 1, 3, 'Consulta de rutina - Firulais se ve saludable'),
(2, '2025-10-16 11:00:00', '11:00:00', 'Pendiente', 2, 2, 3, 'Vacunación anual para Michi'),
(3, '2025-10-17 14:30:00', '14:30:00', 'Pendiente', 3, 3, 3, 'Baño y corte para Luna'),
(4, '2025-10-18 10:00:00', '10:00:00', 'Pendiente', 1, 4, 3, 'Desparasitación trimestral')
ON DUPLICATE KEY UPDATE 
    fecha = VALUES(fecha),
    estado = VALUES(estado),
    observaciones = VALUES(observaciones);

-- Historial Clínico
INSERT INTO historialclinico (id_historial, fecha, diagnostico, tratamiento, observaciones, id_mascota, notas) VALUES
(1, '2024-12-01 10:30:00', 'Otitis externa', 'Limpieza auricular + Otomax por 7 días', 'Paciente respondió bien al tratamiento', 1, 'Control en 15 días'),
(2, '2025-01-15 15:00:00', 'Vacunación preventiva', 'Vacuna múltiple + antirrábica', 'Sin reacciones adversas', 2, 'Próxima vacuna en 12 meses'),
(3, '2025-02-20 11:15:00', 'Control post-esterilización', 'Revisión de sutura', 'Cicatrización perfecta', 3, 'Alta médica completa')
ON DUPLICATE KEY UPDATE 
    diagnostico = VALUES(diagnostico),
    tratamiento = VALUES(tratamiento),
    observaciones = VALUES(observaciones);

-- Reportes
INSERT INTO reporte (id_reporte, titulo, descripcion, fecha_generacion, id_usuario) VALUES
(1, 'Reporte Mensual Octubre 2025', 'Resumen de actividades y ventas del mes de octubre', '2025-10-01 08:00:00', 1),
(2, 'Reporte de Citas Programadas', 'Listado de citas programadas para la próxima semana', '2025-10-10 09:00:00', 3),
(3, 'Inventario de Productos', 'Estado actual del inventario de productos veterinarios', '2025-10-05 16:30:00', 1)
ON DUPLICATE KEY UPDATE 
    descripcion = VALUES(descripcion),
    fecha_generacion = VALUES(fecha_generacion);