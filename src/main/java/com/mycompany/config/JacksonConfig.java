package com.mycompany.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuración de Jackson para manejar proxies de Hibernate (lazy loading) y tipos Java 8 date/time.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        
        // Registrar módulo Hibernate para manejar proxies lazy
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        
        // Configuración: no forzar carga de lazy entities
        // Serializa solo lo que ya está cargado
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        
        // Serializar proxies no inicializados como null en vez de lanzar error
        hibernate6Module.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
        
        // Registrar módulo para tipos Java 8 date/time (LocalDate, LocalDateTime, etc.)
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        builder.modules(hibernate6Module, javaTimeModule);
        
        // Deshabilitar escritura de fechas como timestamps (usar formato ISO-8601)
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return builder;
    }
}
