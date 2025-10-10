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
    (3, 'CLIENTE', 'Cliente / Propietario de mascotas');

-- Usuarios del sistema (passwords están hasheadas con BCrypt)
INSERT INTO usuario (id_usuario, nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol) VALUES
    (1, 'Admin', 'Sistema', '00000000', 'admin@local', '999999999', 'Calle Principal 1', 'admin', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 1),
    (2, 'Juan', 'Perez', '12345678', 'juan.perez@example.com', '987654321', 'Av. Siempre Viva 123', 'jperez', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 3),
    (3, 'Dra. Maria', 'Lopez', '23456789', 'maria.lopez@example.com', '987001122', 'Calle Salud 45', 'mlopez', '$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa', 2);

-- Servicios veterinarios disponibles
INSERT INTO servicio (id_servicio, nombre, descripcion, precio) VALUES
    (1, 'Consulta General', 'Revisión médica completa y diagnóstico', 25.00),
    (2, 'Vacunación', 'Aplicación de vacunas según calendario veterinario', 15.00),
    (3, 'Desparasitación', 'Tratamiento antiparasitario interno y externo', 12.00),
    (4, 'Cirugía Menor', 'Procedimientos quirúrgicos ambulatorios', 80.00),
    (5, 'Baño y Peluquería', 'Servicio completo de higiene y estética', 18.00),
    (6, 'Radiografía', 'Diagnóstico por imágenes', 35.00);

-- Mascotas registradas (según estructura de base de datos real)
INSERT INTO mascota (id_mascota, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario) VALUES
    (1, 'Firulais', 'Canino', 'Criollo', 'Macho', '2021-03-10', 12.50, 2),
    (2, 'Michi', 'Felino', 'Siames', 'Hembra', '2022-05-20', 4.30, 2),
    (3, 'Luna', 'Felino', 'Siam', 'Hembra', '2024-06-01', 3.20, 1);

-- Citas programadas (según estructura real)
INSERT INTO cita (id_cita, fecha, hora, estado, id_mascota, id_servicio, id_veterinario, observaciones) VALUES
    (1, '2025-10-10 00:00:00', '09:30:00', 'Pendiente', 1, 1, 3, 'Consulta general programada'),
    (2, '2025-10-12 00:00:00', '11:00:00', 'Pendiente', 2, 2, 3, 'Vacunación anual');

-- Productos veterinarios (según estructura real)
INSERT INTO producto (id_producto, nombre, descripcion, precio, stock) VALUES
    (1, 'Alimento Premium 1kg', 'Alimento balanceado para perros', 12.5, 50),
    (2, 'Shampoo Antipulgas 250ml', 'Shampoo para limpieza y control de parásitos', 7.99, 30);

-- Historial clínico de las mascotas
INSERT INTO historialclinico (id_historial, fecha, diagnostico, tratamiento, observaciones, id_mascota) VALUES
    (1, '2024-12-01 00:00:00', 'Otitis', 'Limpieza + antibiótico', 'Mejoró a los 7 días', 1),
    (2, '2025-01-15 00:00:00', 'Vacunación anual', 'Dose X', 'Vacunado sin incidentes', 2);

-- Reportes del sistema
INSERT INTO reporte (id_reporte, titulo, descripcion, fecha_generacion, id_usuario) VALUES
    (1, 'Reporte Ventas Octubre', 'Ventas y actividad del mes', '2025-10-01 10:00:00', 1),
    (2, 'Reporte Citas Octubre', 'Resumen de citas programadas', '2025-10-02 12:00:00', 1);

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

