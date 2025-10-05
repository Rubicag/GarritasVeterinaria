package com.mycompany.garritasveterinaria;

import com.mycompany.service.HealthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HealthServiceTest {
    @Test
    public void statusShouldReturnOK() {
        HealthService svc = new HealthService();
        assertEquals("OK", svc.status());
    }
}
