package com.mycompany.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API REST.
 * 
 * La documentación estará disponible en:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI veterinariaOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desarrollo");

        Server prodServer = new Server();
        prodServer.setUrl("https://garritas-veterinaria.com");
        prodServer.setDescription("Servidor de Producción");

        Contact contact = new Contact();
        contact.setEmail("info@garritas-veterinaria.com");
        contact.setName("Garritas Veterinaria");
        contact.setUrl("https://garritas-veterinaria.com");

        License mitLicense = new License()
            .name("MIT License")
            .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
            .title("API REST - Sistema Garritas Veterinaria")
            .version("1.0.0")
            .contact(contact)
            .description("API RESTful para la gestión de citas, mascotas, servicios y usuarios en Veterinaria Garritas. " +
                        "Incluye endpoints para CRUD de citas, filtros, paginación y reportes.")
            .termsOfService("https://garritas-veterinaria.com/terminos")
            .license(mitLicense);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer, prodServer));
    }
}
