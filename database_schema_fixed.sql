-- ====================================
-- ESQUEMA DE BASE DE DATOS CORREGIDO
-- 100% Alineado con la estructura MySQL real
-- ====================================

-- Tabla rol (roles de usuario)
DROP TABLE IF EXISTS `rol`;
CREATE TABLE `rol` (
  `id_rol` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Roles de usuario (Administrador, Veterinario, Cliente, etc.)';

-- Tabla usuario (usuarios del sistema)
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `id_usuario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dni` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `usuario` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_rol` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`),
  UNIQUE KEY `usuario` (`usuario`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Usuarios del sistema (Clientes, Veterinarios, Administradores, etc.)';

-- Tabla mascota (mascotas registradas)
DROP TABLE IF EXISTS `mascota`;
CREATE TABLE `mascota` (
  `id_mascota` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `especie` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `raza` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sexo` enum('Macho','Hembra') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `peso` decimal(5,2) DEFAULT NULL,
  `id_usuario` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id_mascota`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `mascota_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Mascotas registradas por los clientes.';

-- Tabla servicio (servicios veterinarios)
DROP TABLE IF EXISTS `servicio`;
CREATE TABLE `servicio` (
  `id_servicio` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio` double DEFAULT NULL,
  PRIMARY KEY (`id_servicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Servicios veterinarios (consulta, baño, vacunación, etc.)';

-- Tabla cita (citas registradas)
DROP TABLE IF EXISTS `cita`;
CREATE TABLE `cita` (
  `id_cita` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `hora` time NOT NULL,
  `estado` enum('Pendiente','Atendida','Cancelada') COLLATE utf8mb4_unicode_ci DEFAULT 'Pendiente',
  `id_mascota` int(10) UNSIGNED NOT NULL,
  `id_servicio` int(10) UNSIGNED NOT NULL,
  `id_veterinario` int(10) UNSIGNED NOT NULL,
  `mascota_id` bigint(20) DEFAULT NULL,
  `observaciones` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `servicio_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  KEY `id_mascota` (`id_mascota`),
  KEY `id_servicio` (`id_servicio`),
  KEY `id_veterinario` (`id_veterinario`),
  CONSTRAINT `cita_ibfk_1` FOREIGN KEY (`id_mascota`) REFERENCES `mascota` (`id_mascota`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cita_ibfk_2` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id_servicio`) ON UPDATE CASCADE,
  CONSTRAINT `cita_ibfk_3` FOREIGN KEY (`id_veterinario`) REFERENCES `usuario` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Citas registradas para las mascotas.';

-- Tabla historialclinico (historial médico)
DROP TABLE IF EXISTS `historialclinico`;
CREATE TABLE `historialclinico` (
  `id_historial` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `diagnostico` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tratamiento` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_mascota` int(10) UNSIGNED NOT NULL,
  `mascota_id` bigint(20) DEFAULT NULL,
  `notas` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_historial`),
  KEY `id_mascota` (`id_mascota`),
  CONSTRAINT `historialclinico_ibfk_1` FOREIGN KEY (`id_mascota`) REFERENCES `mascota` (`id_mascota`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Historial médico de cada mascota.';

-- Tabla producto (productos veterinarios)
DROP TABLE IF EXISTS `producto`;
CREATE TABLE `producto` (
  `id_producto` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `stock` int(10) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Productos veterinarios y alimentos.';

-- Tabla reporte (reportes del sistema)
DROP TABLE IF EXISTS `reporte`;
CREATE TABLE `reporte` (
  `id_reporte` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_generacion` datetime DEFAULT current_timestamp(),
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `mascota_id` bigint(20) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_reporte`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `reporte_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Reportes generados por el sistema (ventas, citas, etc.)';

-- ====================================
-- DATOS INICIALES - 100% COHERENTES CON LA ESTRUCTURA REAL
-- ====================================

-- Roles del sistema
INSERT INTO `rol` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'VETERINARIO', 'Personal veterinario'),
(3, 'CLIENTE', 'Cliente / Propietario de mascotas');

-- Usuarios del sistema (exactamente como en tu base de datos)
INSERT INTO `usuario` (`id_usuario`, `nombre`, `apellido`, `dni`, `correo`, `telefono`, `direccion`, `usuario`, `contrasena`, `id_rol`) VALUES
(1, 'Admin', 'Sistema', '00000000', 'admin@local', '999999999', 'Calle Principal 1', 'admin', 'admin', 1),
(2, 'Juan', 'Perez', '12345678', 'juan.perez@example.com', '987654321', 'Av. Siempre Viva 123', 'jperez', 'secret', 3),
(3, 'Dra. Maria', 'Lopez', '23456789', 'maria.lopez@example.com', '987001122', 'Calle Salud 45', 'mlopez', 'vetpass', 2);

-- Servicios veterinarios (como en tu base de datos)
INSERT INTO `servicio` (`id_servicio`, `nombre`, `descripcion`, `precio`) VALUES
(1, 'Consulta general', 'Revisión y consejo', 15),
(2, 'Vacunación', 'Vacuna anual', 25),
(3, 'Baño y corte', 'Higiene y corte de pelo', 30);

-- Mascotas registradas (con estructura completa)
INSERT INTO `mascota` (`id_mascota`, `nombre`, `especie`, `raza`, `sexo`, `fecha_nacimiento`, `peso`, `id_usuario`) VALUES
(1, 'Firulais', 'Canino', 'Criollo', 'Macho', '2021-03-10', 12.50, 2),
(2, 'Michi', 'Felino', 'Siames', 'Hembra', '2022-05-20', 4.30, 2),
(3, 'Luna', 'Felino', 'Siam', 'Hembra', '2024-06-01', 3.20, 1);

-- Citas programadas (estructura exacta de tu DB)
INSERT INTO `cita` (`id_cita`, `fecha`, `hora`, `estado`, `id_mascota`, `id_servicio`, `id_veterinario`, `observaciones`) VALUES
(1, '2025-10-10 00:00:00', '09:30:00', 'Pendiente', 1, 1, 3, NULL),
(2, '2025-10-12 00:00:00', '11:00:00', 'Pendiente', 2, 2, 3, NULL);

-- Productos veterinarios (como en tu DB)
INSERT INTO `producto` (`id_producto`, `nombre`, `descripcion`, `precio`, `stock`) VALUES
(1, 'Alimento Premium 1kg', 'Alimento balanceado para perros', 12.5, 50),
(2, 'Shampoo Antipulgas 250ml', 'Shampoo para limpieza y control de parásitos', 7.99, 30);

-- Historial clínico (como en tu DB)
INSERT INTO `historialclinico` (`id_historial`, `fecha`, `diagnostico`, `tratamiento`, `observaciones`, `id_mascota`) VALUES
(1, '2024-12-01 00:00:00', 'Otitis', 'Limpieza + antibiótico', 'Mejoró a los 7 días', 1),
(2, '2025-01-15 00:00:00', 'Vacunación anual', 'Dose X', 'Vacunado sin incidentes', 2);

-- Reportes del sistema (como en tu DB)
INSERT INTO `reporte` (`id_reporte`, `titulo`, `descripcion`, `fecha_generacion`, `id_usuario`) VALUES
(1, 'Reporte Ventas Octubre', 'Ventas y actividad del mes', '2025-10-01 10:00:00', 1),
(2, 'Reporte Citas Octubre', 'Resumen de citas programadas', '2025-10-02 12:00:00', 1);