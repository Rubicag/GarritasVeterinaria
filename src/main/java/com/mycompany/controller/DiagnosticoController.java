package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.service.ProductoService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/diagnostico")
public class DiagnosticoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "Servidor funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productos-count")
    public ResponseEntity<Map<String, Object>> productosCount() {
        try {
            Map<String, Object> response = new HashMap<>();
            int count = productoService.findAll().size();
            response.put("status", "OK");
            response.put("count", count);
            response.put("message", "Conteo de productos exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/productos-simple")
    public ResponseEntity<Map<String, Object>> productosSimple() {
        try {
            Map<String, Object> response = new HashMap<>();
            var productos = productoService.findAll();
            response.put("status", "OK");
            response.put("total", productos.size());
            
            // Solo enviar información básica sin objetos complejos
            response.put("primerProducto", productos.isEmpty() ? null : 
                Map.of("id", productos.get(0).getId(), 
                       "nombre", productos.get(0).getNombre()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}