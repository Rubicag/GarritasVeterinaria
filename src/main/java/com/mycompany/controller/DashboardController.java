package com.mycompany.controller;

import com.mycompany.repository.CitaRepository;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import com.mycompany.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para el dashboard con estadísticas y métricas avanzadas.
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "API para estadísticas y métricas del sistema")
public class DashboardController {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardController(CitaRepository citaRepository, 
                              MascotaRepository mascotaRepository,
                              ServicioRepository servicioRepository,
                              UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Estadísticas generales del sistema")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getGeneralStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCitas", citaRepository.count());
        stats.put("totalMascotas", mascotaRepository.count());
        stats.put("totalServicios", servicioRepository.count());
        stats.put("totalUsuarios", usuarioRepository.count());
        
        // Citas futuras
        LocalDateTime ahora = LocalDateTime.now();
        long citasFuturas = citaRepository.findCitasFuturas(ahora).size();
        stats.put("citasFuturas", citasFuturas);
        
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Ingresos mensuales del año actual")
    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<Map<String, Object>> getIngresosMensuales() {
        LocalDateTime inicioAnio = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0);
        LocalDateTime finAnio = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59);
        
        var citas = citaRepository.findByFechaBetween(inicioAnio, finAnio);
        
        // Agrupar por mes
        Map<String, Double> ingresosPorMes = new TreeMap<>();
        for (int mes = 1; mes <= 12; mes++) {
            String mesStr = YearMonth.of(LocalDateTime.now().getYear(), mes).toString();
            ingresosPorMes.put(mesStr, 0.0);
        }
        
        for (var cita : citas) {
            if (cita.getServicio() != null && cita.getFecha() != null) {
                String mes = YearMonth.from(cita.getFecha()).toString();
                double precioServicio = cita.getServicio().getPrecio() != null ? 
                    cita.getServicio().getPrecio() : 0.0;
                ingresosPorMes.merge(mes, precioServicio, Double::sum);
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("meses", new ArrayList<>(ingresosPorMes.keySet()));
        response.put("ingresos", new ArrayList<>(ingresosPorMes.values()));
        response.put("total", ingresosPorMes.values().stream().mapToDouble(Double::doubleValue).sum());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Top 5 servicios más solicitados")
    @GetMapping("/top-servicios")
    public ResponseEntity<List<Map<String, Object>>> getTopServicios() {
        var todasCitas = citaRepository.findAll();
        
        // Contar servicios
        Map<Long, Long> conteoServicios = todasCitas.stream()
            .filter(c -> c.getServicio() != null)
            .collect(Collectors.groupingBy(
                c -> c.getServicio().getId(),
                Collectors.counting()
            ));
        
        // Top 5
        var top5 = conteoServicios.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                var servicio = servicioRepository.findById(entry.getKey()).orElse(null);
                Map<String, Object> item = new HashMap<>();
                item.put("servicioId", entry.getKey());
                item.put("servicioNombre", servicio != null ? servicio.getNombre() : "Desconocido");
                item.put("cantidad", entry.getValue());
                item.put("precio", servicio != null ? servicio.getPrecio() : 0.0);
                return item;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(top5);
    }

    @Operation(summary = "Mascotas con más citas")
    @GetMapping("/mascotas-frecuentes")
    public ResponseEntity<List<Map<String, Object>>> getMascotasFrecuentes() {
        var todasCitas = citaRepository.findAll();
        
        Map<Long, Long> conteoMascotas = todasCitas.stream()
            .filter(c -> c.getMascota() != null)
            .collect(Collectors.groupingBy(
                c -> c.getMascota().getId(),
                Collectors.counting()
            ));
        
        var top10 = conteoMascotas.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(10)
            .map(entry -> {
                var mascota = mascotaRepository.findById(entry.getKey()).orElse(null);
                Map<String, Object> item = new HashMap<>();
                item.put("mascotaId", entry.getKey());
                item.put("mascotaNombre", mascota != null ? mascota.getNombre() : "Desconocido");
                item.put("especie", mascota != null ? mascota.getEspecie() : "N/A");
                item.put("cantidadCitas", entry.getValue());
                return item;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(top10);
    }

    @Operation(summary = "Utilización de veterinarios (citas por veterinario)")
    @GetMapping("/utilizacion-veterinarios")
    public ResponseEntity<List<Map<String, Object>>> getUtilizacionVeterinarios() {
        var todasCitas = citaRepository.findAll();
        
        Map<Long, Long> citasPorVeterinario = todasCitas.stream()
            .filter(c -> c.getVeterinario() != null)
            .collect(Collectors.groupingBy(
                c -> c.getVeterinario().getId(),
                Collectors.counting()
            ));
        
        var utilizacion = citasPorVeterinario.entrySet().stream()
            .map(entry -> {
                var veterinario = usuarioRepository.findById(entry.getKey()).orElse(null);
                Map<String, Object> item = new HashMap<>();
                item.put("veterinarioId", entry.getKey());
                item.put("veterinarioNombre", veterinario != null ? 
                    veterinario.getNombre() + " " + veterinario.getApellido() : "Desconocido");
                item.put("totalCitas", entry.getValue());
                return item;
            })
            .sorted((a, b) -> Long.compare((Long)b.get("totalCitas"), (Long)a.get("totalCitas")))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(utilizacion);
    }

    @Operation(summary = "Resumen completo del dashboard")
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumenCompleto() {
        Map<String, Object> resumen = new HashMap<>();
        
        // Stats generales
        resumen.put("estadisticas", getGeneralStats().getBody());
        
        // Top servicios
        resumen.put("topServicios", getTopServicios().getBody());
        
        // Mascotas frecuentes (top 5)
        var mascotasFrecuentes = getMascotasFrecuentes().getBody();
        resumen.put("mascotasFrecuentes", 
            mascotasFrecuentes != null && mascotasFrecuentes.size() > 5 ? 
            mascotasFrecuentes.subList(0, 5) : mascotasFrecuentes);
        
        // Utilización veterinarios
        resumen.put("utilizacionVeterinarios", getUtilizacionVeterinarios().getBody());
        
        return ResponseEntity.ok(resumen);
    }
}
