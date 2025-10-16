package com.mycompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.MascotaService;
import com.mycompany.service.CitaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ReporteService;
import com.mycompany.service.ClinicaService;
import com.mycompany.service.HistorialClinicoService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador web para servir páginas HTML desde templates
 * Ahora con integración a base de datos real
 */
@Controller
public class WebViewController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MascotaService mascotaService;
    
    @Autowired
    private CitaService citaService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ReporteService reporteService;
    
    @Autowired
    private ClinicaService clinicaService;
    
    @Autowired
    private HistorialClinicoService historialClinicoService;

    /**
     * Página principal - redirige al login
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/web/login";
    }

    /**
     * Página de login
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Iniciar Sesión");
        return "login";
    }

    /**
     * Página de login web
     */
    @GetMapping("/web/login")
    public String webLogin(Model model) {
        model.addAttribute("pageTitle", "Iniciar Sesión");
        return "login";
    }

    /**
     * Página de dashboard con datos reales de la base de datos
     * Reflejando específicamente los datos de database_complete_data.sql
     */
    @GetMapping("/dashboard")  
    public String dashboard(Model model) {
        try {
            // Cargar estadísticas del dashboard desde la base de datos
            model.addAttribute("pageTitle", "Dashboard - Garritas Veterinaria");
            
            // Estadísticas principales (deben reflejar los datos de database_complete_data.sql)
            int totalUsuarios = usuarioService.countUsers();
            List<com.mycompany.model.Mascota> mascotas = mascotaService.findAll();
            List<com.mycompany.model.Cita> todasCitas = citaService.listAll();
            List<com.mycompany.model.Producto> productos = productoService.findAll();
            List<com.mycompany.model.Reporte> reportes = reporteService.findAll();
            
            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalMascotas", mascotas.size());
            model.addAttribute("totalCitas", todasCitas.size());
            model.addAttribute("citasPendientes", todasCitas.stream()
                .mapToInt(c -> "Pendiente".equals(c.getEstado().toString()) ? 1 : 0).sum());
            model.addAttribute("totalProductos", productos.size());
            model.addAttribute("totalReportes", reportes.size());
            
            // Información específica de los datos cargados
            model.addAttribute("mascotasInfo", Map.of(
                "caninos", mascotas.stream().mapToInt(m -> "Canino".equals(m.getEspecie()) ? 1 : 0).sum(),
                "felinos", mascotas.stream().mapToInt(m -> "Felino".equals(m.getEspecie()) ? 1 : 0).sum(),
                "otros", mascotas.stream().mapToInt(m -> !"Canino".equals(m.getEspecie()) && !"Felino".equals(m.getEspecie()) ? 1 : 0).sum()
            ));
            
            // Resumen de próximas citas (octubre 2025)
            model.addAttribute("proximasCitas", todasCitas.stream()
                .filter(c -> "Pendiente".equals(c.getEstado().toString()))
                .limit(5)
                .collect(java.util.stream.Collectors.toList()));
            
            // Stock de productos
            long productosConStock = productos.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .count();
            model.addAttribute("productosConStock", productosConStock);
            model.addAttribute("productosSinStock", productos.size() - productosConStock);
            
            // Información de la clínica
            model.addAttribute("clinicaInfo", clinicaService.getInformacionGeneralClinica());
            model.addAttribute("estadisticasClinica", clinicaService.getEstadisticasGenerales());
            model.addAttribute("horariosClinica", clinicaService.getHorariosAtencion());
            model.addAttribute("serviciosDisponibles", clinicaService.getServiciosDisponibles());
            
            // Debug: Mostrar información de los datos cargados
            System.out.println("=== DASHBOARD - DATOS CARGADOS ===");
            System.out.println("Usuarios: " + totalUsuarios);
            System.out.println("Mascotas: " + mascotas.size());
            System.out.println("Citas: " + todasCitas.size());
            System.out.println("Productos: " + productos.size());
            System.out.println("Reportes: " + reportes.size());
            
            // Log detallado al cargar el dashboard
            System.out.println("[INFO] Cargando datos para el dashboard...");
            System.out.println("[DEBUG] Usuarios totales: " + totalUsuarios);
            System.out.println("[DEBUG] Mascotas totales: " + mascotas.size());
            System.out.println("[DEBUG] Citas totales: " + todasCitas.size());
            System.out.println("[DEBUG] Productos totales: " + productos.size());
            System.out.println("[DEBUG] Reportes totales: " + reportes.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar dashboard: " + e.getMessage());
            e.printStackTrace();
            
            // En caso de error, mostrar valores por defecto
            model.addAttribute("pageTitle", "Dashboard - Error");
            model.addAttribute("totalUsuarios", 0);
            model.addAttribute("totalMascotas", 0);
            model.addAttribute("totalCitas", 0);
            model.addAttribute("citasPendientes", 0);
            model.addAttribute("totalProductos", 0);
            model.addAttribute("totalReportes", 0);
            model.addAttribute("error", "Error al cargar los datos del dashboard: " + e.getMessage());
        }
        return "dashboard";
    }
    
    /**
     * Página de dashboard web
     */
    @GetMapping("/web/dashboard")
    public String webDashboard(Model model) {
        return dashboard(model);
    }
    


    /**
     * Página de usuarios (web interface) con datos de BD
     */
    @GetMapping("/web/usuarios")
    public String webUsuarios(Model model) {
        try {
            model.addAttribute("pageTitle", "Gestión de Usuarios");
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("totalUsuarios", usuarioService.countUsers());
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Gestión de Usuarios");
            model.addAttribute("usuarios", java.util.Collections.emptyList());
            model.addAttribute("totalUsuarios", 0);
        }
        return "usuarios";
    }

    /**
     * Página de mascotas (web interface) con datos de BD
     * Mostrando específicamente: Firulais (Canino), Michi (Felino), Luna (Felino)
     */
    @GetMapping("/web/mascotas")
    public String webMascotas(Model model) {
        try {
            var mascotas = mascotaService.findAll();
            var usuarios = usuarioService.findAll();
            
            model.addAttribute("pageTitle", "Gestión de Mascotas");
            model.addAttribute("mascotas", mascotas);
            model.addAttribute("totalMascotas", mascotas.size());
            model.addAttribute("usuarios", usuarios); // Para seleccionar propietarios
            
            // Estadísticas de especies (basado en database_complete_data.sql)
            long caninos = mascotas.stream().filter(m -> "Canino".equals(m.getEspecie())).count();
            long felinos = mascotas.stream().filter(m -> "Felino".equals(m.getEspecie())).count();
            
            model.addAttribute("estadisticasEspecies", Map.of(
                "caninos", caninos,
                "felinos", felinos,
                "otros", mascotas.size() - caninos - felinos
            ));
            
            // Debug info - mostrar datos específicos esperados
            System.out.println("=== MASCOTAS CARGADAS ===");
            System.out.println("Total mascotas: " + mascotas.size());
            System.out.println("Caninos: " + caninos + ", Felinos: " + felinos);
            for (var mascota : mascotas) {
                System.out.println("- " + mascota.getNombre() + " (" + mascota.getEspecie() + ", " + 
                                 mascota.getRaza() + ") - Propietario: " + 
                                 (mascota.getPropietario() != null ? mascota.getPropietario().getNombre() : "N/A"));
            }
            
        } catch (Exception e) {
            System.err.println("Error al cargar mascotas: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("pageTitle", "Gestión de Mascotas");
            model.addAttribute("mascotas", java.util.Collections.emptyList());
            model.addAttribute("totalMascotas", 0);
            model.addAttribute("usuarios", java.util.Collections.emptyList());
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
        }
        return "mascotas";
    }

    /**
     * Página de citas (web interface) con datos de BD
     * Mostrando las 4 citas específicas de database_complete_data.sql (octubre 2025)
     */
    @GetMapping("/web/citas")
    public String webCitas(Model model) {
        try {
            var todasCitas = citaService.listAll();
            var mascotas = mascotaService.findAll();
            var usuarios = usuarioService.findAll();

            // Filtrar propietarios (rol propietario)
            var propietarios = usuarios.stream()
                .filter(u -> u.getRol() != null && u.getRol().getNombre().equalsIgnoreCase("PROPIETARIO"))
                .collect(java.util.stream.Collectors.toList());

            // Filtrar veterinarios (rol veterinario o admin)
            var veterinarios = usuarios.stream()
                .filter(u -> u.getRol() != null && (u.getRol().getNombre().equalsIgnoreCase("VETERINARIO") || u.getRol().getNombre().equalsIgnoreCase("ADMIN")))
                .collect(java.util.stream.Collectors.toList());

            model.addAttribute("pageTitle", "Gestión de Citas");
            model.addAttribute("citas", todasCitas);
            model.addAttribute("citasHoy", todasCitas); // Por simplicidad, mostrar todas
            model.addAttribute("mascotas", mascotas);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("propietarios", propietarios);
            model.addAttribute("veterinarios", veterinarios);

            // Estadísticas de citas (basado en database_complete_data.sql)
            long citasPendientes = todasCitas.stream()
                .filter(c -> "Pendiente".equals(c.getEstado().toString()))
                .count();

            model.addAttribute("estadisticasCitas", Map.of(
                "totalCitas", todasCitas.size(),
                "pendientes", citasPendientes,
                "atendidas", todasCitas.size() - citasPendientes
            ));

            // Debug info - mostrar citas específicas esperadas
            System.out.println("=== CITAS CARGADAS ===");
            System.out.println("Total citas: " + todasCitas.size());
            System.out.println("Citas pendientes: " + citasPendientes);
            for (var cita : todasCitas) {
                System.out.println("- Cita " + cita.getId() + ": " + 
                                 (cita.getMascota() != null ? cita.getMascota().getNombre() : "N/A") + 
                                 " - " + cita.getFecha() + " - " + cita.getEstado());
            }

        } catch (Exception e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("pageTitle", "Gestión de Citas");
            model.addAttribute("citas", java.util.Collections.emptyList());
            model.addAttribute("citasHoy", java.util.Collections.emptyList());
            model.addAttribute("mascotas", java.util.Collections.emptyList());
            model.addAttribute("usuarios", java.util.Collections.emptyList());
            model.addAttribute("propietarios", java.util.Collections.emptyList());
            model.addAttribute("veterinarios", java.util.Collections.emptyList());
            model.addAttribute("error", "Error al cargar las citas: " + e.getMessage());
        }
        return "citas";
    }

    /**
     * Página de inventario (web interface) con datos de BD
     */
    @GetMapping("/web/inventario")
    public String webInventario(Model model) {
        System.out.println("DEBUG: Iniciando carga de inventario");
        try {
            model.addAttribute("pageTitle", "Gestión de Inventario");
            System.out.println("DEBUG: Título establecido");
            
            // Cargar productos básicos con manejo de errores individual
            try {
                List<com.mycompany.model.Producto> productos = productoService.findAll();
                System.out.println("DEBUG: Productos cargados: " + productos.size());
                model.addAttribute("productos", productos);
                model.addAttribute("productosDisponibles", productos);
                
                // Productos con bajo stock
                try {
                    List<com.mycompany.model.Producto> bajoStock = productoService.findProductosConBajoStock(5);
                    System.out.println("DEBUG: Productos bajo stock: " + bajoStock.size());
                    model.addAttribute("productosBajoStock", bajoStock);
                } catch (Exception e) {
                    System.out.println("ERROR: Error al cargar productos con bajo stock: " + e.getMessage());
                    model.addAttribute("productosBajoStock", java.util.Collections.emptyList());
                }
                
                // Resumen simplificado del inventario
                Map<String, Object> resumenSimple = new HashMap<>();
                resumenSimple.put("totalProductos", productos.size());
                
                long conStock = productos.stream()
                    .filter(p -> p.getStock() != null && p.getStock() > 0)
                    .count();
                resumenSimple.put("productosConStock", conStock);
                
                long sinStock = productos.stream()
                    .filter(p -> p.getStock() == null || p.getStock() == 0)
                    .count();
                resumenSimple.put("productosSinStock", sinStock);
                
                System.out.println("DEBUG: Resumen calculado - Total: " + productos.size() + ", Con stock: " + conStock + ", Sin stock: " + sinStock);
                model.addAttribute("resumenInventario", resumenSimple);
                
                // Alertas simplificadas
                Map<String, Object> alertasSimples = new HashMap<>();
                alertasSimples.put("totalAlertas", 0); // Temporalmente en 0 para evitar problemas
                model.addAttribute("alertasInventario", alertasSimples);
                
            } catch (Exception e) {
                System.out.println("ERROR: Error al cargar productos: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("productos", java.util.Collections.emptyList());
                model.addAttribute("productosDisponibles", java.util.Collections.emptyList());
                model.addAttribute("productosBajoStock", java.util.Collections.emptyList());
                model.addAttribute("resumenInventario", new HashMap<>());
                model.addAttribute("alertasInventario", new HashMap<>());
            }
            
            System.out.println("DEBUG: Inventario cargado exitosamente");
            
        } catch (Exception e) {
            System.out.println("ERROR CRÍTICO en webInventario: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("pageTitle", "Gestión de Inventario");
            model.addAttribute("productos", java.util.Collections.emptyList());
            model.addAttribute("productosDisponibles", java.util.Collections.emptyList());
            model.addAttribute("productosBajoStock", java.util.Collections.emptyList());
            model.addAttribute("resumenInventario", new HashMap<>());
            model.addAttribute("alertasInventario", new HashMap<>());
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
        }
        return "inventario";
    }

    /**
     * Página de reportes (web interface) con datos de BD
     */
    @GetMapping("/web/reportes")
    public String webReportes(Model model) {
        try {
            model.addAttribute("pageTitle", "Reportes");
            
            List<com.mycompany.model.Reporte> reportes = reporteService.findAll();
            model.addAttribute("reportes", reportes);
            model.addAttribute("reportesRecientes", reportes);
            model.addAttribute("totalReportes", reportes.size());
            
            // Estadísticas adicionales de reportes
            model.addAttribute("reportesEsteAno", reportes.size()); // Simplificado
            model.addAttribute("reportesEsteMes", reportes.size()); // Simplificado
            
        } catch (Exception e) {
            System.err.println("Error al cargar reportes: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("pageTitle", "Reportes");
            model.addAttribute("reportes", java.util.Collections.emptyList());
            model.addAttribute("reportesRecientes", java.util.Collections.emptyList());
            model.addAttribute("totalReportes", 0);
            model.addAttribute("error", "Error al cargar los reportes: " + e.getMessage());
        }
        return "reportes";
    }

    /**
     * Página de historial (web interface) con datos de BD
     */
    @GetMapping("/web/historial")
    public String webHistorial(Model model) {
        try {
            model.addAttribute("pageTitle", "Historial Clínico");
            
            // Cargar mascotas para el formulario
            List<com.mycompany.model.Mascota> mascotas = mascotaService.findAll();
            model.addAttribute("mascotas", mascotas);
            
            // Cargar veterinarios (usuarios con rol de veterinario o admin)
            List<com.mycompany.model.Usuario> todosUsuarios = usuarioService.findAll();
            List<com.mycompany.model.Usuario> veterinarios = todosUsuarios.stream()
                .filter(u -> u.getRol() != null && 
                           (u.getRol().getNombre().equalsIgnoreCase("VETERINARIO") || 
                            u.getRol().getNombre().equalsIgnoreCase("ADMIN")))
                .collect(java.util.stream.Collectors.toList());
            
            // Si no hay veterinarios específicos, usar todos los usuarios como veterinarios temporalmente
            if (veterinarios.isEmpty()) {
                veterinarios = todosUsuarios;
            }
            model.addAttribute("veterinarios", veterinarios);
            
            // Intentar cargar registros de historial clínico
            List<com.mycompany.model.HistorialClinico> registros;
            try {
                registros = historialClinicoService.findAll();
            } catch (Exception e) {
                System.out.println("WARN: No se pudieron cargar registros de historial desde BD: " + e.getMessage());
                registros = crearRegistrosEjemplo(mascotas, veterinarios);
            }
            
            model.addAttribute("registros", registros);
            
            // Estadísticas básicas
            model.addAttribute("totalRegistros", registros.size());
            model.addAttribute("totalMascotas", mascotas.size());
            model.addAttribute("totalVeterinarios", veterinarios.size());
            
            System.out.println("DEBUG: Historial cargado - " + registros.size() + " registros, " + 
                             veterinarios.size() + " veterinarios, " + mascotas.size() + " mascotas");
            
        } catch (Exception e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("pageTitle", "Historial Clínico");
            model.addAttribute("registros", java.util.Collections.emptyList());
            model.addAttribute("mascotas", java.util.Collections.emptyList());
            model.addAttribute("veterinarios", java.util.Collections.emptyList());
            model.addAttribute("totalRegistros", 0);
            model.addAttribute("totalMascotas", 0);
            model.addAttribute("totalVeterinarios", 0);
            model.addAttribute("error", "Error al cargar los datos del historial: " + e.getMessage());
        }
        return "historial";
    }
    
    /**
     * Crea registros de ejemplo para mostrar en el historial cuando no hay datos en BD
     */
    private List<com.mycompany.model.HistorialClinico> crearRegistrosEjemplo(
            List<com.mycompany.model.Mascota> mascotas, 
            List<com.mycompany.model.Usuario> veterinarios) {
        
        List<com.mycompany.model.HistorialClinico> registros = new java.util.ArrayList<>();
        
        if (mascotas.isEmpty() || veterinarios.isEmpty()) {
            return registros; // Lista vacía si no hay datos base
        }
        
        try {
            // Crear algunos registros de ejemplo
            for (int i = 0; i < Math.min(mascotas.size(), 5); i++) {
                com.mycompany.model.HistorialClinico registro = new com.mycompany.model.HistorialClinico();
                
                registro.setId((long) (i + 1));
                registro.setMascota(mascotas.get(i % mascotas.size()));
                registro.setVeterinario(veterinarios.get(i % veterinarios.size()));
                registro.setFechaConsulta(java.time.LocalDateTime.now().minusDays(i + 1));
                
                // Tipos de consulta variados
                String[] tipos = {"CONSULTA", "VACUNACION", "CONTROL", "EMERGENCIA", "CIRUGIA"};
                registro.setTipoConsulta(tipos[i % tipos.length]);
                
                // Diagnósticos de ejemplo
                String[] diagnosticos = {
                    "Control de rutina - Mascota en buen estado general",
                    "Vacunación antirrábica aplicada correctamente",
                    "Control post-operatorio satisfactorio",
                    "Consulta por malestar estomacal - Gastritis leve",
                    "Cirugía de esterilización completada exitosamente"
                };
                registro.setDiagnostico(diagnosticos[i % diagnosticos.length]);
                
                registro.setMotivoConsulta("Motivo de consulta de ejemplo #" + (i + 1));
                registro.setTratamiento("Tratamiento recomendado según diagnóstico");
                registro.setObservaciones("Observaciones adicionales del registro " + (i + 1));
                registro.setEstado("COMPLETADO");
                registro.setCosto(50.0 + (i * 25.0));
                
                registros.add(registro);
            }
            
            System.out.println("INFO: Creados " + registros.size() + " registros de ejemplo para el historial");
        } catch (Exception e) {
            System.err.println("Error al crear registros de ejemplo: " + e.getMessage());
        }
        
        return registros;
    }
    
    // Métodos redundantes eliminados para simplificar el controlador
    // Los métodos eliminados son: usuariosWeb, mascotasWeb, citasWeb, inventarioWeb
    // Estos métodos redirigían a otros métodos ya existentes y no aportaban funcionalidad adicional
}