package com.mycompany.garritasveterinaria;

import com.mycompany.repository.UsuarioRepository;
import com.mycompany.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceMockitoTest {
    @Test
    public void countUsersDelegatesToRepository() {
        UsuarioRepository repo = Mockito.mock(UsuarioRepository.class);
        Mockito.when(repo.count()).thenReturn(5);

        UsuarioService svc = new UsuarioService(repo);
        assertEquals(5, svc.countUsers());

        Mockito.verify(repo).count();
    }
}
