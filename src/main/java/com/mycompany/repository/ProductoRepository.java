/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByStockLessThan(Integer stock);
    
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max")
    List<Producto> findByPrecioBetween(@Param("min") Double minPrecio, @Param("max") Double maxPrecio);
    
    @Query("SELECT p FROM Producto p WHERE p.stock > 0 ORDER BY p.nombre ASC")
    List<Producto> findDisponiblesOrderByNombre();
}
