package com.mycompany.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = "jwt.enabled", havingValue = "false")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Ruta personalizada para la página de inicio de sesión
                        .defaultSuccessUrl("/dashboard", true) // Redirigir al dashboard después del login exitoso
                        .failureUrl("/login?error=true") // Redirigir a /login con parámetro error si falla
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll())
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}