package com.mycompany.garritasveterinaria;

import com.mycompany.VeterinariaApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test básico para verificar que el contexto de Spring se carga correctamente
 */
@SpringBootTest(
    classes = VeterinariaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "jwt.enabled=false"
})
@DisplayName("Test Básico de Contexto")
public class BasicContextTest {

    @Test
    @DisplayName("El contexto de Spring debe cargarse correctamente")
    public void contextLoads() {
        // Test simple que verifica que el contexto de Spring se carga sin errores
        assertTrue(true, "El contexto de Spring se cargó exitosamente");
    }
    
    @Test
    @DisplayName("Test básico de configuración")
    public void basicConfigurationTest() {
        assertNotNull(System.getProperty("java.version"));
           assertTrue(System.getProperty("java.version").startsWith("24"));
    }
}