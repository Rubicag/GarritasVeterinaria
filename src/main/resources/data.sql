/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  LUIGGI
 * Created: 1 oct. 2025
 * Updated: 9 oct. 2025 - Alineado con nueva estructura de BD
 */

-- ========================================
-- DATOS DE EJEMPLO PARA DESARROLLO Y TESTS
-- Compatible con H2 y MySQL
-- Basado en la estructura real de la base de datos
-- ========================================

-- Roles del sistema
INSERT INTO rol (id_rol, nombre, descripcion) VALUES
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'VETERINARIO', 'Personal veterinario'),
(3, 'CLIENTE', 'Cliente / Propietario de mascotas'),
(4, 'RECEPCIONISTA', 'Recepcionista de la clínica');

-- Usuarios del sistema (con contraseñas en texto plano para desarrollo)
INSERT INTO usuario (id_usuario, nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol) VALUES
(1, 'Admin', 'Sistema', '00000000', 'admin@garritas.com', '999999999', 'Calle Principal 1', 'admin', '123456', 1),
(2, 'Dr. Juan', 'Perez', '12345678', 'juan.perez@garritas.com', '987654321', 'Av. Siempre Viva 123', 'veterinario', '123456', 2),
(3, 'Dra. Maria', 'Lopez', '23456789', 'maria.lopez@garritas.com', '987001122', 'Calle Salud 45', 'doctora', '123456', 2),
(4, 'Ana', 'García', '11111111', 'ana.garcia@email.com', '987654323', 'Jr. Las Flores 321', 'cliente1', '123456', 3),
(5, 'Sofia', 'Recepcionista', '33333333', 'sofia@garritas.com', '987654325', 'Calle Recepción 654', 'recepcion', '123456', 4),
(6, 'Dr. Carlos', 'Ramirez', '34567890', 'carlos.ramirez@garritas.com', '987001133', 'Av. Veterinaria 789', 'drcarlos', '123456', 2),
(7, 'Dra. Laura', 'Martinez', '45678901', 'laura.martinez@garritas.com', '987001144', 'Calle Mascotas 456', 'dralaura', '123456', 2),
(8, 'Dr. Roberto', 'Sanchez', '56789012', 'roberto.sanchez@garritas.com', '987001155', 'Jr. Animales 234', 'drroberto', '123456', 2),
(9, 'Dra. Patricia', 'Torres', '67890123', 'patricia.torres@garritas.com', '987001166', 'Av. Salud Animal 567', 'drapatricia', '123456', 2),
(10, 'Dr. Miguel', 'Fernandez', '78901234', 'miguel.fernandez@garritas.com', '987001177', 'Calle Veterinaria 890', 'drmiguel', '123456', 2);

-- Servicios veterinarios disponibles
INSERT INTO servicio (id_servicio, nombre, descripcion, precio) VALUES
(1, 'Consulta general', 'Revisión médica general', 50),
(2, 'Vacunación', 'Aplicación de vacunas', 35),
(3, 'Desparasitación', 'Tratamiento antiparasitario', 40),
(4, 'Cirugía menor', 'Procedimientos quirúrgicos menores', 200),
(5, 'Radiografía', 'Estudio radiológico', 80),
(6, 'Baño y corte', 'Higiene y corte de pelo', 30),
(7, 'Consulta especializada', 'Consulta con especialista', 75),
(8, 'Análisis de laboratorio', 'Exámenes de laboratorio', 60);

-- Mascotas registradas (según estructura de base de datos real)
INSERT INTO mascota (id_mascota, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario, edad) VALUES
(1, 'Firulais', 'Canino', 'Criollo', 'Macho', '2021-03-10', '12.50', 4, NULL),
(2, 'Michi', 'Felino', 'Siames', 'Hembra', '2022-05-20', '4.30', 4, NULL),
(3, 'Luna', 'Felino', 'Persa', 'Hembra', '2024-06-01', '3.20', 1, NULL),
(4, 'Max', 'Canino', 'Golden Retriever', 'Macho', '2020-05-15', '30.50', 4, NULL),
(5, 'Rocky', 'Canino', 'Bulldog', 'Macho', '2019-08-10', '25.00', 4, NULL),
(6, 'Nala', 'Felino', 'Angora', 'Hembra', '2021-07-14', '5.10', 4, NULL);

-- Citas programadas (según estructura real)
INSERT INTO cita (id_cita, fecha, hora, estado, id_mascota, id_servicio, id_veterinario, mascota_id, observaciones, servicio_id) VALUES
(1, '2025-10-15 00:00:00.000000', '09:30:00', 'Pendiente', 1, 1, 2, NULL, NULL, NULL),
(2, '2025-10-15 00:00:00.000000', '11:00:00', 'Pendiente', 2, 2, 3, NULL, NULL, NULL),
(3, '2025-10-16 00:00:00.000000', '10:30:00', 'Pendiente', 3, 1, 6, NULL, NULL, NULL),
(4, '2025-10-16 00:00:00.000000', '15:00:00', 'Pendiente', 4, 3, 7, NULL, NULL, NULL),
(5, '2025-10-17 00:00:00.000000', '08:30:00', 'Pendiente', 5, 4, 8, NULL, NULL, NULL),
(6, '2025-10-17 00:00:00.000000', '16:00:00', 'Pendiente', 6, 6, 9, NULL, NULL, NULL),
(7, '2025-11-15 10:00:00.000000', '10:00:00', 'Pendiente', 1, 1, 6, NULL, 'Vacunación anual', NULL),
(8, '2025-11-16 14:30:00.000000', '14:30:00', 'Pendiente', 2, 2, 6, NULL, 'Revisión dental', NULL),
(9, '2025-11-17 09:00:00.000000', '09:00:00', 'Pendiente', 3, 3, 7, NULL, 'Cirugía programada', NULL),
(10, '2025-11-18 11:00:00.000000', '11:00:00', 'Pendiente', 4, 1, 7, NULL, 'Control post-operatorio', NULL),
(11, '2025-11-19 15:00:00.000000', '15:00:00', 'Pendiente', 5, 4, 8, NULL, 'Emergencia', NULL),
(12, '2025-11-20 10:30:00.000000', '10:30:00', 'Pendiente', 1, 2, 8, NULL, 'Consulta general', NULL),
(13, '2025-11-21 13:00:00.000000', '13:00:00', 'Pendiente', 2, 1, 9, NULL, 'Vacunación antirrábica', NULL),
(14, '2025-11-22 16:00:00.000000', '16:00:00', 'Pendiente', 3, 6, 9, NULL, 'Baño y peluquería', NULL),
(15, '2025-11-23 09:30:00.000000', '09:30:00', 'Pendiente', 4, 3, 10, NULL, 'Ecografía', NULL),
(16, '2025-11-24 14:00:00.000000', '14:00:00', 'Pendiente', 5, 1, 10, NULL, 'Vacunación múltiple', NULL),
(17, '2025-10-05 10:00:00.000000', '10:00:00', 'Atendida', 1, 1, 2, NULL, 'Consulta general - Todo normal', NULL),
(18, '2025-10-12 11:30:00.000000', '11:30:00', 'Atendida', 2, 2, 2, NULL, 'Limpieza dental exitosa', NULL),
(19, '2025-10-08 09:00:00.000000', '09:00:00', 'Atendida', 3, 1, 3, NULL, 'Vacunación aplicada', NULL),
(20, '2025-10-15 15:00:00.000000', '15:00:00', 'Atendida', 4, 4, 3, NULL, 'Emergencia atendida a tiempo', NULL),
(21, '2025-10-10 14:00:00.000000', '14:00:00', 'Atendida', 5, 2, 6, NULL, 'Consulta de rutina', NULL),
(22, '2025-10-20 10:30:00.000000', '10:30:00', 'Atendida', 1, 3, 6, NULL, 'Cirugía menor - Recuperación exitosa', NULL),
(23, '2025-09-05 10:00:00.000000', '10:00:00', 'Atendida', 2, 1, 7, NULL, 'Control anual', NULL),
(24, '2025-09-12 13:00:00.000000', '13:00:00', 'Atendida', 3, 2, 8, NULL, 'Desparasitación', NULL),
(25, '2025-09-18 11:00:00.000000', '11:00:00', 'Atendida', 4, 1, 9, NULL, 'Vacunación', NULL),
(26, '2025-09-25 16:00:00.000000', '16:00:00', 'Atendida', 5, 3, 10, NULL, 'Radiografía - Sin problemas', NULL),
(27, '2025-10-25 14:00:00.000000', '14:00:00', 'Cancelada', 6, 1, 2, NULL, 'Cliente canceló con anticipación', NULL),
(28, '2025-10-28 09:00:00.000000', '09:00:00', 'Cancelada', 1, 2, 3, NULL, 'Mascota enferma, reprogramar', NULL);

-- Productos veterinarios (estructura completa con categoría, proveedor y vencimiento)
INSERT INTO producto (id_producto, nombre, descripcion, categoria, precio, stock, proveedor, fecha_vencimiento, activo) VALUES
(1, 'Alimento Premium 1kg', 'Alimento balanceado para perros', 'ALIMENTO', 25.5, 50, 'PetFood Distributors', '2026-12-31', 1),
(2, 'Shampoo Antipulgas 250ml', 'Shampoo para control de parásitos', 'HIGIENE', 15.99, 30, 'VetSupply SA', '2027-06-30', 1),
(3, 'Collar Antiparasitario', 'Collar de protección por 6 meses', 'MEDICAMENTO', 35, 25, 'MedVet Corp', '2026-08-15', 1),
(4, 'Juguete Masticable', 'Juguete resistente para perros', 'JUGUETE', 12.5, 40, 'Pet Toys Inc', NULL, 1),
(5, 'Arena para Gatos 5kg', 'Arena sanitaria premium', 'HIGIENE', 18, 35, 'CatCare Products', NULL, 1),
(6, 'Vitaminas Pet 60 tab', 'Suplemento vitamínico', 'MEDICAMENTO', 45, 20, 'VitaPet Labs', '2027-03-20', 1),
(7, 'Correa Ajustable', 'Correa resistente ajustable', 'ACCESORIO', 22, 15, 'Pet Accessories', NULL, 1),
(8, 'Cama Pet Mediana', 'Cama cómoda para mascotas', 'ACCESORIO', 65, 10, 'Comfort Pets', NULL, 1);

-- Historial clínico de las mascotas
INSERT INTO historial_medico (id_historial, id_cita, fecha_registro, fecha_consulta, tipo_consulta, motivo_consulta, diagnostico, tratamiento, medicamentos, observaciones, estado, costo, archivo, nombre_archivo, tipo_archivo, tamanio_archivo) VALUES
(1, 17, '2025-10-05 10:30:00', '2025-10-05 10:00:00', 'CONSULTA', 'Consulta general de rutina', 'Mascota en buen estado general de salud', 'Continuar con dieta actual y ejercicio regular', 'Vitaminas múltiples (1 tableta diaria)', 'Todo normal. Próxima consulta en 6 meses', 'COMPLETADO', '50.00', NULL, NULL, NULL, NULL),
(2, 18, '2025-10-12 11:45:00', '2025-10-12 11:30:00', 'CONSULTA', 'Limpieza dental programada', 'Presencia de sarro leve en molares', 'Limpieza dental realizada. Cepillado diario recomendado', 'Pasta dental canina', 'Procedimiento exitoso sin complicaciones', 'COMPLETADO', '75.00', NULL, NULL, NULL, NULL),
(3, 19, '2025-10-08 09:15:00', '2025-10-08 09:00:00', 'VACUNACION', 'Vacunación antirrábica anual', 'Aplicación de vacuna antirrábica', 'Observación 30 minutos post-vacuna. Próxima dosis en 1 año', 'Vacuna antirrábica', 'Sin reacciones adversas', 'COMPLETADO', '35.00', NULL, NULL, NULL, NULL),
(4, 20, '2025-10-15 15:30:00', '2025-10-15 15:00:00', 'EMERGENCIA', 'Vómitos y diarrea desde la madrugada', 'Gastroenteritis aguda', 'Hidratación IV, dieta blanda 3 días, reposo', 'Metronidazol 250mg (cada 12h por 5 días)', 'Emergencia atendida a tiempo. Control en 48 horas', 'COMPLETADO', '120.00', NULL, NULL, NULL, NULL),
(5, 21, '2025-10-10 14:15:00', '2025-10-10 14:00:00', 'CONSULTA', 'Revisión de rutina', 'Estado general bueno', 'Continuar cuidados normales', NULL, 'Todo en orden', 'COMPLETADO', '50.00', NULL, NULL, NULL, NULL),
(6, 22, '2025-10-20 11:00:00', '2025-10-20 10:30:00', 'CIRUGIA', 'Extirpación de quiste pequeño', 'Quiste benigno en pata delantera', 'Cirugía menor exitosa. Antibiótico 7 días, reposo 10 días', 'Amoxicilina 500mg (cada 8h por 7 días), Analgésico', 'Procedimiento exitoso. Retiro de puntos en 10 días', 'COMPLETADO', '200.00', NULL, NULL, NULL, NULL),
(7, 23, '2025-09-05 10:20:00', '2025-09-05 10:00:00', 'CONTROL', 'Control anual de salud', 'Estado general óptimo', 'Continuar con cuidados actuales', 'Multivitamínico', 'Próximo control en 1 año', 'COMPLETADO', '50.00', NULL, NULL, NULL, NULL),
(8, 24, '2025-09-12 13:15:00', '2025-09-12 13:00:00', 'CONSULTA', 'Desparasitación programada', 'Desparasitación interna aplicada', 'Repetir en 3 meses', 'Antiparasitario interno (dosis única)', 'Procedimiento completado sin problemas', 'COMPLETADO', '40.00', NULL, NULL, NULL, NULL),
(9, 25, '2025-09-18 11:10:00', '2025-09-18 11:00:00', 'VACUNACION', 'Vacuna múltiple', 'Aplicación de vacuna séxtuple', 'Observación 30 minutos. Próxima dosis según calendario', 'Vacuna séxtuple', 'Sin reacciones adversas', 'COMPLETADO', '35.00', NULL, NULL, NULL, NULL),
(10, 26, '2025-09-25 16:20:00', '2025-09-25 16:00:00', 'CONSULTA', 'Cojera en pata trasera', 'Radiografía sin fracturas. Posible esguince leve', 'Reposo 7 días, antiinflamatorio', 'Meloxicam 5mg (cada 24h por 5 días)', 'Radiografía - Sin problemas óseos', 'COMPLETADO', '80.00', NULL, NULL, NULL, NULL);

-- Reportes del sistema
INSERT INTO reporte (id_reporte, titulo, descripcion, fecha_generacion, id_usuario, fecha, mascota_id, usuario_id, contenido, id_usuario_generador, tipo_reporte) VALUES
(1, 'Reporte Mensual Octubre', 'Resumen de actividades del mes', '2025-10-01 10:00:00', 1, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'Reporte de Citas', 'Estadísticas de citas programadas', '2025-10-05 12:00:00', 1, NULL, NULL, NULL, NULL, NULL, NULL),
(3, 'Reporte de Ingresos', 'Análisis financiero mensual', '2025-10-10 09:00:00', 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO movimiento_inventario (id_movimiento, id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(1, 1, 'Entrada', 50, 45, 95, '2025-10-01 10:00:00.000000', 'Compra inicial a proveedor', 'admin'),
(2, 1, 'Salida', 10, 95, 85, '2025-10-05 14:30:00.000000', 'Venta a cliente', 'recepcion'),
(3, 1, 'Salida', 5, 85, 80, '2025-10-12 11:00:00.000000', 'Venta a cliente', 'recepcion'),
(4, 1, 'Entrada', 30, 80, 110, '2025-10-20 09:00:00.000000', 'Reposición de stock', 'admin'),
(5, 1, 'Salida', 15, 110, 95, '2025-11-01 16:00:00.000000', 'Venta mayorista', 'recepcion'),
(6, 2, 'Entrada', 40, 30, 70, '2025-09-15 10:00:00.000000', 'Compra a proveedor', 'admin'),
(7, 2, 'Salida', 8, 70, 62, '2025-09-20 15:00:00.000000', 'Venta a cliente', 'recepcion'),
(8, 2, 'Salida', 12, 62, 50, '2025-10-10 12:00:00.000000', 'Venta a cliente', 'recepcion'),
(9, 2, 'Entrada', 25, 50, 75, '2025-10-25 10:30:00.000000', 'Reposición de stock', 'admin'),
(10, 2, 'Salida', 10, 75, 65, '2025-11-05 14:00:00.000000', 'Venta a cliente', 'recepcion'),
(11, 3, 'Entrada', 100, 70, 170, '2025-09-10 09:00:00.000000', 'Compra inicial', 'admin'),
(12, 3, 'Salida', 20, 170, 150, '2025-09-18 13:00:00.000000', 'Venta a cliente', 'recepcion'),
(13, 3, 'Salida', 15, 150, 135, '2025-10-02 11:30:00.000000', 'Venta a cliente', 'recepcion'),
(14, 3, 'Salida', 10, 135, 125, '2025-10-15 16:00:00.000000', 'Venta a cliente', 'recepcion'),
(15, 3, 'Entrada', 50, 125, 175, '2025-10-28 10:00:00.000000', 'Reposición de stock', 'admin'),
(16, 4, 'Entrada', 80, 55, 135, '2025-09-05 10:00:00.000000', 'Compra a proveedor', 'admin'),
(17, 4, 'Salida', 25, 135, 110, '2025-09-15 14:00:00.000000', 'Venta a cliente', 'recepcion'),
(18, 4, 'Salida', 20, 110, 90, '2025-10-01 12:00:00.000000', 'Venta a cliente', 'recepcion'),
(19, 4, 'Entrada', 40, 90, 130, '2025-10-18 09:30:00.000000', 'Reposición de stock', 'admin'),
(20, 4, 'Salida', 15, 130, 115, '2025-11-03 15:00:00.000000', 'Venta a cliente', 'recepcion'),
(21, 5, 'Entrada', 60, 15, 75, '2025-09-20 10:00:00.000000', 'Compra a proveedor', 'admin'),
(22, 5, 'Salida', 5, 75, 70, '2025-09-25 13:00:00.000000', 'Uso en consulta veterinaria', 'veterinario'),
(23, 5, 'Salida', 8, 70, 62, '2025-10-08 11:00:00.000000', 'Venta a cliente', 'recepcion'),
(24, 5, 'Salida', 7, 62, 55, '2025-10-22 14:30:00.000000', 'Venta a cliente', 'recepcion'),
(25, 5, 'Entrada', 30, 55, 85, '2025-11-02 10:00:00.000000', 'Reposición de stock', 'admin');

-- ========================================
-- NOTAS PARA DESARROLLO:
-- 1. Todas las contraseñas son: "password123" (hasheadas con BCrypt)
-- 2. Datos sincronizados con la estructura real de MySQL
-- 3. Los IDs están definidos explícitamente para testing consistente
-- 4. Estados de cita: 'Pendiente', 'Atendida', 'Cancelada'
-- 5. Roles de usuario: 'ADMIN', 'VETERINARIO', 'CLIENTE'
-- 6. Sexo de mascotas: 'Macho', 'Hembra'
-- 7. Compatible con H2 (tests) y MySQL (producción)
-- ========================================

-- FOR DEV ONLY: Fuerza todas las contraseñas a un valor conocido
-- Ejecutar solo en entorno de desarrollo (H2). No usar en producción.
UPDATE usuario SET contrasena = '123456';

-- Configuración inicial (desarrollo)
INSERT INTO configuracion (id, clave, valor) VALUES
(1, 'site.name', 'Garritas Veterinaria'),
(2, 'site.maintenance', 'false'),
(3, 'site.itemsPerPage', '20');

