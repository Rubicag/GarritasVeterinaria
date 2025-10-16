package com.mycompany.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador personalizado para manejar errores
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Obtener el código de estado del error
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object requestUri = request.getAttribute("jakarta.servlet.error.request_uri");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("status", statusCode);
            
            switch (statusCode) {
                case 404:
                    model.addAttribute("error", "Página no encontrada");
                    model.addAttribute("message", "La página que buscas no existe.");
                    break;
                case 500:
                    model.addAttribute("error", "Error interno del servidor");
                    model.addAttribute("message", "Ha ocurrido un error interno.");
                    break;
                case 403:
                    model.addAttribute("error", "Acceso denegado");
                    model.addAttribute("message", "No tienes permisos para acceder a esta página.");
                    break;
                default:
                    model.addAttribute("error", "Error desconocido");
                    model.addAttribute("message", "Ha ocurrido un error inesperado.");
                    break;
            }
        } else {
            model.addAttribute("status", "Unknown");
            model.addAttribute("error", "Error desconocido");
            model.addAttribute("message", "Ha ocurrido un error inesperado.");
        }

        if (requestUri != null) {
            model.addAttribute("requestUri", requestUri.toString());
        }

        return "error";
    }
}