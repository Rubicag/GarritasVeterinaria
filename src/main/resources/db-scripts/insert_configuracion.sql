    -- Inserciones iniciales para la tabla configuracion
-- Ejecutar sobre la base de datos garritas_veterinaria (MySQL)

INSERT INTO configuracion (id, clave, valor) VALUES
(1, 'site.name', 'Garritas Veterinaria')
ON DUPLICATE KEY UPDATE valor = VALUES(valor);

INSERT INTO configuracion (id, clave, valor) VALUES
(2, 'site.maintenance', 'false')
ON DUPLICATE KEY UPDATE valor = VALUES(valor);

INSERT INTO configuracion (id, clave, valor) VALUES
(3, 'site.itemsPerPage', '20')
ON DUPLICATE KEY UPDATE valor = VALUES(valor);
