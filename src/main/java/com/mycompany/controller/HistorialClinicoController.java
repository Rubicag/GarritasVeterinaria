package com.mycompany.controller;

import com.mycompany.model.HistorialClinico;
import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import com.mycompany.service.HistorialClinicoService;
import com.mycompany.service.MascotaService;
import com.mycompany.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/historial")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoService historialClinicoService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todos los registros del historial clínico
     */
    @GetMapping
    public ResponseEntity<List<HistorialClinico>> getAllHistorial() {
        List<HistorialClinico> registros = historialClinicoService.findAllOrderByFechaDesc();
        return ResponseEntity.ok(registros);
    }

    /**
     * Obtiene un registro específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistorialClinico> getHistorialById(@PathVariable Long id) {
        Optional<HistorialClinico> historial = historialClinicoService.findById(id);
        return historial.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo registro de historial clínico
     */
    @PostMapping
    public ResponseEntity<HistorialClinico> createHistorial(@RequestBody HistorialClinico historial) {
        try {
            // Validar que existe la mascota
            if (historial.getMascota() == null || historial.getMascota().getId() == null) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Mascota> mascota = mascotaService.findById(historial.getMascota().getId());
            if (mascota.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            historial.setMascota(mascota.get());

            // Validar y asignar veterinario si existe
            if (historial.getVeterinario() != null && historial.getVeterinario().getId() != null) {
                Optional<Usuario> veterinario = usuarioService.findById(historial.getVeterinario().getId());
                if (veterinario.isPresent()) {
                    historial.setVeterinario(veterinario.get());
                }
            }

            HistorialClinico nuevoHistorial = historialClinicoService.save(historial);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHistorial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza un registro de historial clínico existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistorialClinico> updateHistorial(@PathVariable Long id, 
                                                            @RequestBody HistorialClinico historial) {
        try {
            Optional<HistorialClinico> existente = historialClinicoService.findById(id);
            if (existente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar el ID para asegurar que se actualiza el registro correcto
            historial.setId(id);

            // Validar que existe la mascota
            if (historial.getMascota() == null || historial.getMascota().getId() == null) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Mascota> mascota = mascotaService.findById(historial.getMascota().getId());
            if (mascota.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            historial.setMascota(mascota.get());

            // Validar y asignar veterinario si existe
            if (historial.getVeterinario() != null && historial.getVeterinario().getId() != null) {
                Optional<Usuario> veterinario = usuarioService.findById(historial.getVeterinario().getId());
                if (veterinario.isPresent()) {
                    historial.setVeterinario(veterinario.get());
                }
            }

            HistorialClinico actualizado = historialClinicoService.save(historial);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un registro de historial clínico
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorial(@PathVariable Long id) {
        try {
            Optional<HistorialClinico> existente = historialClinicoService.findById(id);
            if (existente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            historialClinicoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el historial de una mascota específica
     */
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialClinico>> getHistorialByMascota(@PathVariable Long mascotaId) {
        Optional<Mascota> mascota = mascotaService.findById(mascotaId);
        if (mascota.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<HistorialClinico> registros = historialClinicoService.findByMascota(mascota.get());
        return ResponseEntity.ok(registros);
    }

    /**
     * Obtiene estadísticas del historial clínico
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Total de registros
        estadisticas.put("totalRegistros", historialClinicoService.count());

        // Conteo por tipo de consulta
        long consultas = historialClinicoService.countByTipoConsulta(HistorialClinico.TipoConsulta.CONSULTA);
        long vacunaciones = historialClinicoService.countByTipoConsulta(HistorialClinico.TipoConsulta.VACUNACION);
        long cirugias = historialClinicoService.countByTipoConsulta(HistorialClinico.TipoConsulta.CIRUGIA);
        long emergencias = historialClinicoService.countByTipoConsulta(HistorialClinico.TipoConsulta.EMERGENCIA);
        long controles = historialClinicoService.countByTipoConsulta(HistorialClinico.TipoConsulta.CONTROL);

        Map<String, Long> porTipo = new HashMap<>();
        porTipo.put("consulta", consultas);
        porTipo.put("vacunacion", vacunaciones);
        porTipo.put("cirugia", cirugias);
        porTipo.put("emergencia", emergencias);
        porTipo.put("control", controles);
        estadisticas.put("porTipo", porTipo);

        // Costo total
        BigDecimal costoTotal = historialClinicoService.calcularCostoTotal();
        estadisticas.put("costoTotal", costoTotal);

        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Busca registros por tipo de consulta
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<HistorialClinico>> getByTipoConsulta(@PathVariable String tipo) {
        try {
            HistorialClinico.TipoConsulta tipoConsulta = HistorialClinico.TipoConsulta.valueOf(tipo.toUpperCase());
            List<HistorialClinico> registros = historialClinicoService.findByTipoConsulta(tipoConsulta);
            return ResponseEntity.ok(registros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca registros por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HistorialClinico>> getByEstado(@PathVariable String estado) {
        try {
            HistorialClinico.EstadoConsulta estadoConsulta = HistorialClinico.EstadoConsulta.valueOf(estado.toUpperCase());
            List<HistorialClinico> registros = historialClinicoService.findByEstado(estadoConsulta);
            return ResponseEntity.ok(registros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
