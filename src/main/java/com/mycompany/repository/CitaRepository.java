/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByMascota(Mascota mascota);
    List<Cita> findByServicio(Servicio servicio);
    List<Cita> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT c FROM Cita c WHERE c.fecha >= :fecha ORDER BY c.fecha ASC")
    List<Cita> findCitasFuturas(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT c FROM Cita c WHERE DATE(c.fecha) = DATE(:fecha)")
    List<Cita> findCitasPorDia(@Param("fecha") LocalDateTime fecha);
}
