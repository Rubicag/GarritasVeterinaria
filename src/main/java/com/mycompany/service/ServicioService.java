package com.mycompany.service;

import com.mycompany.model.Servicio;
import com.mycompany.repository.ServicioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) { 
        this.servicioRepository = servicioRepository; 
    }

    public List<Servicio> findAll() { 
        return servicioRepository.findAll(); 
    }

    public List<Servicio> listAll() { 
        return findAll(); 
    }

    public Optional<Servicio> getById(Long id) { 
        return servicioRepository.findById(id); 
    }

    public Servicio create(Servicio s) { 
        s.setId(null); 
        return servicioRepository.save(s); 
    }

    public Servicio update(Long id, Servicio s) {
        Optional<Servicio> ex = servicioRepository.findById(id); 
        if (ex.isEmpty()) return null;
        
        Servicio cur = ex.get(); 
        cur.setNombre(s.getNombre()); 
        cur.setDescripcion(s.getDescripcion()); 
        cur.setPrecio(s.getPrecio());
        return servicioRepository.save(cur);
    }

    public boolean delete(Long id) { 
        if (!servicioRepository.existsById(id)) return false; 
        servicioRepository.deleteById(id); 
        return true; 
    }

    // Métodos adicionales de búsqueda
    public List<Servicio> findByNombre(String nombre) {
        return servicioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Servicio> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
        return servicioRepository.findByPrecioBetween(minPrecio, maxPrecio);
    }

    public List<Servicio> findAllOrderByPrecio() {
        return servicioRepository.findAllOrderByPrecioAsc();
    }
}
