package com.mycompany.garritasveterinaria;

import com.mycompany.service.UsuarioService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {
    @Test
    public void initialUserCountIsZero() {
        UsuarioService svc = new UsuarioService(null);
        assertEquals(0, svc.countUsers());
    }
}
