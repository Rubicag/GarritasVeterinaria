package com.mycompany.controller;

import com.mycompany.repository.InMemoryUsuarioRepository;
import com.mycompany.service.UsuarioService;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet mínimo que expone endpoints para usuarios.
 * GET /api/usuarios/count -> { "count": <n> }
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = "/api/usuarios/*")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService;

    public UsuarioServlet() {
        // Inyección manual: para una app real, use CDI o Spring
        this.usuarioService = new UsuarioService(new InMemoryUsuarioRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // may be /count
        if (path == null || path.equals("/") || path.equals("/count")) {
            int count = usuarioService.countUsers();
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(String.format("{\"count\": %d}", count));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
