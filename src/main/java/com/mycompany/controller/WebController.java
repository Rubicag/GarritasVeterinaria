package com.mycompany.controller;

import com.mycompany.model.Rol;
import com.mycompany.model.Usuario;
import com.mycompany.model.Cita;
import com.mycompany.service.MascotaService;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.CitaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ServicioService;
import com.mycompany.service.HistorialClinicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final ProductoService productoService;
    private final ServicioService servicioService;
    private final HistorialClinicoService historialClinicoService;

    public WebController(UsuarioService usuarioService, MascotaService mascotaService, 
                        CitaService citaService, ProductoService productoService, 
                        ServicioService servicioService, HistorialClinicoService historialClinicoService) {
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
        this.productoService = productoService;
        this.servicioService = servicioService;
        this.historialClinicoService = historialClinicoService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Agregar estadísticas para el dashboard
        model.addAttribute("totalUsuarios", usuarioService.countUsers());
        model.addAttribute("totalMascotas", mascotaService.count());
        model.addAttribute("citasHoy", citaService.findCitasFuturas().size());
        model.addAttribute("totalProductos", productoService.findAll().size());
        return "dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/usuarios")
    public String usuariosPage(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("nuevoUsuario", new Usuario());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario, 
                              RedirectAttributes redirectAttributes) {
        try {
            usuarioService.create(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/inventario")
    public String inventarioPage(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "inventario";
    }

    @GetMapping("/reportes")
    public String reportesPage(Model model) {
        // Agregar datos para reportes
        model.addAttribute("totalUsuarios", usuarioService.countUsers());
        model.addAttribute("totalMascotas", mascotaService.count());
        model.addAttribute("citasFuturas", citaService.findCitasFuturas().size());
        model.addAttribute("totalServicios", servicioService.findAll().size());
        return "reportes";
    }

    @GetMapping("/historial")
    public String historialPage(Model model) {
        // Cargar todos los registros del historial ordenados por fecha descendente
        model.addAttribute("registros", historialClinicoService.findAllOrderByFechaDesc());
        
        // Cargar todas las mascotas para el selector
        model.addAttribute("mascotas", mascotaService.findAll());
        
        // Cargar usuarios con rol VETERINARIO o ADMIN para el selector
        List<Usuario> veterinarios = usuarioService.findAll()
            .stream()
            .filter(u -> {
                Rol rol = u.getRol();
                if (rol == null) return false;
                String nombreRol = rol.getNombre().toUpperCase();
                return nombreRol.equals("VETERINARIO") || nombreRol.equals("ADMIN");
            })
            .collect(Collectors.toList());
        model.addAttribute("veterinarios", veterinarios);
        
        return "historial";
    }

    @GetMapping("/reportes/exportar")
    public ResponseEntity<byte[]> exportarReporte(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta,
            @RequestParam String formato) {
        
        try {
            // Obtener consultas del período
            List<Cita> citas = citaService.findByFechaBetween(
                desde.atStartOfDay(), 
                hasta.atTime(23, 59, 59)
            );
            
            // Generar contenido según formato
            String contenido;
            String nombreArchivo;
            MediaType mediaType;
            
            if ("pdf".equalsIgnoreCase(formato)) {
                // Generar HTML simple que se puede imprimir como PDF desde el navegador
                contenido = generarHTMLReporte(citas, desde, hasta);
                nombreArchivo = "reporte_" + desde + "_" + hasta + ".html";
                mediaType = MediaType.TEXT_HTML;
            } else if ("csv".equalsIgnoreCase(formato)) {
                contenido = generarCSVReporte(citas);
                nombreArchivo = "reporte_" + desde + "_" + hasta + ".csv";
                mediaType = MediaType.parseMediaType("text/csv");
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(contenido.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    private String generarHTMLReporte(List<Cita> citas, LocalDate desde, LocalDate hasta) {
        StringBuilder html = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        html.append("<!DOCTYPE html><html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Reporte de Consultas</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }");
        html.append("h1 { color: #333; text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #007bff; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append(".header { margin-bottom: 20px; }");
        html.append(".totales { margin-top: 30px; padding: 20px; background-color: #f8f9fa; border-radius: 5px; }");
        html.append("@media print { button { display: none; } }");
        html.append("</style></head><body>");
        
        html.append("<div class='header'>");
        html.append("<h1>Reporte de Consultas Veterinarias</h1>");
        html.append("<p><strong>Período:</strong> " + desde.format(formatter) + " - " + hasta.format(formatter) + "</p>");
        html.append("<p><strong>Fecha de generación:</strong> " + LocalDate.now().format(formatter) + "</p>");
        html.append("<button onclick='window.print()'>Imprimir / Guardar como PDF</button>");
        html.append("</div>");
        
        html.append("<table>");
        html.append("<thead><tr>");
        html.append("<th>Fecha</th>");
        html.append("<th>Mascota</th>");
        html.append("<th>Propietario</th>");
        html.append("<th>Servicio</th>");
        html.append("<th>Veterinario</th>");
        html.append("<th>Estado</th>");
        html.append("<th>Precio</th>");
        html.append("</tr></thead><tbody>");
        
        double totalIngresos = 0.0;
        
        for (Cita cita : citas) {
            html.append("<tr>");
            html.append("<td>").append(cita.getFecha().toLocalDate().format(formatter)).append("</td>");
            html.append("<td>").append(cita.getMascota() != null ? cita.getMascota().getNombre() : "N/A").append("</td>");
            html.append("<td>").append(cita.getMascota() != null && cita.getMascota().getPropietario() != null ? 
                                      cita.getMascota().getPropietario().getNombre() : "N/A").append("</td>");
            html.append("<td>").append(cita.getServicio() != null ? cita.getServicio().getNombre() : "N/A").append("</td>");
            html.append("<td>").append(cita.getVeterinario() != null ? cita.getVeterinario().getNombre() : "N/A").append("</td>");
            html.append("<td>").append(cita.getEstado() != null ? cita.getEstado() : "Pendiente").append("</td>");
            
            double precio = cita.getServicio() != null && cita.getServicio().getPrecio() != null ? 
                           cita.getServicio().getPrecio() : 0.0;
            html.append("<td>$").append(String.format("%.2f", precio)).append("</td>");
            html.append("</tr>");
            
            if (cita.getEstado() == Cita.EstadoCita.Atendida) {
                totalIngresos += precio;
            }
        }
        
        html.append("</tbody></table>");
        
        html.append("<div class='totales'>");
        html.append("<h3>Resumen</h3>");
        html.append("<p><strong>Total de consultas:</strong> ").append(citas.size()).append("</p>");
        html.append("<p><strong>Consultas atendidas:</strong> ").append(
            citas.stream().filter(c -> c.getEstado() == Cita.EstadoCita.Atendida).count()
        ).append("</p>");
        html.append("<p><strong>Total ingresos:</strong> $").append(String.format("%.2f", totalIngresos)).append("</p>");
        html.append("</div>");
        
        html.append("</body></html>");
        
        return html.toString();
    }
    
    private String generarCSVReporte(List<Cita> citas) {
        StringBuilder csv = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Encabezados
        csv.append("Fecha,Mascota,Propietario,Servicio,Veterinario,Estado,Precio\n");
        
        // Datos
        for (Cita cita : citas) {
            csv.append(cita.getFecha().format(formatter)).append(",");
            csv.append(cita.getMascota() != null ? cita.getMascota().getNombre() : "N/A").append(",");
            csv.append(cita.getMascota() != null && cita.getMascota().getPropietario() != null ? 
                      cita.getMascota().getPropietario().getNombre() : "N/A").append(",");
            csv.append(cita.getServicio() != null ? cita.getServicio().getNombre() : "N/A").append(",");
            csv.append(cita.getVeterinario() != null ? cita.getVeterinario().getNombre() : "N/A").append(",");
            csv.append(cita.getEstado() != null ? cita.getEstado() : "Pendiente").append(",");
            
            double precio = cita.getServicio() != null && cita.getServicio().getPrecio() != null ? 
                           cita.getServicio().getPrecio() : 0.0;
            csv.append(String.format("%.2f", precio)).append("\n");
        }
        
        return csv.toString();
    }
}