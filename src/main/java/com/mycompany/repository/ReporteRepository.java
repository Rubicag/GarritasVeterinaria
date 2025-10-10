package com.mycompany.repository;

import com.mycompany.model.Reporte;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    
    // Buscar reportes por usuario generador
    List<Reporte> findByIdUsuarioGenerador(Long idUsuarioGenerador);
    
    // Buscar reportes por tipo
    List<Reporte> findByTipoReporte(String tipoReporte);
    
    // Buscar reportes entre fechas
    List<Reporte> findByFechaGeneracionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar reportes por contenido (contiene texto)
    List<Reporte> findByContenidoContainingIgnoreCase(String texto);
    
    // Buscar reportes recientes (últimos N días)
    @Query("SELECT r FROM Reporte r WHERE r.fechaGeneracion >= :fechaDesde ORDER BY r.fechaGeneracion DESC")
    List<Reporte> findReportesRecientes(@Param("fechaDesde") LocalDateTime fechaDesde);
    
    // Contar reportes por usuario generador
    @Query("SELECT COUNT(r) FROM Reporte r WHERE r.idUsuarioGenerador = :idUsuarioGenerador")
    Long countByIdUsuarioGenerador(@Param("idUsuarioGenerador") Long idUsuarioGenerador);
    
    // Buscar todos los reportes ordenados por fecha descendente
    @Query("SELECT r FROM Reporte r ORDER BY r.fechaGeneracion DESC")
    List<Reporte> findAllOrderByFechaDesc();
}
