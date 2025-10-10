package com.mycompany.controller;

import com.mycompany.model.Usuario;
import com.mycompany.service.MascotaService;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.CitaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final ProductoService productoService;
    private final ServicioService servicioService;

    public WebController(UsuarioService usuarioService, MascotaService mascotaService, 
                        CitaService citaService, ProductoService productoService, 
                        ServicioService servicioService) {
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
        this.productoService = productoService;
        this.servicioService = servicioService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
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
        model.addAttribute("mascotas", mascotaService.findAll());
        return "historial";
    }
}