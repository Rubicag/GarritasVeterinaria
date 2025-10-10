package com.mycompany.controller;

import com.mycompany.model.Mascota;
import com.mycompany.service.MascotaService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        try {
            return ResponseEntity.ok(Map.of("count", mascotaService.count()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"", "/", "/all"})
    public ResponseEntity<List<Mascota>> all() {
        try {
            return ResponseEntity.ok(mascotaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> getById(@PathVariable Long id) {
        try {
            Optional<Mascota> m = mascotaService.findById(id);
            return m.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> mascotaData) {
        try {
            Mascota mascota = new Mascota();
            mascota.setNombre((String) mascotaData.get("nombre"));
            mascota.setEspecie((String) mascotaData.get("especie"));
            mascota.setRaza((String) mascotaData.get("raza"));
            
            if (mascotaData.get("edad") != null) {
                mascota.setEdad(Integer.valueOf(mascotaData.get("edad").toString()));
            }
            
            Long propietarioId = Long.valueOf(mascotaData.get("propietarioId").toString());
            Mascota created = mascotaService.create(mascota, propietarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Mascota m) {
        try {
            Mascota updated = mascotaService.update(id, m);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boolean deleted = mascotaService.delete(id);
            if (!deleted) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    // Endpoints adicionales de búsqueda
    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<Mascota>> getByPropietario(@PathVariable Long propietarioId) {
        try {
            List<Mascota> mascotas = mascotaService.findByPropietarioId(propietarioId);
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Mascota>> getByEspecie(@PathVariable String especie) {
        try {
            List<Mascota> mascotas = mascotaService.findByEspecie(especie);
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Mascota>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<Mascota> mascotas = mascotaService.findByNombre(nombre);
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
