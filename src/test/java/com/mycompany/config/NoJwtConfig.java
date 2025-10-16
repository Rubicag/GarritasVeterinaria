package com.mycompany.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para desactivar JWT en tests
 */
@Configuration
@ConditionalOnProperty(name = "jwt.enabled", havingValue = "false")
public class NoJwtConfig {
    // Esta clase existe únicamente para desactivar explícitamente JWT en tests
    // Cuando jwt.enabled=false, esta configuración se activa y las configuraciones JWT se desactivan
}