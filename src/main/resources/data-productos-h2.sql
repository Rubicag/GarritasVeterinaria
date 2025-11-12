-- ========================================
-- DATOS DE PRODUCTOS PARA H2 (Desarrollo)
-- Incluye las columnas nuevas: categoria, proveedor, fecha_vencimiento, activo
-- ========================================

-- Limpiar productos existentes
DELETE FROM producto;

-- Insertar productos con todas las columnas
INSERT INTO producto (id_producto, nombre, descripcion, categoria, precio, stock, proveedor, fecha_vencimiento, activo) VALUES
    (1, 'Alimento Premium 1kg', 'Alimento balanceado para perros adultos', 'ALIMENTO', 12.50, 50, 'Distribuidora Pet Food S.A.', '2026-05-06', true),
    (2, 'Shampoo Antipulgas 250ml', 'Shampoo para limpieza y control de parásitos externos', 'HIGIENE', 7.99, 30, 'Laboratorio Vet Care', '2027-11-06', true),
    (3, 'Collar Antipulgas', 'Collar preventivo de pulgas y garrapatas', 'ACCESORIOS', 15.00, 25, 'Importadora Global Pet', '2026-11-06', true),
    (4, 'Pelota Caucho Grande', 'Pelota resistente para perros grandes', 'JUGUETES', 5.50, 40, 'Fábrica Toys & Pets', '2026-11-06', true),
    (5, 'Vitaminas Pet 60 tab', 'Suplemento vitamínico para perros y gatos', 'MEDICAMENTO', 18.00, 35, 'Laboratorio Vet Care', '2026-11-06', true),
    (6, 'Arena Sanitaria 5kg', 'Arena aglomerante para gatos', 'HIGIENE', 9.99, 45, 'Distribuidora Pet Food S.A.', '2027-11-06', true),
    (7, 'Correa Extensible 3m', 'Correa retráctil para paseo', 'ACCESORIOS', 12.00, 20, 'Importadora Global Pet', '2026-11-06', true),
    (8, 'Snack Dental 200g', 'Premios dentales para perros', 'ALIMENTO', 6.50, 60, 'Distribuidora Pet Food S.A.', '2026-05-06', true);
