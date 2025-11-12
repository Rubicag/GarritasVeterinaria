-- =====================================================
-- CREAR TABLA MOVIMIENTO_INVENTARIO
-- =====================================================

CREATE TABLE `movimiento_inventario` (
  `id_movimiento` BIGINT NOT NULL AUTO_INCREMENT,
  `id_producto` BIGINT NOT NULL,
  `tipo` ENUM('Entrada', 'Salida') NOT NULL,
  `cantidad` INT NOT NULL,
  `stock_anterior` INT NOT NULL,
  `stock_actual` INT NOT NULL,
  `fecha` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `motivo` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `usuario` VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_movimiento`),
  KEY `idx_producto` (`id_producto`),
  KEY `idx_fecha` (`fecha`),
  CONSTRAINT `fk_movimiento_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Registro de movimientos de inventario (entradas y salidas)';

-- =====================================================
-- INSERTAR DATOS DE EJEMPLO
-- =====================================================

-- Movimientos para producto 1 (Alimento Perro Adulto)
INSERT INTO movimiento_inventario (id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(1, 'Entrada', 50, 45, 95, '2025-10-01 10:00:00', 'Compra inicial a proveedor', 'admin'),
(1, 'Salida', 10, 95, 85, '2025-10-05 14:30:00', 'Venta a cliente', 'recepcion'),
(1, 'Salida', 5, 85, 80, '2025-10-12 11:00:00', 'Venta a cliente', 'recepcion'),
(1, 'Entrada', 30, 80, 110, '2025-10-20 09:00:00', 'Reposición de stock', 'admin'),
(1, 'Salida', 15, 110, 95, '2025-11-01 16:00:00', 'Venta mayorista', 'recepcion');

-- Movimientos para producto 2 (Alimento Gato Cachorro)
INSERT INTO movimiento_inventario (id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(2, 'Entrada', 40, 30, 70, '2025-09-15 10:00:00', 'Compra a proveedor', 'admin'),
(2, 'Salida', 8, 70, 62, '2025-09-20 15:00:00', 'Venta a cliente', 'recepcion'),
(2, 'Salida', 12, 62, 50, '2025-10-10 12:00:00', 'Venta a cliente', 'recepcion'),
(2, 'Entrada', 25, 50, 75, '2025-10-25 10:30:00', 'Reposición de stock', 'admin'),
(2, 'Salida', 10, 75, 65, '2025-11-05 14:00:00', 'Venta a cliente', 'recepcion');

-- Movimientos para producto 3 (Shampoo Antipulgas)
INSERT INTO movimiento_inventario (id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(3, 'Entrada', 100, 70, 170, '2025-09-10 09:00:00', 'Compra inicial', 'admin'),
(3, 'Salida', 20, 170, 150, '2025-09-18 13:00:00', 'Venta a cliente', 'recepcion'),
(3, 'Salida', 15, 150, 135, '2025-10-02 11:30:00', 'Venta a cliente', 'recepcion'),
(3, 'Salida', 10, 135, 125, '2025-10-15 16:00:00', 'Venta a cliente', 'recepcion'),
(3, 'Entrada', 50, 125, 175, '2025-10-28 10:00:00', 'Reposición de stock', 'admin');

-- Movimientos para producto 4 (Arena Sanitaria)
INSERT INTO movimiento_inventario (id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(4, 'Entrada', 80, 55, 135, '2025-09-05 10:00:00', 'Compra a proveedor', 'admin'),
(4, 'Salida', 25, 135, 110, '2025-09-15 14:00:00', 'Venta a cliente', 'recepcion'),
(4, 'Salida', 20, 110, 90, '2025-10-01 12:00:00', 'Venta a cliente', 'recepcion'),
(4, 'Entrada', 40, 90, 130, '2025-10-18 09:30:00', 'Reposición de stock', 'admin'),
(4, 'Salida', 15, 130, 115, '2025-11-03 15:00:00', 'Venta a cliente', 'recepcion');

-- Movimientos para producto 5 (Vitaminas Multiples)
INSERT INTO movimiento_inventario (id_producto, tipo, cantidad, stock_anterior, stock_actual, fecha, motivo, usuario) VALUES
(5, 'Entrada', 60, 15, 75, '2025-09-20 10:00:00', 'Compra a proveedor', 'admin'),
(5, 'Salida', 5, 75, 70, '2025-09-25 13:00:00', 'Uso en consulta veterinaria', 'veterinario'),
(5, 'Salida', 8, 70, 62, '2025-10-08 11:00:00', 'Venta a cliente', 'recepcion'),
(5, 'Salida', 7, 62, 55, '2025-10-22 14:30:00', 'Venta a cliente', 'recepcion'),
(5, 'Entrada', 30, 55, 85, '2025-11-02 10:00:00', 'Reposición de stock', 'admin');

-- =====================================================
-- VERIFICACIÓN
-- =====================================================

-- Ver todos los movimientos ordenados por fecha
SELECT 
    m.id_movimiento,
    p.nombre as producto,
    m.tipo,
    m.cantidad,
    m.stock_anterior,
    m.stock_actual,
    m.fecha,
    m.motivo,
    m.usuario
FROM movimiento_inventario m
INNER JOIN producto p ON m.id_producto = p.id_producto
ORDER BY m.fecha DESC;

-- Resumen de movimientos por producto
SELECT 
    p.nombre as producto,
    SUM(CASE WHEN m.tipo = 'Entrada' THEN m.cantidad ELSE 0 END) as total_entradas,
    SUM(CASE WHEN m.tipo = 'Salida' THEN m.cantidad ELSE 0 END) as total_salidas,
    COUNT(*) as total_movimientos
FROM movimiento_inventario m
INNER JOIN producto p ON m.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
ORDER BY total_movimientos DESC;
