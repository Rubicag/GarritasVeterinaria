package com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.*;

/**
 * Configuración separada para el PasswordEncoder para evitar dependencias circulares
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder para desarrollo - acepta contraseñas en texto plano
        // ADVERTENCIA: Solo para desarrollo. En producción usar BCrypt
        return NoOpPasswordEncoder.getInstance();
    }
}