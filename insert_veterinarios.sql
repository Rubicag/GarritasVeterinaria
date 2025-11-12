-- =====================================================
-- SCRIPT PARA AGREGAR MÁS VETERINARIOS A LA BASE DE DATOS
-- =====================================================
-- Ejecutar este script en phpMyAdmin o MySQL Workbench
-- =====================================================

-- Insertar 5 veterinarios adicionales (id_rol = 2 para Veterinario)
INSERT INTO `usuario` (`nombre`, `apellido`, `dni`, `correo`, `telefono`, `direccion`, `usuario`, `contrasena`, `id_rol`) VALUES
('Dr. Carlos', 'Ramirez', '34567890', 'carlos.ramirez@garritas.com', '987001133', 'Av. Veterinaria 789', 'drcarlos', '123456', 2),
('Dra. Laura', 'Martinez', '45678901', 'laura.martinez@garritas.com', '987001144', 'Calle Mascotas 456', 'dralaura', '123456', 2),
('Dr. Roberto', 'Sanchez', '56789012', 'roberto.sanchez@garritas.com', '987001155', 'Jr. Animales 234', 'drroberto', '123456', 2),
('Dra. Patricia', 'Torres', '67890123', 'patricia.torres@garritas.com', '987001166', 'Av. Salud Animal 567', 'drapatricia', '123456', 2),
('Dr. Miguel', 'Fernandez', '78901234', 'miguel.fernandez@garritas.com', '987001177', 'Calle Veterinaria 890', 'drmiguel', '123456', 2);

-- =====================================================
-- RESULTADO ESPERADO: 7 veterinarios en total
-- =====================================================
-- 1. Dr. Juan Perez (ya existe)
-- 2. Dra. Maria Lopez (ya existe)
-- 3. Dr. Carlos Ramirez (nuevo)
-- 4. Dra. Laura Martinez (nuevo)
-- 5. Dr. Roberto Sanchez (nuevo)
-- 6. Dra. Patricia Torres (nuevo)
-- 7. Dr. Miguel Fernandez (nuevo)
-- =====================================================

-- Verificar que se insertaron correctamente
SELECT * FROM usuario WHERE id_rol = 2 ORDER BY id_usuario;
