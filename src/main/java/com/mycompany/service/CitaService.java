package com.mycompany.service;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.repository.CitaRepository;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final ServicioRepository servicioRepository;

    public CitaService(CitaRepository citaRepository, MascotaRepository mascotaRepository, ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.servicioRepository = servicioRepository;
    }

    public List<Cita> listAll() { 
        return citaRepository.findAll(); 
    }
    
    public Optional<Cita> getById(Long id) { 
        return citaRepository.findById(id); 
    }
    
    public Cita create(Cita c, Long mascotaId, Long servicioId) {
        Optional<Mascota> mascota = mascotaRepository.findById(mascotaId);
        Optional<Servicio> servicio = servicioRepository.findById(servicioId);
        
        if (mascota.isEmpty()) {
            throw new RuntimeException("Mascota no encontrada");
        }
        if (servicio.isEmpty()) {
            throw new RuntimeException("Servicio no encontrado");
        }
        
        // Validar que no haya conflicto de horarios
        if (hayConflictoHorario(c.getFecha(), mascotaId)) {
            throw new RuntimeException("Ya existe una cita para esta mascota en el horario seleccionado");
        }
        
        c.setId(null);
        c.setMascota(mascota.get());
        c.setServicio(servicio.get());
        
        return citaRepository.save(c);
    }
    
    public Cita update(Long id, Cita c) { 
        Optional<Cita> existing = citaRepository.findById(id); 
        if (existing.isEmpty()) return null; 
        
        Cita current = existing.get(); 
        current.setFecha(c.getFecha()); 
        current.setObservaciones(c.getObservaciones());
        
        if (c.getMascota() != null) {
            current.setMascota(c.getMascota());
        }
        if (c.getServicio() != null) {
            current.setServicio(c.getServicio());
        }
        
        return citaRepository.save(current); 
    }
    
    public boolean delete(Long id) { 
        if (!citaRepository.existsById(id)) return false; 
        citaRepository.deleteById(id); 
        return true; 
    }

    // Métodos adicionales de búsqueda y lógica de negocio
    public List<Cita> findByMascota(Mascota mascota) {
        return citaRepository.findByMascota(mascota);
    }

    public List<Cita> findByServicio(Servicio servicio) {
        return citaRepository.findByServicio(servicio);
    }

    public List<Cita> findCitasFuturas() {
        return citaRepository.findCitasFuturas(LocalDateTime.now());
    }

    public List<Cita> findCitasPorDia(LocalDateTime fecha) {
        return citaRepository.findCitasPorDia(fecha);
    }

    public List<Cita> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaBetween(inicio, fin);
    }

    private boolean hayConflictoHorario(LocalDateTime fecha, Long mascotaId) {
        // Buscar citas en un rango de ±1 hora para la misma mascota
        LocalDateTime inicio = fecha.minusHours(1);
        LocalDateTime fin = fecha.plusHours(1);
        
        Optional<Mascota> mascota = mascotaRepository.findById(mascotaId);
        if (mascota.isPresent()) {
            List<Cita> citasExistentes = citaRepository.findByMascota(mascota.get());
            return citasExistentes.stream()
                    .anyMatch(cita -> cita.getFecha().isAfter(inicio) && cita.getFecha().isBefore(fin));
        }
        return false;
    }

    public boolean confirmarCita(Long citaId) {
        // Lógica para confirmar una cita (podríamos agregar un campo estado)
        Optional<Cita> cita = citaRepository.findById(citaId);
        if (cita.isPresent()) {
            // Aquí podríamos actualizar un campo de estado si lo agregamos al modelo
            return true;
        }
        return false;
    }
}

