package com.mycompany.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    public DataInitializer() {
        // Constructor vacío - servicios comentados temporalmente
    }

    @Override
    public void run(String... args) throws Exception {
        // Temporalmente deshabilitado - usando datos SQL en su lugar
        System.out.println("DataInitializer deshabilitado - usando datos SQL en application-*.properties");
        // Datos de inicialización se cargan desde src/main/resources/data.sql
    }
}
