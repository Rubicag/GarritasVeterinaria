package com.mycompany.controller;

import com.mycompany.model.Servicio;
import com.mycompany.service.ServicioService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService service;
    public ServicioController(ServicioService service) { this.service = service; }

    @GetMapping
    public List<Servicio> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> get(@PathVariable Long id) {
        Optional<Servicio> s = service.getById(id);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Servicio> create(@RequestBody Servicio s) { return ResponseEntity.ok(service.create(s)); }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Long id, @RequestBody Servicio s) {
        Servicio up = service.update(id, s); if (up == null) return ResponseEntity.notFound().build(); return ResponseEntity.ok(up);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { if (!service.delete(id)) return ResponseEntity.notFound().build(); return ResponseEntity.noContent().build(); }
}
