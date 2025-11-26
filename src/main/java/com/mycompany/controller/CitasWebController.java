package com.mycompany.controller;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.model.Usuario;
import com.mycompany.service.CitaService;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import com.mycompany.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CitasWebController {

    private final CitaService citaService;
    private final MascotaRepository mascotaRepo;
    private final ServicioRepository servicioRepo;
    private final UsuarioRepository usuarioRepo;

    public CitasWebController(CitaService citaService, MascotaRepository mascotaRepo, 
                              ServicioRepository servicioRepo, UsuarioRepository usuarioRepo) {
        this.citaService = citaService;
        this.mascotaRepo = mascotaRepo;
        this.servicioRepo = servicioRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/citas")
    public String viewCitas(
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long veterinarioId,
            @RequestParam(required = false) Long mascotaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // Convertir strings de fecha a LocalDateTime
        LocalDateTime fechaDesdeLD = (fechaDesde != null && !fechaDesde.isEmpty()) 
            ? LocalDate.parse(fechaDesde).atStartOfDay() 
            : null;
        LocalDateTime fechaHastaLD = (fechaHasta != null && !fechaHasta.isEmpty()) 
            ? LocalDate.parse(fechaHasta).atTime(LocalTime.MAX) 
            : null;
        
        // Convertir estado string a enum
        Cita.EstadoCita estadoEnum = null;
        if (estado != null && !estado.isEmpty()) {
            try {
                estadoEnum = Cita.EstadoCita.valueOf(estado);
            } catch (IllegalArgumentException e) {
                // Estado inválido, ignorar
            }
        }
        
        // Configurar paginación y ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Obtener citas filtradas y paginadas
        Page<Cita> citasPage = citaService.findByFilters(
            fechaDesdeLD, fechaHastaLD, estadoEnum, veterinarioId, mascotaId, pageable
        );
        
        // Datos para formularios
        List<Mascota> mascotas = mascotaRepo.findAll();
        List<Servicio> servicios = servicioRepo.findAll();
        
        // Filtrar solo veterinarios (rol VETERINARIO)
        List<Usuario> veterinarios = usuarioRepo.findAll().stream()
            .filter(u -> u.getRol() != null && 
                        (u.getRol().getNombre().equalsIgnoreCase("VETERINARIO") || 
                         u.getRol().getNombre().equalsIgnoreCase("ADMIN")))
            .collect(java.util.stream.Collectors.toList());
        
        // Agregar atributos al modelo
        model.addAttribute("citasPage", citasPage);
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("servicios", servicios);
        model.addAttribute("veterinarios", veterinarios);
        model.addAttribute("nuevaCita", new Cita());
        
        // Mantener valores de filtros en el formulario
        model.addAttribute("filtroFechaDesde", fechaDesde);
        model.addAttribute("filtroFechaHasta", fechaHasta);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroVeterinarioId", veterinarioId);
        model.addAttribute("filtroMascotaId", mascotaId);
        
        return "citas";
    }

    @PostMapping("/citas")
    public String crearCita(@ModelAttribute("nuevaCita") Cita c, 
                           @RequestParam("mascotaId") Long mascotaId,
                           @RequestParam("servicioId") Long servicioId,
                           @RequestParam("veterinarioId") Long veterinarioId) {
        try {
            // Asignar veterinario manualmente seleccionado
            Usuario veterinario = usuarioRepo.findById(veterinarioId)
                .orElseThrow(() -> new IllegalArgumentException("Veterinario no encontrado"));
            c.setVeterinario(veterinario);
            
            citaService.create(c, mascotaId, servicioId);
            return "redirect:/citas?success=true";
        } catch (Exception e) {
            return "redirect:/citas?error=" + e.getMessage();
        }
    }
}