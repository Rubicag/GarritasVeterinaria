package com.mycompany.controller;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.MovimientoInventario;
import com.mycompany.model.Producto;
import com.mycompany.model.Servicio;
import com.mycompany.repository.MovimientoInventarioRepository;
import com.mycompany.service.CitaService;
import com.mycompany.service.MascotaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ServicioService;
import com.mycompany.service.UsuarioService;
import java.time.LocalDate;
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
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final ProductoService productoService;
    private final ServicioService servicioService;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public ReporteController(UsuarioService usuarioService, MascotaService mascotaService,
                           CitaService citaService, ProductoService productoService,
                           ServicioService servicioService,
                           MovimientoInventarioRepository movimientoInventarioRepository) {
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
        this.productoService = productoService;
        this.servicioService = servicioService;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            Map<String, Object> metricas = new HashMap<>();
            
            // Si no se especifican fechas, usar el mes actual
            LocalDate fechaDesde = desde != null ? desde : LocalDate.now().withDayOfMonth(1);
            LocalDate fechaHasta = hasta != null ? hasta : LocalDate.now();
            
            // Citas en el período especificado
            List<Cita> citas = citaService.findByFechaBetween(
                fechaDesde.atStartOfDay(), 
                fechaHasta.atTime(23, 59, 59)
            );
            
            // Total de consultas (citas) en el período
            metricas.put("totalConsultas", citas.size());
            
            // Total de ingresos (solo citas atendidas)
            double ingresos = citas.stream()
                    .filter(c -> c.getEstado() == Cita.EstadoCita.Atendida
                            && c.getServicio() != null 
                            && c.getServicio().getPrecio() != null)
                    .mapToDouble(c -> c.getServicio().getPrecio())
                    .sum();
            metricas.put("totalIngresos", ingresos);
            
            // Nuevas mascotas (mascotas únicas atendidas en el período)
            long nuevasMascotas = citas.stream()
                    .filter(c -> c.getMascota() != null)
                    .map(c -> c.getMascota().getId())
                    .distinct()
                    .count();
            metricas.put("nuevasMascotas", nuevasMascotas);
            
            // Tasa de ocupación (porcentaje de citas sobre capacidad máxima estimada)
            // Asumimos capacidad de 10 citas por día
            long diasPeriodo = java.time.temporal.ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;
            long capacidadMaxima = diasPeriodo * 10;
            double tasaOcupacion = capacidadMaxima > 0 ? (citas.size() * 100.0 / capacidadMaxima) : 0;
            metricas.put("tasaOcupacion", Math.round(tasaOcupacion));
            
            // Calcular variaciones vs mes anterior
            LocalDate mesAnteriorDesde = fechaDesde.minusMonths(1);
            LocalDate mesAnteriorHasta = fechaHasta.minusMonths(1);
            
            List<Cita> citasMesAnterior = citaService.findByFechaBetween(
                mesAnteriorDesde.atStartOfDay(),
                mesAnteriorHasta.atTime(23, 59, 59)
            );
            
            // Variación de consultas
            int variacionConsultas = citasMesAnterior.size() > 0 
                ? (int) Math.round(((citas.size() - citasMesAnterior.size()) * 100.0) / citasMesAnterior.size())
                : 0;
            metricas.put("variacionConsultas", variacionConsultas);
            
            // Variación de ingresos
            double ingresosMesAnterior = citasMesAnterior.stream()
                    .filter(c -> c.getEstado() == Cita.EstadoCita.Atendida
                            && c.getServicio() != null 
                            && c.getServicio().getPrecio() != null)
                    .mapToDouble(c -> c.getServicio().getPrecio())
                    .sum();
            int variacionIngresos = ingresosMesAnterior > 0
                ? (int) Math.round(((ingresos - ingresosMesAnterior) * 100.0) / ingresosMesAnterior)
                : 0;
            metricas.put("variacionIngresos", variacionIngresos);
            
            // Variación de mascotas atendidas
            long mascotasMesAnterior = citasMesAnterior.stream()
                    .filter(c -> c.getMascota() != null)
                    .map(c -> c.getMascota().getId())
                    .distinct()
                    .count();
            int variacionMascotas = mascotasMesAnterior > 0
                ? (int) Math.round(((nuevasMascotas - mascotasMesAnterior) * 100.0) / mascotasMesAnterior)
                : 0;
            metricas.put("variacionMascotas", variacionMascotas);
            
            return ResponseEntity.ok(metricas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener métricas: " + e.getMessage()));
        }
    }

    @GetMapping("/consultas-tiempo")
    public ResponseEntity<?> getConsultasTiempo(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde.atStartOfDay(), hasta.atTime(23, 59, 59)) : 
                citaService.listAll();
                
            Map<String, Object> resultado = new HashMap<>();
            
            // Agrupar citas por día y calcular consultas e ingresos
            Map<String, Long> citasPorDia = citas.stream()
                    .filter(c -> c.getFecha() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getFecha().toLocalDate().toString(),
                            Collectors.counting()
                    ));
            
            // Calcular ingresos por día (solo citas atendidas)
            Map<String, Double> ingresosPorDia = citas.stream()
                    .filter(c -> c.getFecha() != null 
                            && c.getEstado() == Cita.EstadoCita.Atendida
                            && c.getServicio() != null 
                            && c.getServicio().getPrecio() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getFecha().toLocalDate().toString(),
                            Collectors.summingDouble(c -> c.getServicio().getPrecio())
                    ));
            
            // Ordenar las fechas
            List<String> fechasOrdenadas = citasPorDia.keySet().stream()
                    .sorted()
                    .collect(Collectors.toList());
            
            // Crear arrays de consultas e ingresos en el mismo orden que las fechas
            List<Long> consultasOrdenadas = fechasOrdenadas.stream()
                    .map(fecha -> citasPorDia.getOrDefault(fecha, 0L))
                    .collect(Collectors.toList());
            
            List<Double> ingresosOrdenados = fechasOrdenadas.stream()
                    .map(fecha -> ingresosPorDia.getOrDefault(fecha, 0.0))
                    .collect(Collectors.toList());
            
            resultado.put("fechas", fechasOrdenadas);
            resultado.put("consultas", consultasOrdenadas);
            resultado.put("ingresos", ingresosOrdenados);
            resultado.put("total", citas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener consultas por tiempo: " + e.getMessage()));
        }
    }

    @GetMapping("/tipos-consulta")
    public ResponseEntity<?> getTiposConsulta(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde.atStartOfDay(), hasta.atTime(23, 59, 59)) : 
                citaService.listAll();
                
            // Mapear servicios a categorías (basado en los 8 servicios reales de MySQL)
            Map<String, Long> categorias = new HashMap<>();
            categorias.put("CONSULTA_GENERAL", 0L);
            categorias.put("CONSULTA_ESPECIALIZADA", 0L);
            categorias.put("VACUNACION", 0L);
            categorias.put("DESPARASITACION", 0L);
            categorias.put("CIRUGIA", 0L);
            categorias.put("RADIOGRAFIA", 0L);
            categorias.put("BANO_CORTE", 0L);
            categorias.put("LABORATORIO", 0L);
            
            citas.stream()
                    .filter(c -> c.getServicio() != null && c.getServicio().getNombre() != null)
                    .forEach(c -> {
                        String nombreServicio = c.getServicio().getNombre().toUpperCase();
                        
                        // Mapear servicios exactos de la base de datos
                        if (nombreServicio.contains("CONSULTA GENERAL")) {
                            categorias.put("CONSULTA_GENERAL", categorias.get("CONSULTA_GENERAL") + 1);
                        } else if (nombreServicio.contains("CONSULTA ESPECIALIZADA")) {
                            categorias.put("CONSULTA_ESPECIALIZADA", categorias.get("CONSULTA_ESPECIALIZADA") + 1);
                        } else if (nombreServicio.contains("VACUN")) {
                            categorias.put("VACUNACION", categorias.get("VACUNACION") + 1);
                        } else if (nombreServicio.contains("DESPARASIT")) {
                            categorias.put("DESPARASITACION", categorias.get("DESPARASITACION") + 1);
                        } else if (nombreServicio.contains("CIRUG")) {
                            categorias.put("CIRUGIA", categorias.get("CIRUGIA") + 1);
                        } else if (nombreServicio.contains("RADIOGRAF")) {
                            categorias.put("RADIOGRAFIA", categorias.get("RADIOGRAFIA") + 1);
                        } else if (nombreServicio.contains("BAÑO") || nombreServicio.contains("CORTE")) {
                            categorias.put("BANO_CORTE", categorias.get("BANO_CORTE") + 1);
                        } else if (nombreServicio.contains("LABORATORIO") || nombreServicio.contains("ANALISIS")) {
                            categorias.put("LABORATORIO", categorias.get("LABORATORIO") + 1);
                        }
                    });
            
            // Crear resultado con tipos específicos
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("CONSULTA_GENERAL", categorias.get("CONSULTA_GENERAL"));
            resultado.put("CONSULTA_ESPECIALIZADA", categorias.get("CONSULTA_ESPECIALIZADA"));
            resultado.put("VACUNACION", categorias.get("VACUNACION"));
            resultado.put("DESPARASITACION", categorias.get("DESPARASITACION"));
            resultado.put("CIRUGIA", categorias.get("CIRUGIA"));
            resultado.put("RADIOGRAFIA", categorias.get("RADIOGRAFIA"));
            resultado.put("BANO_CORTE", categorias.get("BANO_CORTE"));
            resultado.put("LABORATORIO", categorias.get("LABORATORIO"));
            resultado.put("total", citas.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener tipos de consulta: " + e.getMessage()));
        }
    }

    @GetMapping("/top-veterinarios")
    public ResponseEntity<?> getTopVeterinarios(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde.atStartOfDay(), hasta.atTime(23, 59, 59)) : 
                citaService.listAll();
                
            // Agrupar por veterinario real
            Map<String, Long> veterinarios = citas.stream()
                    .filter(c -> c.getVeterinario() != null && c.getVeterinario().getNombre() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getVeterinario().getNombre(),
                            Collectors.counting()
                    ));
            
            // Si no hay veterinarios asignados, retornar lista vacía
            if (veterinarios.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            
            // Ordenar por cantidad de consultas (descendente) y limitar a top 5
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            // Obtener citas del período
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde.atStartOfDay(), hasta.atTime(23, 59, 59)) : 
                citaService.listAll();
            
            // Contar consultas por especie de mascota
            Map<String, Long> especiesCount = citas.stream()
                    .filter(c -> c.getMascota() != null 
                            && c.getMascota().getEspecie() != null 
                            && !c.getMascota().getEspecie().trim().isEmpty())
                    .collect(Collectors.groupingBy(
                            c -> c.getMascota().getEspecie(),
                            Collectors.counting()
                    ));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("especies", especiesCount.keySet());
            resultado.put("consultas", especiesCount.values());
            resultado.put("total", citas.size());
            
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
            LocalDate hoy = LocalDate.now();
            LocalDate dentroDe30Dias = hoy.plusDays(30);
            
            Map<String, Object> stats = new HashMap<>();
            
            // Stock crítico (productos con stock bajo o sin stock)
            int stockCritico = productoService.findProductosConBajoStock(5).size();
            stats.put("stockCritico", stockCritico);
            
            // Productos por vencer en 30 días
            int productosVencer = (int) productos.stream()
                    .filter(p -> p.getFechaVencimiento() != null 
                            && !p.getFechaVencimiento().isBefore(hoy)
                            && !p.getFechaVencimiento().isAfter(dentroDe30Dias))
                    .count();
            stats.put("productosVencer", productosVencer);
            
            // Valor total del inventario
            double valorInventario = productos.stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();
            stats.put("valorInventario", valorInventario);
            
            // Movimientos del mes actual (si existe la tabla movimiento_inventario)
            try {
                LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();
                LocalDateTime finMes = hoy.atTime(23, 59, 59);
                long movimientosMes = movimientoInventarioRepository
                        .findByFechaBetween(inicioMes, finMes)
                        .size();
                stats.put("movimientosMes", movimientosMes);
            } catch (Exception e) {
                stats.put("movimientosMes", 0);
            }
            
            // Datos adicionales
            stats.put("totalProductos", productos.size());
            stats.put("stockTotal", productos.stream()
                    .mapToInt(p -> p.getStock() != null ? p.getStock() : 0)
                    .sum());
            
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
            
            // Inicializar array de 12 meses con 0
            double[] ingresosMensuales = new double[12];
            
            // Sumar ingresos por mes (solo del año actual)
            int anioActual = LocalDate.now().getYear();
            citas.stream()
                    .filter(c -> c.getFecha() != null 
                            && c.getFecha().getYear() == anioActual
                            && c.getServicio() != null 
                            && c.getServicio().getPrecio() != null
                            && c.getEstado() != null 
                            && c.getEstado() == Cita.EstadoCita.Atendida)
                    .forEach(c -> {
                        int mes = c.getFecha().getMonthValue() - 1; // 0-11
                        ingresosMensuales[mes] += c.getServicio().getPrecio();
                    });
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("ingresos", ingresosMensuales);
            resultado.put("anio", anioActual);
            resultado.put("total", java.util.Arrays.stream(ingresosMensuales).sum());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener ingresos mensuales: " + e.getMessage()));
        }
    }

    @GetMapping("/consultas-detalle")
    public ResponseEntity<?> getConsultasDetalle(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            List<Cita> citas = (desde != null && hasta != null) ? 
                citaService.findByFechaBetween(desde.atStartOfDay(), hasta.atTime(23, 59, 59)) : 
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
                        consulta.put("veterinario", cita.getVeterinario() != null ? cita.getVeterinario().getNombre() : "N/A");
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

    @GetMapping("/movimientos-inventario")
    public ResponseEntity<?> getMovimientosInventario(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        try {
            // Convertir fechas a LocalDateTime para incluir todo el día
            LocalDateTime fechaDesde = desde != null ? desde.atStartOfDay() : LocalDateTime.now().minusMonths(1);
            LocalDateTime fechaHasta = hasta != null ? hasta.atTime(23, 59, 59) : LocalDateTime.now();
            
            // Consultar movimientos reales desde la base de datos
            List<MovimientoInventario> movimientosDB = movimientoInventarioRepository
                    .findByFechaBetween(fechaDesde, fechaHasta);
            
            // Convertir entidades a Maps para la respuesta JSON
            List<Map<String, Object>> movimientos = movimientosDB.stream()
                    .map(m -> {
                        Map<String, Object> mov = new HashMap<>();
                        mov.put("id", m.getId());
                        mov.put("fecha", m.getFecha().toLocalDate().toString());
                        mov.put("producto", m.getProducto() != null ? m.getProducto().getNombre() : "N/A");
                        mov.put("tipo", m.getTipo().name());
                        mov.put("cantidad", m.getCantidad());
                        mov.put("stockAnterior", m.getStockAnterior());
                        mov.put("stockActual", m.getStockActual());
                        mov.put("motivo", m.getMotivo() != null ? m.getMotivo() : "");
                        mov.put("usuario", m.getUsuario() != null ? m.getUsuario() : "N/A");
                        return mov;
                    })
                    .collect(Collectors.toList());
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("movimientos", movimientos);
            resultado.put("total", movimientos.size());
            resultado.put("periodo", Map.of(
                    "desde", fechaDesde.toLocalDate().toString(),
                    "hasta", fechaHasta.toLocalDate().toString()
            ));
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener movimientos de inventario: " + e.getMessage()));
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
