package com.mycompany.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.mycompany.service.UsuarioService;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador de debug para diagnosticar problemas
 */
@RestController
public class DebugController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/debug/usuarios")
    public ResponseEntity<Map<String, Object>> debugUsuarios() {
        Map<String, Object> debug = new HashMap<>();
        
        try {
            // Verificar servicio
            debug.put("servicioDisponible", usuarioService != null);
            
            // Contar usuarios
            int count = usuarioService.countUsers();
            debug.put("totalUsuarios", count);
            
            // Obtener lista de usuarios
            var usuarios = usuarioService.findAll();
            debug.put("usuariosSize", usuarios.size());
            debug.put("usuarios", usuarios);
            
            debug.put("status", "OK");
            
        } catch (Exception e) {
            debug.put("status", "ERROR");
            debug.put("error", e.getMessage());
            debug.put("stackTrace", e.getStackTrace());
        }
        
        return ResponseEntity.ok(debug);
    }

    @GetMapping("/debug/database")
    public ResponseEntity<Map<String, Object>> debugDatabase() {
        Map<String, Object> debug = new HashMap<>();
        
        try {
            debug.put("timestamp", System.currentTimeMillis());
            debug.put("status", "Checking database connection...");
            
            // Test básico de conexión
            int userCount = usuarioService.countUsers();
            debug.put("userCount", userCount);
            debug.put("connectionStatus", "OK");
            
        } catch (Exception e) {
            debug.put("connectionStatus", "ERROR");
            debug.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(debug);
    }
}