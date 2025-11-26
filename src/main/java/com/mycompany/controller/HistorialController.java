package com.mycompany.controller;

import com.mycompany.model.HistorialMedico;
import com.mycompany.service.HistorialMedicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de compatibilidad en `/api/historial` para llamadas desde las plantillas.
 */
@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialMedicoService historialService;

    public HistorialController(HistorialMedicoService historialService) {
        this.historialService = historialService;
    }

    @GetMapping
    public ResponseEntity<List<HistorialMedico>> listar() {
        return ResponseEntity.ok(historialService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return historialService.obtenerPorId(id)
                .map(h -> {
                    // Construir un DTO simple (Map) con la estructura que espera el frontend
                    Map<String, Object> dto = new HashMap<>();
                    // fechaConsulta, tipoConsulta, estado y campos principales
                    dto.put("id", h.getId());
                    dto.put("fechaConsulta", h.getFechaConsulta());
                    dto.put("tipoConsulta", h.getTipoConsulta() != null ? h.getTipoConsulta().name() : null);
                    dto.put("estado", h.getEstado() != null ? h.getEstado().name() : null);
                    dto.put("motivoConsulta", h.getMotivoConsulta());
                    dto.put("diagnostico", h.getDiagnostico());
                    dto.put("tratamiento", h.getTratamiento());
                    dto.put("medicamentos", h.getMedicamentos());
                    dto.put("observaciones", h.getObservaciones());

                    // Cita relacionada (puede ser lazy) -> extraer mascota y veterinario si están presentes
                    if (h.getCita() != null) {
                        Map<String, Object> mascotaMap = new HashMap<>();
                        if (h.getCita().getMascota() != null) {
                            mascotaMap.put("id", h.getCita().getMascota().getId());
                            mascotaMap.put("nombre", h.getCita().getMascota().getNombre());
                            mascotaMap.put("especie", h.getCita().getMascota().getEspecie());
                            mascotaMap.put("raza", h.getCita().getMascota().getRaza());
                            if (h.getCita().getMascota().getPropietario() != null) {
                                Map<String, Object> propietarioMap = new HashMap<>();
                                propietarioMap.put("id", h.getCita().getMascota().getPropietario().getId());
                                propietarioMap.put("usuario", h.getCita().getMascota().getPropietario().getUsuario());
                                mascotaMap.put("propietario", propietarioMap);
                            }
                        }
                        dto.put("mascota", mascotaMap);

                        if (h.getCita().getVeterinario() != null) {
                            Map<String, Object> vetMap = new HashMap<>();
                            vetMap.put("id", h.getCita().getVeterinario().getId());
                            vetMap.put("usuario", h.getCita().getVeterinario().getUsuario());
                            dto.put("veterinario", vetMap);
                        }
                    }

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> estadisticas() {
        List<HistorialMedico> todos = historialService.listarTodos();
        Map<String, Object> stats = new HashMap<>();

        long total = todos.size();
        YearMonth ahora = YearMonth.from(LocalDateTime.now());
        long esteMes = todos.stream()
                .filter(h -> h.getFechaConsulta() != null)
                .filter(h -> YearMonth.from(h.getFechaConsulta()).equals(ahora))
                .count();

        long vacunas = todos.stream()
                .filter(h -> h.getTipoConsulta() != null && "VACUNACION".equalsIgnoreCase(h.getTipoConsulta().name()))
                .count();

        long cirugias = todos.stream()
                .filter(h -> h.getTipoConsulta() != null && "CIRUGIA".equalsIgnoreCase(h.getTipoConsulta().name()))
                .count();

        stats.put("total", total);
        stats.put("esteMes", esteMes);
        stats.put("vacunas", vacunas);
        stats.put("cirugias", cirugias);

        return ResponseEntity.ok(stats);
    }
}
