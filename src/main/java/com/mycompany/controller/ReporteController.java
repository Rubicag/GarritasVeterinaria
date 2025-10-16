package com.mycompany.controller;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Producto;
import com.mycompany.model.Servicio;
import com.mycompany.service.CitaService;
import com.mycompany.service.MascotaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ServicioService;
import com.mycompany.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final ProductoService productoService;
    private final ServicioService servicioService;

    public ReporteController(UsuarioService usuarioService, MascotaService mascotaService,
                           CitaService citaService, ProductoService productoService,
                           ServicioService servicioService) {
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
        this.productoService = productoService;
        this.servicioService = servicioService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // Estadísticas generales
            dashboard.put("totalUsuarios", usuarioService.countUsers());
            dashboard.put("totalMascotas", mascotaService.count());
            dashboard.put("totalProductos", productoService.findAll().size());
            dashboard.put("totalServicios", servicioService.findAll().size());
            
            // Estadísticas de citas
            List<Cita> todasCitas = citaService.listAll();
            List<Cita> citasFuturas = citaService.findCitasFuturas();
            dashboard.put("totalCitas", todasCitas.size());
            dashboard.put("citasFuturas", citasFuturas.size());
            
            // Productos con bajo stock
            List<Producto> bajoStock = productoService.findProductosConBajoStock(5);
            dashboard.put("productosConBajoStock", bajoStock.size());
            
            // Valor total del inventario
            double valorInventario = productoService.findAll().stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();
            dashboard.put("valorTotalInventario", valorInventario);
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar datos del dashboard"));
        }
    }

    @GetMapping("/citas")
    public ResponseEntity<?> getReporteCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        try {
            List<Cita> citas;
            
            if (inicio != null && fin != null) {
                citas = citaService.findByFechaBetween(inicio, fin);
            } else {
                citas = citaService.listAll();
            }
            
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("totalCitas", citas.size());
            reporte.put("citas", citas);
            
            // Agrupar por servicio
            Map<String, Long> citasPorServicio = citas.stream()
                    .filter(c -> c.getServicio() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getServicio().getNombre(),
                            Collectors.counting()
                    ));
            reporte.put("citasPorServicio", citasPorServicio);
            
            // Ingresos estimados (si las citas tienen servicios con precios)
            double ingresosEstimados = citas.stream()
                    .filter(c -> c.getServicio() != null && c.getServicio().getPrecio() != null)
                    .mapToDouble(c -> c.getServicio().getPrecio())
                    .sum();
            reporte.put("ingresosEstimados", ingresosEstimados);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar reporte de citas"));
        }
    }

    @GetMapping("/mascotas")
    public ResponseEntity<?> getReporteMascotas() {
        try {
            List<Mascota> mascotas = mascotaService.findAll();
            Map<String, Object> reporte = new HashMap<>();
            
            reporte.put("totalMascotas", mascotas.size());
            
            // Agrupar por especie
            Map<String, Long> mascotasPorEspecie = mascotas.stream()
                    .filter(m -> m.getEspecie() != null)
                    .collect(Collectors.groupingBy(
                            Mascota::getEspecie,
                            Collectors.counting()
                    ));
            reporte.put("mascotasPorEspecie", mascotasPorEspecie);
            
            // Agrupar por raza (top 10)
            Map<String, Long> mascotasPorRaza = mascotas.stream()
                    .filter(m -> m.getRaza() != null)
                    .collect(Collectors.groupingBy(
                            Mascota::getRaza,
                            Collectors.counting()
                    ));
            reporte.put("mascotasPorRaza", mascotasPorRaza);
            
            // Distribución por edad
            Map<String, Long> mascotasPorEdad = mascotas.stream()
                    .filter(m -> m.getEdad() != null)
                    .collect(Collectors.groupingBy(
                            m -> {
                                int edad = m.getEdad();
                                if (edad <= 1) return "Cachorro (0-1 año)";
                                else if (edad <= 3) return "Joven (2-3 años)";
                                else if (edad <= 7) return "Adulto (4-7 años)";
                                else return "Senior (8+ años)";
                            },
                            Collectors.counting()
                    ));
            reporte.put("mascotasPorEdad", mascotasPorEdad);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar reporte de mascotas"));
        }
    }

    @GetMapping("/inventario")
    public ResponseEntity<?> getReporteInventario() {
        try {
            List<Producto> productos = productoService.findAll();
            Map<String, Object> reporte = new HashMap<>();
            
            reporte.put("totalProductos", productos.size());
            
            // Valor total del inventario
            double valorTotal = productos.stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();
            reporte.put("valorTotalInventario", valorTotal);
            
            // Productos con mayor valor en inventario (top 10)
            List<Map<String, Object>> productosPorValor = productos.stream()
                    .filter(p -> p.getPrecio() != null && p.getStock() != null)
                    .sorted((p1, p2) -> Double.compare(
                            p2.getPrecio() * p2.getStock(),
                            p1.getPrecio() * p1.getStock()
                    ))
                    .limit(10)
                    .map(p -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", p.getId());
                        item.put("nombre", p.getNombre());
                        item.put("precio", p.getPrecio());
                        item.put("stock", p.getStock());
                        item.put("valorTotal", p.getPrecio() * p.getStock());
                        return item;
                    })
                    .collect(Collectors.toList());
            reporte.put("productosPorValor", productosPorValor);
            
            // Productos con bajo stock
            List<Producto> bajoStock = productoService.findProductosConBajoStock(5);
            reporte.put("productosConBajoStock", bajoStock);
            
            // Productos sin stock
            List<Producto> sinStock = productos.stream()
                    .filter(p -> p.getStock() == null || p.getStock() == 0)
                    .collect(Collectors.toList());
            reporte.put("productosSinStock", sinStock);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar reporte de inventario"));
        }
    }

    @GetMapping("/servicios")
    public ResponseEntity<?> getReporteServicios() {
        try {
            List<Servicio> servicios = servicioService.findAll();
            List<Cita> citas = citaService.listAll();
            
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("totalServicios", servicios.size());
            
            // Servicios más utilizados
            Map<String, Long> serviciosPopulares = citas.stream()
                    .filter(c -> c.getServicio() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getServicio().getNombre(),
                            Collectors.counting()
                    ));
            reporte.put("serviciosPopulares", serviciosPopulares);
            
            // Ingresos por servicio
            Map<String, Double> ingresosPorServicio = new HashMap<>();
            servicios.forEach(servicio -> {
                long vecesUtilizado = citas.stream()
                        .filter(c -> c.getServicio() != null && 
                                   c.getServicio().getId().equals(servicio.getId()))
                        .count();
                double ingreso = vecesUtilizado * (servicio.getPrecio() != null ? servicio.getPrecio() : 0.0);
                ingresosPorServicio.put(servicio.getNombre(), ingreso);
            });
            reporte.put("ingresosPorServicio", ingresosPorServicio);
            
            // Precio promedio de servicios
            double precioPromedio = servicios.stream()
                    .filter(s -> s.getPrecio() != null)
                    .mapToDouble(Servicio::getPrecio)
                    .average()
                    .orElse(0.0);
            reporte.put("precioPromedio", precioPromedio);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al generar reporte de servicios"));
        }
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> getMetricas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            Map<String, Object> metricas = new HashMap<>();
            
            // Métricas generales
            metricas.put("totalUsuarios", usuarioService.countUsers());
            metricas.put("totalMascotas", mascotaService.count());
            metricas.put("totalProductos", productoService.findAll().size());
            metricas.put("totalServicios", servicioService.findAll().size());
            
            // Citas en el período especificado
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde, hasta) : 
                citaService.listAll();
            metricas.put("totalCitas", citas.size());
            
            // Ingresos estimados
            double ingresos = citas.stream()
                    .filter(c -> c.getServicio() != null && c.getServicio().getPrecio() != null)
                    .mapToDouble(c -> c.getServicio().getPrecio())
                    .sum();
            metricas.put("ingresosEstimados", ingresos);
            
            // Productos con bajo stock
            metricas.put("productosConBajoStock", productoService.findProductosConBajoStock(5).size());
            
            // Valor total del inventario
            double valorInventario = productoService.findAll().stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();
            metricas.put("valorInventario", valorInventario);
            
            return ResponseEntity.ok(metricas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener métricas: " + e.getMessage()));
        }
    }

    @GetMapping("/consultas-tiempo")
    public ResponseEntity<?> getConsultasTiempo(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde, hasta) : 
                citaService.listAll();
                
            Map<String, Object> resultado = new HashMap<>();
            
            // Agrupar citas por día
            Map<String, Long> citasPorDia = citas.stream()
                    .filter(c -> c.getFecha() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getFecha().toLocalDate().toString(),
                            Collectors.counting()
                    ));
            
            resultado.put("labels", citasPorDia.keySet());
            resultado.put("data", citasPorDia.values());
            resultado.put("total", citas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener consultas por tiempo: " + e.getMessage()));
        }
    }

    @GetMapping("/tipos-consulta")
    public ResponseEntity<?> getTiposConsulta(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde, hasta) : 
                citaService.listAll();
                
            Map<String, Long> tiposConsulta = citas.stream()
                    .filter(c -> c.getServicio() != null && c.getServicio().getNombre() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getServicio().getNombre(),
                            Collectors.counting()
                    ));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("labels", tiposConsulta.keySet());
            resultado.put("data", tiposConsulta.values());
            resultado.put("total", citas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener tipos de consulta: " + e.getMessage()));
        }
    }

    @GetMapping("/top-veterinarios")
    public ResponseEntity<?> getTopVeterinarios(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde, hasta) : 
                citaService.listAll();
                
            // Simulamos veterinarios - en una implementación real tendrías una entidad Veterinario
            Map<String, Long> veterinarios = citas.stream()
                    .collect(Collectors.groupingBy(
                            c -> "Dr. " + (c.getId() % 5 == 0 ? "García" : 
                                          c.getId() % 4 == 0 ? "Martínez" : 
                                          c.getId() % 3 == 0 ? "López" : 
                                          c.getId() % 2 == 0 ? "Rodríguez" : "Fernández"),
                            Collectors.counting()
                    ));
            
            List<Map<String, Object>> topVets = veterinarios.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .map(entry -> {
                        Map<String, Object> vet = new HashMap<>();
                        vet.put("nombre", entry.getKey());
                        vet.put("consultas", entry.getValue());
                        return vet;
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(topVets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener top veterinarios: " + e.getMessage()));
        }
    }

    @GetMapping("/especies")
    public ResponseEntity<?> getEspecies(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            List<Mascota> mascotas = mascotaService.findAll();
            
            Map<String, Long> especies = mascotas.stream()
                    .filter(m -> m.getEspecie() != null && !m.getEspecie().trim().isEmpty())
                    .collect(Collectors.groupingBy(
                            Mascota::getEspecie,
                            Collectors.counting()
                    ));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("labels", especies.keySet());
            resultado.put("data", especies.values());
            resultado.put("total", mascotas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener especies: " + e.getMessage()));
        }
    }

    @GetMapping("/inventario-stats")
    public ResponseEntity<?> getInventarioStats() {
        try {
            List<Producto> productos = productoService.findAll();
            
            Map<String, Object> stats = new HashMap<>();
            
            // Estadísticas básicas
            stats.put("totalProductos", productos.size());
            
            // Valor total del inventario
            double valorTotal = productos.stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();
            stats.put("valorTotal", valorTotal);
            
            // Productos con stock bajo
            int bajoStock = productoService.findProductosConBajoStock(5).size();
            stats.put("productosConBajoStock", bajoStock);
            
            // Productos sin stock
            int sinStock = (int) productos.stream()
                    .filter(p -> p.getStock() == null || p.getStock() == 0)
                    .count();
            stats.put("productosSinStock", sinStock);
            
            // Stock total
            int stockTotal = productos.stream()
                    .mapToInt(p -> p.getStock() != null ? p.getStock() : 0)
                    .sum();
            stats.put("stockTotal", stockTotal);
            
            // Precio promedio
            double precioPromedio = productos.stream()
                    .filter(p -> p.getPrecio() != null)
                    .mapToDouble(Producto::getPrecio)
                    .average()
                    .orElse(0.0);
            stats.put("precioPromedio", precioPromedio);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de inventario: " + e.getMessage()));
        }
    }

    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<?> getIngresosMensuales() {
        try {
            List<Cita> citas = citaService.listAll();
            
            Map<String, Double> ingresosPorMes = citas.stream()
                    .filter(c -> c.getFecha() != null && c.getServicio() != null && c.getServicio().getPrecio() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getFecha().getYear() + "-" + String.format("%02d", c.getFecha().getMonthValue()),
                            Collectors.summingDouble(c -> c.getServicio().getPrecio())
                    ));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("labels", ingresosPorMes.keySet());
            resultado.put("data", ingresosPorMes.values());
            
            double totalIngresos = ingresosPorMes.values().stream().mapToDouble(Double::doubleValue).sum();
            resultado.put("total", totalIngresos);
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener ingresos mensuales: " + e.getMessage()));
        }
    }

    @GetMapping("/consultas-detalle")
    public ResponseEntity<?> getConsultasDetalle(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde, hasta) : 
                citaService.listAll();
                
            List<Map<String, Object>> consultas = citas.stream()
                    .map(cita -> {
                        Map<String, Object> consulta = new HashMap<>();
                        consulta.put("id", cita.getId());
                        consulta.put("fecha", cita.getFecha().toString());
                        consulta.put("mascota", cita.getMascota() != null ? cita.getMascota().getNombre() : "N/A");
                        consulta.put("dueno", cita.getMascota() != null && cita.getMascota().getPropietario() != null ? 
                                    cita.getMascota().getPropietario().getNombre() : "N/A");
                        consulta.put("servicio", cita.getServicio() != null ? cita.getServicio().getNombre() : "N/A");
                        consulta.put("precio", cita.getServicio() != null ? cita.getServicio().getPrecio() : 0.0);
                        consulta.put("estado", cita.getEstado() != null ? cita.getEstado() : "Pendiente");
                        return consulta;
                    })
                    .collect(Collectors.toList());
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("consultas", consultas);
            resultado.put("total", consultas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener consultas detalladas: " + e.getMessage()));
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<?> exportarCSV(@RequestParam String tipo) {
        try {
            // Aquí se implementaría la lógica de exportación a CSV
            // Por ahora retornamos una respuesta indicando que está disponible
            return ResponseEntity.ok(Map.of(
                "message", "Exportación CSV disponible para: " + tipo,
                "available", true,
                "format", "CSV"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al exportar CSV"));
        }
    }
}
