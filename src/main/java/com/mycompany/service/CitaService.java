package com.mycompany.service;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.model.Usuario;
import com.mycompany.repository.CitaRepository;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import com.mycompany.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public CitaService(CitaRepository citaRepository, MascotaRepository mascotaRepository, 
                      ServicioRepository servicioRepository, UsuarioRepository usuarioRepository,
                      NotificacionService notificacionService) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
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
        
        // Asignar veterinario si no está definido
        if (c.getVeterinario() == null) {
            // Buscar el primer usuario disponible como veterinario
            List<Usuario> usuarios = usuarioRepository.findAll();
            if (usuarios.isEmpty()) {
                throw new RuntimeException("No hay usuarios disponibles en el sistema");
            }
            c.setVeterinario(usuarios.get(0));
        }
        
        // Extraer la hora del LocalDateTime y asignarla al campo hora
        if (c.getFecha() != null && c.getHora() == null) {
            c.setHora(c.getFecha().toLocalTime());
        }
        
        // Establecer estado inicial si no está definido
        if (c.getEstado() == null) {
            c.setEstado(Cita.EstadoCita.Pendiente);
        }
        
        Cita citaGuardada = citaRepository.save(c);
        
        // Enviar confirmación por email
        try {
            notificacionService.enviarConfirmacionCita(citaGuardada);
        } catch (Exception e) {
            // No fallar la creación si falla el email
            // El error ya se registra en NotificacionService
        }
        
        return citaGuardada;
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

    /**
     * Buscar citas con filtros opcionales y paginación.
     * @param fechaDesde Fecha inicial (opcional)
     * @param fechaHasta Fecha final (opcional)
     * @param estado Estado de la cita (opcional)
     * @param veterinarioId ID del veterinario (opcional)
     * @param mascotaId ID de la mascota (opcional)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de citas que cumplen los criterios
     */
    public Page<Cita> findByFilters(
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            Cita.EstadoCita estado,
            Long veterinarioId,
            Long mascotaId,
            Pageable pageable
    ) {
        return citaRepository.findByFilters(
            fechaDesde, fechaHasta, estado, veterinarioId, mascotaId, pageable
        );
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

