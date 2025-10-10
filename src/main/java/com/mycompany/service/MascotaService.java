package com.mycompany.service;

import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public MascotaService(MascotaRepository mascotaRepository, UsuarioRepository usuarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public long count() { 
        return mascotaRepository.count(); 
    }

    public List<Mascota> findAll() { 
        return mascotaRepository.findAll(); 
    }

    public Optional<Mascota> findById(Long id) { 
        return mascotaRepository.findById(id); 
    }

    public Mascota create(Mascota m, Long propietarioId) {
        Optional<Usuario> propietario = usuarioRepository.findById(propietarioId);
        if (propietario.isEmpty()) {
            throw new RuntimeException("Propietario no encontrado");
        }
        
        m.setId(null);
        m.setPropietario(propietario.get());
        return mascotaRepository.save(m);
    }

    public Mascota update(Long id, Mascota m) {
        return mascotaRepository.findById(id).map(existing -> {
            existing.setNombre(m.getNombre());
            existing.setEspecie(m.getEspecie());
            existing.setRaza(m.getRaza());
            existing.setEdad(m.getEdad());
            
            // Si se proporciona un nuevo propietario
            if (m.getPropietario() != null) {
                existing.setPropietario(m.getPropietario());
            }
            
            return mascotaRepository.save(existing);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (mascotaRepository.findById(id).isPresent()) { 
            mascotaRepository.deleteById(id); 
            return true; 
        }
        return false;
    }

    // Métodos adicionales de búsqueda
    public List<Mascota> findByPropietario(Usuario propietario) {
        return mascotaRepository.findByPropietario(propietario);
    }

    public List<Mascota> findByEspecie(String especie) {
        return mascotaRepository.findByEspecie(especie);
    }

    public List<Mascota> findByNombre(String nombre) {
        return mascotaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Mascota> findByPropietarioId(Long propietarioId) {
        Optional<Usuario> propietario = usuarioRepository.findById(propietarioId);
        if (propietario.isPresent()) {
            return mascotaRepository.findByPropietario(propietario.get());
        }
        return List.of();
    }
}
