package com.mycompany.controller;

import com.mycompany.service.ConfiguracionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfiguracionController.class)
@Import(ConfiguracionControllerTest.TestConfig.class)
class ConfiguracionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfiguracionService configuracionService;

    @Test
    void getConfigPageForAnonymousShowsReadonly() throws Exception {
        when(configuracionService.get("site.name","Garritas Veterinaria")).thenReturn("MiSitio");
        when(configuracionService.get("site.maintenance","false")).thenReturn("false");
        when(configuracionService.get("site.itemsPerPage","20")).thenReturn("10");
        when(configuracionService.getAll()).thenReturn(java.util.Map.of());

        mockMvc.perform(get("/configuracion").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("siteName"))
                .andExpect(model().attribute("isAdmin", false));
    }

    @Test
    void postConfigRequiresAdmin() throws Exception {
        mockMvc.perform(post("/configuracion")
                .with(user("user").roles("USER"))
                .param("siteName","X")
                .param("itemsPerPage","10"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postConfigAsAdminSavesAndRedirects() throws Exception {
        mockMvc.perform(post("/configuracion")
                .with(user("admin").roles("ADMIN"))
                .param("siteName","X")
                .param("itemsPerPage","5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion"));

        verify(configuracionService, times(1)).save("site.name","X");
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConfiguracionService configuracionService() {
            return mock(ConfiguracionService.class);
        }
    }
}
