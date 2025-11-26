package com.mycompany.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura y procesa errores de manera centralizada.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de Bean Validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage(),
                    (existing, replacement) -> existing
                ));
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Error de validación");
        response.put("errors", errors);
        
        logger.warn("Errores de validación: {}", errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja RuntimeExceptions genéricas
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        logger.error("Error en la aplicación: {} - URL: {}", 
                    ex.getMessage(), request.getRequestURI(), ex);
        
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        mav.addObject("path", request.getRequestURI());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return mav;
    }

    /**
     * Maneja IllegalArgumentException (validaciones de negocio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage());
        
        logger.warn("Argumento inválido: {}", ex.getMessage());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones generales no capturadas
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        logger.error("Error inesperado: {} - URL: {}", 
                    ex.getMessage(), request.getRequestURI(), ex);
        
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        mav.addObject("path", request.getRequestURI());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return mav;
    }
}
