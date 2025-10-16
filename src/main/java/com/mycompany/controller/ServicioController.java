package com.mycompany.controller;

import com.mycompany.model.Servicio;
import com.mycompany.service.ServicioService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService service;
    public ServicioController(ServicioService service) { this.service = service; }

    @GetMapping
    public List<Servicio> list() {
        System.out.println("[INFO] Listando todos los servicios...");
        List<Servicio> servicios = service.listAll();
        System.out.println("[DEBUG] Servicios obtenidos: " + servicios.size());
        return servicios;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> get(@PathVariable Long id) {
        System.out.println("[INFO] Obteniendo servicio con ID: " + id);
        Optional<Servicio> s = service.getById(id);
        if (s.isPresent()) {
            System.out.println("[DEBUG] Servicio encontrado: " + s.get());
        } else {
            System.out.println("[WARN] Servicio no encontrado para ID: " + id);
        }
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
