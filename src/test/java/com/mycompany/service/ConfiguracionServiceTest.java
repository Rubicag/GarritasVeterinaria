package com.mycompany.service;

import com.mycompany.model.Configuracion;
import com.mycompany.repository.ConfiguracionRepository;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfiguracionServiceTest {

    @Mock
    private ConfiguracionRepository repo;

    @InjectMocks
    private ConfiguracionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReturnsDefaultWhenMissing() {
        when(repo.findByClave("site.name")).thenReturn(Optional.empty());
        String v = service.get("site.name", "Default");
        assertEquals("Default", v);
    }

    @Test
    void saveCreatesOrUpdates() {
        when(repo.findByClave("k")).thenReturn(Optional.empty());
        Configuracion saved = new Configuracion("k","v");
        when(repo.save(any())).thenReturn(saved);

        service.save("k","v");

        verify(repo, times(1)).save(any(Configuracion.class));
    }

    @Test
    void getAllReturnsMap() {
        Configuracion c1 = new Configuracion("a","1");
        c1.setId(1L);
        Configuracion c2 = new Configuracion("b","2");
        c2.setId(2L);
        when(repo.findAll()).thenReturn(List.of(c1,c2));

        Map<String,String> all = service.getAll();
        assertEquals(2, all.size());
        assertEquals("1", all.get("a"));
    }
}
