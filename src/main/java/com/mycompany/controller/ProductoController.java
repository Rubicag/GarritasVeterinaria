package com.mycompany.controller;

import com.mycompany.model.Producto;
import com.mycompany.service.ProductoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping
    public List<Producto> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> get(@PathVariable Long id) {
        Optional<Producto> p = service.getById(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto p) {
        Producto c = service.create(p);
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto p) {
        Producto up = service.update(id, p);
        if (up == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(up);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.delete(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
