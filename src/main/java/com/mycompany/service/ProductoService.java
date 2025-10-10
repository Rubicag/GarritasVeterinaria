package com.mycompany.service;

import com.mycompany.model.Producto;
import com.mycompany.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) { 
        this.productoRepository = productoRepository; 
    }

    public List<Producto> findAll() { 
        return productoRepository.findAll(); 
    }

    public List<Producto> listAll() { 
        return findAll(); 
    }

    public Optional<Producto> getById(Long id) { 
        return productoRepository.findById(id); 
    }

    public Producto create(Producto p) { 
        p.setId(null); 
        return productoRepository.save(p); 
    }

    public Producto update(Long id, Producto p) {
        Optional<Producto> ex = productoRepository.findById(id);
        if (ex.isEmpty()) return null;
        Producto cur = ex.get();
        cur.setNombre(p.getNombre());
        cur.setDescripcion(p.getDescripcion());
        cur.setPrecio(p.getPrecio());
        cur.setStock(p.getStock());
        return productoRepository.save(cur);
    }

    public boolean delete(Long id) {
        if (!productoRepository.existsById(id)) return false;
        productoRepository.deleteById(id); 
        return true;
    }

    // Métodos adicionales de búsqueda
    public List<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> findProductosConBajoStock(Integer stockMinimo) {
        return productoRepository.findByStockLessThan(stockMinimo);
    }

    public List<Producto> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
        return productoRepository.findByPrecioBetween(minPrecio, maxPrecio);
    }

    public List<Producto> findDisponibles() {
        return productoRepository.findDisponiblesOrderByNombre();
    }

    public boolean actualizarStock(Long productoId, Integer nuevoStock) {
        Optional<Producto> producto = productoRepository.findById(productoId);
        if (producto.isPresent()) {
            producto.get().setStock(nuevoStock);
            productoRepository.save(producto.get());
            return true;
        }
        return false;
    }
}
