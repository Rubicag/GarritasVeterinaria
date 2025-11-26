package com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración separada para el PasswordEncoder para evitar dependencias circulares
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
           // Solo para desarrollo: texto plano
           return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}