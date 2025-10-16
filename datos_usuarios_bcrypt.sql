-- ====================================
-- DATOS DE USUARIO CON CONTRASEÑAS BCRYPT
-- Sistema Veterinario Garritas
-- ====================================

USE garritas_veterinaria;

-- Limpiar datos existentes (en orden correcto por dependencias)
DELETE FROM historialclinico;
DELETE FROM cita;
DELETE FROM mascota;
DELETE FROM producto;
DELETE FROM servicio;
DELETE FROM reporte;
DELETE FROM usuario;
DELETE FROM rol;

-- Reiniciar AUTO_INCREMENT
ALTER TABLE historialclinico AUTO_INCREMENT = 1;
ALTER TABLE cita AUTO_INCREMENT = 1;
ALTER TABLE mascota AUTO_INCREMENT = 1;
ALTER TABLE producto AUTO_INCREMENT = 1;
ALTER TABLE servicio AUTO_INCREMENT = 1;
ALTER TABLE reporte AUTO_INCREMENT = 1;
ALTER TABLE usuario AUTO_INCREMENT = 1;
ALTER TABLE rol AUTO_INCREMENT = 1;

-- Insertar roles
INSERT INTO rol (id_rol, nombre, descripcion) VALUES
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'VETERINARIO', 'Personal veterinario'),
(3, 'CLIENTE', 'Cliente / Propietario de mascotas'),
(4, 'RECEPCIONISTA', 'Recepcionista de la clínica');

-- Insertar usuarios con contraseñas BCrypt
-- admin/admin -> $2a$10$N.dpzZfU4Kx4k0kT9jY9y.QM1V4kK9rJhGk8H7p5KQ0kA3A5K4a4K
-- 123456 -> $2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW
INSERT INTO usuario (id_usuario, nombre, apellido, dni, correo, telefono, direccion, usuario, contrasena, id_rol)
VALUES
  (1,'Admin','Sistema','00000000','admin@garritas.com','999999999','Calle Principal 1','admin','$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',1),
  (2,'Dr. Juan','Perez','12345678','juan.perez@garritas.com','987654321','Av. Siempre Viva 123','veterinario','$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',2),
  (3,'Dra. Maria','Lopez','23456789','maria.lopez@garritas.com','987001122','Calle Salud 45','doctora','$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',2),
  (4,'Ana','García','11111111','ana.garcia@email.com','987654323','Jr. Las Flores 321','cliente1','$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',3),
  (5,'Sofia','Recepcionista','33333333','sofia@garritas.com','987654325','Calle Recepción 654','recepcion','$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',4);

-- Insertar mascotas
INSERT INTO mascota (id_mascota, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_usuario)
VALUES
  (1,'Firulais','Canino','Criollo','Macho','2021-03-10',12.50,4),
  (2,'Michi','Felino','Siames','Hembra','2022-05-20',4.30,4),
  (3,'Luna','Felino','Persa','Hembra','2024-06-01',3.20,1),
  (4,'Max','Canino','Golden Retriever','Macho','2020-05-15',30.50,4),
  (5,'Rocky','Canino','Bulldog','Macho','2019-08-10',25.00,4),
  (6,'Nala','Felino','Angora','Hembra','2021-07-14',5.10,4);

-- Insertar servicios
INSERT INTO servicio (id_servicio, nombre, descripcion, precio) VALUES
  (1,'Consulta general','Revisión médica general',50.00),
  (2,'Vacunación','Aplicación de vacunas',35.00),
  (3,'Desparasitación','Tratamiento antiparasitario',40.00),
  (4,'Cirugía menor','Procedimientos quirúrgicos menores',200.00),
  (5,'Radiografía','Estudio radiológico',80.00),
  (6,'Baño y corte','Higiene y corte de pelo',30.00),
  (7,'Consulta especializada','Consulta con especialista',75.00),
  (8,'Análisis de laboratorio','Exámenes de laboratorio',60.00);

-- Insertar productos
INSERT INTO producto (id_producto, nombre, descripcion, precio, stock) VALUES
  (1,'Alimento Premium 1kg','Alimento balanceado para perros',25.50,50),
  (2,'Shampoo Antipulgas 250ml','Shampoo para control de parásitos',15.99,30),
  (3,'Collar Antiparasitario','Collar de protección por 6 meses',35.00,25),
  (4,'Juguete Masticable','Juguete resistente para perros',12.50,40),
  (5,'Arena para Gatos 5kg','Arena sanitaria premium',18.00,35),
  (6,'Vitaminas Pet 60 tab','Suplemento vitamínico',45.00,20),
  (7,'Correa Ajustable','Correa resistente ajustable',22.00,15),
  (8,'Cama Pet Mediana','Cama cómoda para mascotas',65.00,10);

-- Insertar citas (usando veterinario id=2)
INSERT INTO cita (id_cita, fecha, hora, estado, id_mascota, id_servicio, id_veterinario) VALUES
  (1,'2025-10-15','09:30:00','Pendiente',1,1,2),
  (2,'2025-10-15','11:00:00','Pendiente',2,2,2),
  (3,'2025-10-16','10:30:00','Confirmada',3,1,3),
  (4,'2025-10-16','15:00:00','Pendiente',4,3,2),
  (5,'2025-10-17','08:30:00','Confirmada',5,4,3),
  (6,'2025-10-17','16:00:00','Pendiente',6,6,2);

-- Insertar historial clínico
INSERT INTO historialclinico (id_historial, fecha, diagnostico, tratamiento, observaciones, id_mascota) VALUES
  (1,'2024-12-01','Otitis externa','Limpieza + antibiótico tópico','Mejoró completamente a los 7 días',1),
  (2,'2025-01-15','Vacunación anual','Vacuna quíntuple + antirrábica','Vacunado sin incidentes',2),
  (3,'2025-03-10','Control de peso','Dieta balanceada','Peso ideal mantenido',3),
  (4,'2025-05-20','Desparasitación','Antiparasitario oral','Control en 30 días',4),
  (5,'2025-08-01','Revisión general','Estado general bueno','Sin observaciones',5);

-- Insertar reportes
INSERT INTO reporte (id_reporte, titulo, descripcion, fecha_generacion, id_usuario) VALUES
  (1,'Reporte Mensual Octubre','Resumen de actividades del mes','2025-10-01 10:00:00',1),
  (2,'Reporte de Citas','Estadísticas de citas programadas','2025-10-05 12:00:00',1),
  (3,'Reporte de Ingresos','Análisis financiero mensual','2025-10-10 09:00:00',1);

-- Verificar datos insertados
SELECT 'Datos insertados correctamente' AS status;
SELECT COUNT(*) as total_usuarios FROM usuario;
SELECT COUNT(*) as total_mascotas FROM mascota;
SELECT COUNT(*) as total_citas FROM cita;