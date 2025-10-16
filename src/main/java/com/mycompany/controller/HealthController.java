package com.mycompany.controller;

import com.mycompany.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para endpoints de health/estado del sistema
 * Proporciona información sobre el estado de la aplicación y sus componentes
 */
@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Autowired
    private HealthService healthService;

    /**
     * Endpoint básico de health check
     * GET /api/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            String status = healthService.status();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    /**
     * Endpoint detallado de health check
     * GET /api/health/detailed
     */
    @GetMapping("/health/detailed")
    public ResponseEntity<Map<String, Object>> healthDetailed() {
        try {
            Map<String, Object> healthInfo = healthService.healthCheck();
            return ResponseEntity.ok(healthInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "ERROR",
                        "message", e.getMessage(),
                        "error", e.getClass().getSimpleName()
                    ));
        }
    }

    /**
     * Endpoint para verificar estado de la base de datos
     * GET /api/health/database
     */
    @GetMapping("/health/database")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        try {
            Map<String, Object> dbHealth = healthService.checkDatabase();
            
            // Determinar el status HTTP basado en el estado de la DB
            boolean isHealthy = "HEALTHY".equals(dbHealth.get("status"));
            HttpStatus status = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            
            return ResponseEntity.status(status).body(dbHealth);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "ERROR",
                        "message", "Error checking database health: " + e.getMessage(),
                        "connected", false
                    ));
        }
    }

    /**
     * Endpoint para información del sistema
     * GET /api/health/system
     */
    @GetMapping("/health/system")
    public ResponseEntity<Map<String, Object>> systemInfo() {
        try {
            Map<String, Object> systemInfo = healthService.getSystemInfo();
            return ResponseEntity.ok(systemInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error retrieving system information: " + e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint simple de ping
     * GET /api/health/ping
     */
    @GetMapping("/health/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        try {
            Map<String, Object> pong = healthService.ping();
            return ResponseEntity.ok(pong);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "message", "ERROR",
                        "error", e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint para verificar si la aplicación está lista
     * GET /api/health/ready
     */
    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        try {
            // Verificar que los servicios críticos estén disponibles
            String basicStatus = healthService.status();
            Map<String, Object> dbHealth = healthService.checkDatabase();
            
            boolean isReady = "OK".equals(basicStatus) && 
                             "HEALTHY".equals(dbHealth.get("status"));
            
            Map<String, Object> readiness = Map.of(
                "ready", isReady,
                "status", isReady ? "READY" : "NOT_READY",
                "message", isReady ? "Application is ready to serve requests" : 
                                   "Application is not ready - some services unavailable"
            );
            
            HttpStatus status = isReady ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            return ResponseEntity.status(status).body(readiness);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                        "ready", false,
                        "status", "ERROR",
                        "message", "Error checking readiness: " + e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint para verificar si la aplicación está viva (liveness probe)
     * GET /api/health/live
     */
    @GetMapping("/health/live")
    public ResponseEntity<Map<String, Object>> liveness() {
        try {
            // Simple check - si el servicio responde, está "alive"
            healthService.status(); // Verificar que el servicio responda
            
            Map<String, Object> liveness = Map.of(
                "alive", true,
                "status", "ALIVE",
                "message", "Application is running"
            );
            
            return ResponseEntity.ok(liveness);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "alive", false,
                        "status", "DEAD",
                        "message", "Application liveness check failed: " + e.getMessage()
                    ));
        }
    }
}