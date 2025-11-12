package com.mycompany.repository;

import com.mycompany.model.MovimientoInventario;
import com.mycompany.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones CRUD de MovimientoInventario.
 * Proporciona métodos para consultar movimientos por fecha, producto y tipo.
 */
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    /**
     * Buscar movimientos por producto
     */
    List<MovimientoInventario> findByProductoOrderByFechaDesc(Producto producto);

    /**
     * Buscar movimientos por tipo
     */
    List<MovimientoInventario> findByTipoOrderByFechaDesc(MovimientoInventario.TipoMovimiento tipo);

    /**
     * Buscar movimientos en un rango de fechas
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.fecha BETWEEN :desde AND :hasta ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByFechaBetween(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    /**
     * Buscar movimientos por producto y rango de fechas
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto = :producto AND m.fecha BETWEEN :desde AND :hasta ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByProductoAndFechaBetween(
            @Param("producto") Producto producto,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    /**
     * Buscar todos los movimientos ordenados por fecha descendente
     */
    List<MovimientoInventario> findAllByOrderByFechaDesc();

    /**
     * Contar movimientos por tipo en un rango de fechas
     */
    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.tipo = :tipo AND m.fecha BETWEEN :desde AND :hasta")
    Long countByTipoAndFechaBetween(
            @Param("tipo") MovimientoInventario.TipoMovimiento tipo,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    /**
     * Obtener últimos N movimientos de un producto
     */
    List<MovimientoInventario> findTop10ByProductoOrderByFechaDesc(Producto producto);
}
