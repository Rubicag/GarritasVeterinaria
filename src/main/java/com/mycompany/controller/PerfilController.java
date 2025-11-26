package com.mycompany.controller;

import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import java.security.Principal;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public String perfil(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
            Optional<Usuario> u = usuarioService.findByUsername(username);
            u.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
        return "perfil";
    }
}
