-- Script para agregar nuevas columnas a la tabla producto
-- Ejecutar en MySQL Workbench o CLI

USE garritas_veterinaria;

-- Agregar columna categoria
ALTER TABLE `producto` 
ADD COLUMN `categoria` VARCHAR(50) DEFAULT 'OTRO' COMMENT 'Categoría del producto' AFTER `descripcion`;

-- Agregar columna proveedor
ALTER TABLE `producto` 
ADD COLUMN `proveedor` VARCHAR(100) DEFAULT NULL COMMENT 'Proveedor del producto' AFTER `stock`;

-- Agregar columna fecha_vencimiento
ALTER TABLE `producto` 
ADD COLUMN `fecha_vencimiento` DATE DEFAULT NULL COMMENT 'Fecha de vencimiento del producto' AFTER `proveedor`;

-- Agregar columna activo (estado del producto)
ALTER TABLE `producto` 
ADD COLUMN `activo` TINYINT(1) DEFAULT 1 COMMENT 'Estado activo/inactivo' AFTER `fecha_vencimiento`;

-- Actualizar productos existentes con valores de ejemplo
UPDATE `producto` SET 
    `categoria` = CASE 
        WHEN `nombre` LIKE '%Alimento%' THEN 'ALIMENTO'
        WHEN `nombre` LIKE '%Shampoo%' THEN 'HIGIENE'
        WHEN `nombre` LIKE '%Collar%' THEN 'ACCESORIOS'
        WHEN `nombre` LIKE '%Juguete%' THEN 'JUGUETES'
        WHEN `nombre` LIKE '%Arena%' THEN 'HIGIENE'
        WHEN `nombre` LIKE '%Vitaminas%' THEN 'MEDICAMENTO'
        WHEN `nombre` LIKE '%Correa%' THEN 'ACCESORIOS'
        WHEN `nombre` LIKE '%Cama%' THEN 'ACCESORIOS'
        ELSE 'OTRO'
    END,
    `proveedor` = CASE 
        WHEN `nombre` LIKE '%Alimento%' THEN 'Distribuidora Pet Food S.A.'
        WHEN `nombre` LIKE '%Shampoo%' THEN 'Laboratorio Vet Care'
        WHEN `nombre` LIKE '%Collar%' THEN 'Importadora Global Pet'
        WHEN `nombre` LIKE '%Juguete%' THEN 'Fábrica Toys & Pets'
        WHEN `nombre` LIKE '%Arena%' THEN 'Distribuidora Pet Food S.A.'
        WHEN `nombre` LIKE '%Vitaminas%' THEN 'Laboratorio Vet Care'
        WHEN `nombre` LIKE '%Correa%' THEN 'Importadora Global Pet'
        WHEN `nombre` LIKE '%Cama%' THEN 'Fábrica Toys & Pets'
        ELSE 'Proveedor General'
    END,
    `fecha_vencimiento` = CASE 
        WHEN `nombre` LIKE '%Alimento%' THEN DATE_ADD(CURDATE(), INTERVAL 6 MONTH)
        WHEN `nombre` LIKE '%Shampoo%' THEN DATE_ADD(CURDATE(), INTERVAL 2 YEAR)
        WHEN `nombre` LIKE '%Vitaminas%' THEN DATE_ADD(CURDATE(), INTERVAL 1 YEAR)
        ELSE NULL
    END,
    `activo` = 1
WHERE `id_producto` BETWEEN 1 AND 8;

-- Verificar los cambios
SELECT * FROM `producto`;
