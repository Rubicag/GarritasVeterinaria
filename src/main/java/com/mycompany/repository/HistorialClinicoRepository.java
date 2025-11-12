/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.HistorialClinico;
import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    
    // Búsquedas por mascota
    List<HistorialClinico> findByMascota(Mascota mascota);
    
    @Query("SELECT h FROM HistorialClinico h WHERE h.mascota = :mascota ORDER BY h.fecha DESC")
    List<HistorialClinico> findByMascotaOrderByFechaDesc(@Param("mascota") Mascota mascota);
    
    // Búsquedas por fecha
    List<HistorialClinico> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT h FROM HistorialClinico h ORDER BY h.fecha DESC")
    List<HistorialClinico> findAllByOrderByFechaDesc();
    
    // Búsquedas por tipo de consulta
    List<HistorialClinico> findByTipoConsulta(HistorialClinico.TipoConsulta tipoConsulta);
    
    // Búsquedas por estado
    List<HistorialClinico> findByEstado(HistorialClinico.EstadoConsulta estado);
    
    // Búsquedas por veterinario
    List<HistorialClinico> findByVeterinario(Usuario veterinario);
    
    // Búsquedas por notas
    @Query("SELECT h FROM HistorialClinico h WHERE h.notas LIKE %:texto%")
    List<HistorialClinico> findByNotasContaining(@Param("texto") String texto);
}
