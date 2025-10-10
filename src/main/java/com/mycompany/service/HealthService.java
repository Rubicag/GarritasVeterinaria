package com.mycompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Verifica el estado básico del sistema
     */
    public String status() {
        return "OK";
    }

    /**
     * Verifica el estado detallado del sistema
     */
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        // Estado general
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Verificar base de datos
        Map<String, Object> database = new HashMap<>();
        try {
            Connection connection = dataSource.getConnection();
            boolean isValid = connection.isValid(5); // 5 segundos timeout
            connection.close();
            
            database.put("status", isValid ? "UP" : "DOWN");
            database.put("message", isValid ? "Base de datos conectada correctamente" : "Error de conexión");
        } catch (Exception e) {
            database.put("status", "DOWN");
            database.put("message", "Error: " + e.getMessage());
        }
        health.put("database", database);
        
        // Verificar servicios
        Map<String, Object> services = new HashMap<>();
        try {
            int userCount = usuarioService.countUsers();
            services.put("status", "UP");
            services.put("userService", "Funcionando - " + userCount + " usuarios registrados");
        } catch (Exception e) {
            services.put("status", "DOWN");
            services.put("userService", "Error: " + e.getMessage());
        }
        health.put("services", services);
        
        // Información del sistema
        Map<String, Object> system = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("os", System.getProperty("os.name"));
        system.put("processors", runtime.availableProcessors());
        system.put("memoryUsed", formatBytes(usedMemory));
        system.put("memoryFree", formatBytes(freeMemory));
        system.put("memoryMax", formatBytes(maxMemory));
        system.put("memoryUsagePercent", Math.round((double)usedMemory / maxMemory * 100));
        
        health.put("system", system);
        
        // Uptime simulado (en una aplicación real, se llevaría el registro del tiempo de inicio)
        health.put("uptime", "Sistema iniciado recientemente");
        
        return health;
    }

    /**
     * Verifica conectividad de base de datos
     */
    public Map<String, Object> checkDatabase() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try {
            Connection connection = dataSource.getConnection();
            
            // Información de la conexión
            dbHealth.put("url", connection.getMetaData().getURL());
            dbHealth.put("driver", connection.getMetaData().getDriverName());
            dbHealth.put("version", connection.getMetaData().getDriverVersion());
            
            // Verificar conexión
            boolean isValid = connection.isValid(10);
            dbHealth.put("connected", isValid);
            dbHealth.put("status", isValid ? "HEALTHY" : "UNHEALTHY");
            
            if (isValid) {
                dbHealth.put("message", "Conexión a base de datos exitosa");
                
                // Información adicional si está conectado
                try {
                    int userCount = usuarioService.countUsers();
                    dbHealth.put("sampleQuery", "OK - " + userCount + " usuarios encontrados");
                } catch (Exception e) {
                    dbHealth.put("sampleQuery", "Error en consulta: " + e.getMessage());
                }
            } else {
                dbHealth.put("message", "Conexión a base de datos falló");
            }
            
            connection.close();
            
        } catch (Exception e) {
            dbHealth.put("connected", false);
            dbHealth.put("status", "ERROR");
            dbHealth.put("message", "Error al conectar: " + e.getMessage());
            dbHealth.put("error", e.getClass().getSimpleName());
        }
        
        dbHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return dbHealth;
    }

    /**
     * Información del sistema y rendimiento
     */
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // Runtime información
        Runtime runtime = Runtime.getRuntime();
        
        // Memoria
        Map<String, Object> memory = new HashMap<>();
        memory.put("max", formatBytes(runtime.maxMemory()));
        memory.put("total", formatBytes(runtime.totalMemory()));
        memory.put("free", formatBytes(runtime.freeMemory()));
        memory.put("used", formatBytes(runtime.totalMemory() - runtime.freeMemory()));
        
        systemInfo.put("memory", memory);
        
        // Procesador
        systemInfo.put("processors", runtime.availableProcessors());
        
        // Sistema operativo
        systemInfo.put("os", Map.of(
            "name", System.getProperty("os.name"),
            "version", System.getProperty("os.version"),
            "arch", System.getProperty("os.arch")
        ));
        
        // Java
        systemInfo.put("java", Map.of(
            "version", System.getProperty("java.version"),
            "vendor", System.getProperty("java.vendor"),
            "home", System.getProperty("java.home")
        ));
        
        // Usuario y directorio
        systemInfo.put("user", Map.of(
            "name", System.getProperty("user.name"),
            "home", System.getProperty("user.home"),
            "dir", System.getProperty("user.dir")
        ));
        
        systemInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return systemInfo;
    }

    /**
     * Formatea bytes en formato legible
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Ping simple para verificar disponibilidad
     */
    public Map<String, Object> ping() {
        return Map.of(
            "message", "pong",
            "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            "status", "alive"
        );
    }
}
