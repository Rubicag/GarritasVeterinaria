

INSERT INTO rol (id_rol, nombre, descripcion) VALUES 
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'VETERINARIO', 'Médico veterinario'),
(3, 'USER', 'Cliente/Usuario regular');

-- Usuarios 
INSERT INTO usuario (id_usuario, nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol) VALUES
(1, 'Admin', 'Sistema', '00000000', 'admin@veterinaria.com', '999999999', 'Oficina Principal', 'admin', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 1),
(2, 'Juan', 'Perez', '12345678', 'juan@email.com', '987654321', 'Av. Principal 123', 'jperez', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 3),
(3, 'Maria', 'Lopez', '23456789', 'maria@veterinaria.com', '987654322', 'Calle Norte 456', 'dra_maria', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 2);

-- Servicios veterinarios
INSERT INTO servicio (id_servicio, nombre, descripcion, precio) VALUES
(1, 'Consulta General', 'Revisión médica completa y diagnóstico', 25.00),
(2, 'Vacunación', 'Aplicación de vacunas según calendario', 15.00),
(3, 'Baño y Peluquería', 'Servicio completo de higiene y estética', 18.00);

-- Mascotas (con estructura completa igual a MySQL)
INSERT INTO mascota (id_mascota, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario) VALUES
(1, 'Firulais', 'Canino', 'Criollo', 'Macho', '2021-03-10', 12.50, 2),
(2, 'Misu', 'Felino', 'Siames', 'Hembra', '2022-05-20', 4.30, 2),
(3, 'Max', 'Canino', 'Golden Retriever', 'Macho', '2023-01-15', 8.75, 3);

-- Citas
INSERT INTO cita (id_cita, id_mascota, id_servicio, id_veterinario, fecha, hora, estado, observaciones) VALUES
(1, 1, 1, 3, DATEADD('DAY', 2, CURRENT_DATE), '09:00:00', 'Pendiente', 'Primera consulta'),
(2, 2, 2, 3, DATEADD('DAY', 5, CURRENT_DATE), '10:00:00', 'Pendiente', 'Vacunación anual');

-- Productos (usando 'id_producto' consistente con JPA y MySQL)
INSERT INTO producto (id_producto, nombre, descripcion, precio, stock) VALUES
(1, 'Alimento Premium', 'Alimento balanceado para perros', 18.50, 25),
(2, 'Shampoo Especial', 'Shampoo hipoalergénico', 12.00, 15);

-- Historial Clínico (ajustado a la estructura JPA)
INSERT INTO historialclinico (id_historial, id_mascota, fecha, diagnostico, tratamiento, observaciones) VALUES
(1, 1, CURRENT_TIMESTAMP, 'Revisión preventiva', 'Vacunación múltiple y desparasitación', 'Mascota en excelente estado de salud'),
(2, 2, CURRENT_TIMESTAMP, 'Control de peso', 'Dieta balanceada recomendada', 'Seguimiento en 2 semanas'),
(3, 1, CURRENT_TIMESTAMP, 'Consulta inicial', 'Examen físico completo', 'Primera visita - historial creado');

INSERT INTO reporte (id_reporte, titulo, descripcion, fecha_generacion, id_usuario) VALUES
(1, 'Citas Diarias', 'Reporte de citas programadas para hoy', CURRENT_TIMESTAMP, 1),
(2, 'Inventario', 'Estado actual del inventario de productos', CURRENT_TIMESTAMP, 1),
(3, 'Ingresos Mensuales', 'Resumen de ingresos del mes actual', CURRENT_TIMESTAMP, 1);