package com.mycompany.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPublicEndpoints() throws Exception {
        // Verificar endpoints públicos
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().isOk());
    }

    @Test
    public void testProtectedEndpointsWithoutAuth() throws Exception {
        // Verificar que los endpoints protegidos requieren autenticación
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());
        
        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isUnauthorized());
        
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminEndpointsWithAdminRole() throws Exception {
        // Verificar que un administrador puede acceder a endpoints de administrador
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAdminEndpointsWithUserRole() throws Exception {
        // Verificar que un usuario normal no puede acceder a endpoints de administrador
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
        
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserEndpointsWithUserRole() throws Exception {
        // Verificar que un usuario normal puede acceder a endpoints de usuario
        mockMvc.perform(get("/api/mascotas/mias"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/citas/mias"))
                .andExpect(status().isOk());
    }
}