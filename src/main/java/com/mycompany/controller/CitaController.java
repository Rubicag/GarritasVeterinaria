package com.mycompany.controller;

import com.mycompany.model.Cita;
import com.mycompany.service.CitaService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) { 
        this.citaService = citaService; 
    }

    @GetMapping
    public ResponseEntity<List<Cita>> list() { 
        try {
            List<Cita> citas = citaService.listAll();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> get(@PathVariable Long id) {
        try {
            Optional<Cita> c = citaService.getById(id);
            return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> citaData) {
        try {
            Cita cita = new Cita();
            cita.setFecha(LocalDateTime.parse(citaData.get("fecha").toString()));
            cita.setObservaciones((String) citaData.get("observaciones"));
            
            Long mascotaId = Long.valueOf(citaData.get("mascotaId").toString());
            Long servicioId = Long.valueOf(citaData.get("servicioId").toString());
            
            Cita created = citaService.create(cita, mascotaId, servicioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cita c) {
        try {
            Cita updated = citaService.update(id, c);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!citaService.delete(id)) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    // Endpoints adicionales
    @GetMapping("/futuras")
    public ResponseEntity<List<Cita>> getCitasFuturas() {
        try {
            List<Cita> citas = citaService.findCitasFuturas();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/por-dia")
    public ResponseEntity<List<Cita>> getCitasPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        try {
            List<Cita> citas = citaService.findCitasPorDia(fecha);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCita(@PathVariable Long id) {
        try {
            boolean confirmada = citaService.confirmarCita(id);
            if (confirmada) {
                return ResponseEntity.ok(Map.of("message", "Cita confirmada exitosamente"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }
}

