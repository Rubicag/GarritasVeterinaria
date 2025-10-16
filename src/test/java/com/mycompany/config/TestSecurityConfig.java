package com.mycompany.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.annotation.Order;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        // Configuración simple para tests
        return new BCryptPasswordEncoder(4); // Usar menor complejidad para tests más rápidos
    }

    @Bean
    @Primary
    @Order(1) // Asegurar que esta configuración tenga prioridad sobre la principal
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.disable()); // Deshabilitamos completamente la gestión de sesiones

        return http.build();
    }
}