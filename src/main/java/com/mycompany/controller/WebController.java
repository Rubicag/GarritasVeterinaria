package com.mycompany.controller;

import com.mycompany.model.Rol;
import com.mycompany.model.Usuario;
import com.mycompany.model.Cita;
import com.mycompany.service.MascotaService;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.CitaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.InventarioService;
import com.mycompany.service.ServicioService;
import com.mycompany.service.HistorialMedicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import com.mycompany.repository.MovimientoInventarioRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.mycompany.model.HistorialMedico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final ServicioService servicioService;
    private final HistorialMedicoService historialMedicoService;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public WebController(UsuarioService usuarioService, MascotaService mascotaService, 
                        CitaService citaService, ProductoService productoService, 
                        ServicioService servicioService, HistorialMedicoService historialMedicoService,
                        InventarioService inventarioService, MovimientoInventarioRepository movimientoInventarioRepository) {
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
        this.productoService = productoService;
        this.servicioService = servicioService;
        this.historialMedicoService = historialMedicoService;
        this.inventarioService = inventarioService;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
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

    // Eliminado método duplicado de login para evitar conflicto de mapeo

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

    @GetMapping("/inventario/{id}/editar")
    public String editarProductoForm(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var opt = productoService.getById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/inventario";
        }
        model.addAttribute("producto", opt.get());
        return "producto_editar";
    }

    @GetMapping("/inventario/{id}/historial")
    public String inventarioHistorial(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var opt = productoService.getById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/inventario";
        }
        model.addAttribute("producto", opt.get());

        // Obtener movimientos reales del repositorio (orden descendente por fecha)
        var movimientos = movimientoInventarioRepository.findByProductoOrderByFechaDesc(opt.get());
        model.addAttribute("movimientos", movimientos);

        return "inventario_historial";
    }

    @PostMapping("/inventario/{id}/editar")
    public String editarProductoSubmit(@org.springframework.web.bind.annotation.PathVariable Long id,
                                       @ModelAttribute("producto") com.mycompany.model.Producto productoForm,
                                       RedirectAttributes redirectAttributes) {
        try {
            var updated = productoService.update(id, productoForm);
            if (updated == null) {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado o no se pudo actualizar");
            } else {
                redirectAttributes.addFlashAttribute("success", "Producto actualizado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/inventario")
    public String crearProducto(@RequestParam String codigo,
                                @RequestParam String nombre,
                                @RequestParam(required = false) String descripcion,
                                @RequestParam(required = false) String categoria,
                                @RequestParam(required = false) String unidadMedida,
                                @RequestParam Integer stock,
                                @RequestParam Integer stockMinimo,
                                @RequestParam Double precio,
                                @RequestParam(required = false) String proveedor,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") java.time.LocalDate fechaVencimiento,
                                @RequestParam(required = false, defaultValue = "true") boolean activo,
                                RedirectAttributes redirectAttributes) {
        try {
            com.mycompany.model.Producto p = new com.mycompany.model.Producto();
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setCategoria(categoria);
            p.setPrecio(precio);
            p.setStock(stock);
            p.setProveedor(proveedor);
            p.setFechaVencimiento(fechaVencimiento);
            p.setActivo(activo);
            productoService.create(p);
            redirectAttributes.addFlashAttribute("success", "Producto creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/inventario/movimiento")
    public String registrarMovimiento(@RequestParam Long productoId,
                                      @RequestParam String tipo,
                                      @RequestParam Integer cantidad,
                                      @RequestParam(required = false) String motivo,
                                      RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> res;
            if ("ENTRADA".equalsIgnoreCase(tipo)) {
                res = inventarioService.registrarEntrada(productoId, cantidad, motivo, "Sistema");
            } else if ("SALIDA".equalsIgnoreCase(tipo)) {
                res = inventarioService.registrarSalida(productoId, cantidad, motivo, "Sistema");
            } else if ("AJUSTE".equalsIgnoreCase(tipo)) {
                // Para ajuste usamos ajustarStock: cantidad es el nuevo stock
                res = inventarioService.ajustarStock(productoId, cantidad, motivo, "Sistema");
            } else {
                redirectAttributes.addFlashAttribute("error", "Tipo de movimiento inválido");
                return "redirect:/inventario";
            }

            if (Boolean.TRUE.equals(res.get("success"))) {
                redirectAttributes.addFlashAttribute("success", res.get("message"));
            } else {
                redirectAttributes.addFlashAttribute("error", res.get("message"));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar movimiento: " + e.getMessage());
        }
        return "redirect:/inventario";
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
        model.addAttribute("registros", historialMedicoService.listarTodos());
        
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

    @GetMapping("/historial/{id}/imprimir")
    public ResponseEntity<String> imprimirHistorial(@org.springframework.web.bind.annotation.PathVariable Long id) {
        var opt = historialMedicoService.obtenerPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        HistorialMedico h = opt.get();

        StringBuilder html = new StringBuilder();
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        html.append("<!doctype html><html><head><meta charset=\"utf-8\"><title>Registro Médico " ).append(h.getId()).append("</title>");
        html.append("<style>body{font-family:Arial, sans-serif;margin:20px;}h1{color:#333;}table{width:100%;border-collapse:collapse;}th,td{padding:8px;border:1px solid #ddd;text-align:left;}@media print{button{display:none}}</style>");
        html.append("</head><body>");
        html.append("<h1>Registro Médico #").append(h.getId()).append("</h1>");
        html.append("<p><strong>Fecha registro:</strong> ").append(h.getFechaRegistro() != null ? h.getFechaRegistro().format(dtf) : "N/A").append("</p>");
        html.append("<p><strong>Fecha consulta:</strong> ").append(h.getFechaConsulta() != null ? h.getFechaConsulta().format(dtf) : "N/A").append("</p>");
        html.append("<table>");
        html.append("<tr><th>Tipo</th><td>").append(h.getTipoConsulta() != null ? h.getTipoConsulta().name() : "N/A").append("</td></tr>");
        html.append("<tr><th>Diagnóstico</th><td>").append(h.getDiagnostico() != null ? h.getDiagnostico() : "").append("</td></tr>");
        html.append("<tr><th>Tratamiento</th><td>").append(h.getTratamiento() != null ? h.getTratamiento() : "").append("</td></tr>");
        html.append("<tr><th>Medicamentos</th><td>").append(h.getMedicamentos() != null ? h.getMedicamentos() : "").append("</td></tr>");
        html.append("<tr><th>Observaciones</th><td>").append(h.getObservaciones() != null ? h.getObservaciones() : "").append("</td></tr>");
        html.append("</table>");
        if (h.getNombreArchivo() != null && h.getTipoArchivo() != null) {
            html.append("<p><strong>Archivo adjunto:</strong> <a href=\"/api/historial-medico/" ).append(h.getId()).append("/archivo\">Descargar</a></p>");
        }
        html.append("<p><button onclick=\"window.print()\">Imprimir / Guardar como PDF</button></p>");
        html.append("</body></html>");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return ResponseEntity.ok().headers(headers).body(html.toString());
    }

    @GetMapping("/historial/{id}/pdf")
    public ResponseEntity<String> pdfHistorial(@org.springframework.web.bind.annotation.PathVariable Long id) {
        // Por simplicidad devolvemos la misma página que la vista de impresión; el navegador puede guardar como PDF
        return imprimirHistorial(id);
    }

    @PostMapping("/historial")
    public String crearRegistroHistorial(
            @RequestParam(name = "mascotaId") Long mascotaId,
            @RequestParam(name = "veterinarioId", required = false) Long veterinarioId,
            @RequestParam(name = "tipoConsulta", required = false) String tipoConsulta,
            @RequestParam(name = "fechaConsulta", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime fechaConsulta,
            @RequestParam(name = "motivoConsulta", required = false) String motivoConsulta,
            @RequestParam(name = "diagnostico") String diagnostico,
            @RequestParam(name = "tratamiento", required = false) String tratamiento,
            @RequestParam(name = "medicamentos", required = false) String medicamentos,
            @RequestParam(name = "observaciones", required = false) String observaciones,
            @RequestParam(name = "costo", required = false) Double costo,
            @RequestParam(name = "estado", required = false) String estado,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Buscar o crear un servicio por defecto para asociar a la cita (se requiere servicio en CitaService.create)
            Long servicioId = null;
            var servicios = servicioService.findAll();
            if (!servicios.isEmpty()) {
                servicioId = servicios.get(0).getId();
            } else {
                // Crear servicio por defecto
                com.mycompany.model.Servicio sv = new com.mycompany.model.Servicio();
                sv.setNombre("Consulta General");
                sv.setDescripcion("Servicio por defecto creado automáticamente");
                sv.setPrecio(0.0);
                var creado = servicioService.create(sv);
                servicioId = creado.getId();
            }

            // Construir una cita mínima y guardarla
            com.mycompany.model.Cita cita = new com.mycompany.model.Cita();
            if (fechaConsulta != null) cita.setFecha(fechaConsulta);
            cita.setObservaciones(motivoConsulta);

            var citaGuardada = citaService.create(cita, mascotaId, servicioId);

            // Crear historial asociado a la cita recién creada
            historialMedicoService.crear(citaGuardada.getId(), diagnostico, tratamiento != null ? tratamiento : "", null, null);

            redirectAttributes.addFlashAttribute("success", "Registro médico creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear registro: " + e.getMessage());
        }
        return "redirect:/historial";
    }

    @GetMapping("/mascotas")
    public String mascotasPage(Model model) {
        // Cargar todas las mascotas para la vista de lista
        model.addAttribute("mascotas", mascotaService.findAll());
        return "mascotas";
    }

    @GetMapping("/mascotas/nueva")
    public String nuevaMascotaForm(Model model) {
        model.addAttribute("mascota", new com.mycompany.model.Mascota());
        // En la vista necesitamos la lista de propietarios (usuarios)
        model.addAttribute("propietarios", usuarioService.findAll());
        // Enum Sexo
        model.addAttribute("sexos", com.mycompany.model.Mascota.Sexo.values());
        return "mascotas_nueva";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuarioForm(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var opt = usuarioService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", opt.get());
        // roles opcionales para el select; si la app tiene una entidad Rol más completa, reemplazar por servicio
        model.addAttribute("roles", java.util.List.of("USER", "VETERINARIO", "ADMIN"));
        return "usuario_editar";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String editarUsuarioSubmit(@org.springframework.web.bind.annotation.PathVariable Long id,
                                      @ModelAttribute("usuario") com.mycompany.model.Usuario usuarioForm,
                                      RedirectAttributes redirectAttributes) {
        try {
            var existingOpt = usuarioService.findById(id);
            if (existingOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/usuarios";
            }
            // preservar rol existente (no permitir cambio de rol desde este formulario)
            usuarioForm.setRol(existingOpt.get().getRol());
            usuarioService.update(id, usuarioForm);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/mascotas")
    public String crearMascota(@ModelAttribute("mascota") com.mycompany.model.Mascota mascota,
                               @RequestParam(value = "propietarioId", required = true) Long propietarioId,
                               RedirectAttributes redirectAttributes) {
        try {
            mascotaService.create(mascota, propietarioId);
            redirectAttributes.addFlashAttribute("success", "Mascota creada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mascotas";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        System.out.println("LoginPage method called with error=" + error + ", logout=" + logout);
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }
        return "login";
    }

    @GetMapping("/favicon.ico")
    public org.springframework.http.ResponseEntity<Void> favicon() {
        // Evitar 403/404 en el navegador respondiendo No Content si no hay favicon en /static
        return org.springframework.http.ResponseEntity.noContent().build();
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