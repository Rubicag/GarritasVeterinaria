package com.mycompany.garritasveterinaria;

import com.mycompany.repository.UsuarioRepository;
import com.mycompany.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {
    @Test
    public void initialUserCountIsZero() {
        // Use a Mockito mock instead of passing null to avoid NPE
        UsuarioRepository repo = Mockito.mock(UsuarioRepository.class);
        Mockito.when(repo.count()).thenReturn(0);

        UsuarioService svc = new UsuarioService(repo);
        assertEquals(0, svc.countUsers());
    }
}
