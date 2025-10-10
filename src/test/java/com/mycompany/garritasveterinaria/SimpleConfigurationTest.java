package com.mycompany.garritasveterinaria;

import com.mycompany.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestSecurityConfig.class)
@ActiveProfiles("h2")
public class SimpleConfigurationTest {

    @Test
    public void contextLoads() {
        // Este test simplemente verifica que el contexto de Spring se carga correctamente
        assertTrue(true, "El contexto de Spring se cargó correctamente");
    }
}