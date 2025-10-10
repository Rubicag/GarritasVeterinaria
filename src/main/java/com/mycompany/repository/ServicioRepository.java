/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT s FROM Servicio s WHERE s.precio BETWEEN :min AND :max")
    List<Servicio> findByPrecioBetween(@Param("min") Double minPrecio, @Param("max") Double maxPrecio);
    
    @Query("SELECT s FROM Servicio s ORDER BY s.precio ASC")
    List<Servicio> findAllOrderByPrecioAsc();
}
