package com.mycompany.garritasveterinaria;

import com.mycompany.service.HealthService;
import com.mycompany.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests completos para HealthService (usando métodos reales)
 * Valida estado del sistema y conectividad
 */
@DisplayName("HealthService Tests")
public class HealthServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData metaData;

    private HealthService healthService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        healthService = new HealthService();
        
        // Inyectar dependencias usando reflexión para simular @Autowired
        injectDependencies();
        
        // Configurar mocks básicos
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(connection.isValid(anyInt())).thenReturn(true);
        when(metaData.getURL()).thenReturn("jdbc:h2:mem:testdb");
        when(metaData.getDriverName()).thenReturn("H2 JDBC Driver");
        when(metaData.getDriverVersion()).thenReturn("2.1.214");
    }

    @Nested
    @DisplayName("Basic Health Check Tests")
    class BasicHealthCheckTests {

        @Test
        @DisplayName("Status básico debe retornar OK")
        public void basicStatusShouldReturnOK() {
            String status = healthService.status();
            assertEquals("OK", status);
        }

        @Test
        @DisplayName("Health check completo debe funcionar")
        public void fullHealthCheckShouldWork() throws Exception {
            when(usuarioService.countUsers()).thenReturn(5);

            Map<String, Object> health = healthService.healthCheck();

            assertNotNull(health);
            assertEquals("UP", health.get("status"));
            assertNotNull(health.get("timestamp"));
            assertNotNull(health.get("database"));
            assertNotNull(health.get("services"));
            assertNotNull(health.get("system"));
        }
    }

    @Nested
    @DisplayName("Database Health Tests")
    class DatabaseHealthTests {

        @Test
        @DisplayName("Verificación de base de datos debe funcionar")
        public void databaseCheckShouldWork() throws Exception {
            when(usuarioService.countUsers()).thenReturn(3);

            Map<String, Object> dbHealth = healthService.checkDatabase();

            assertNotNull(dbHealth);
            assertTrue(dbHealth.containsKey("connected"));
            assertTrue(dbHealth.containsKey("status"));
            assertNotNull(dbHealth.get("timestamp"));
            verify(dataSource).getConnection();
        }

        @Test
        @DisplayName("Conexión de BD fallida debe ser manejada")
        public void failedDatabaseConnectionShouldBeHandled() throws Exception {
            when(dataSource.getConnection()).thenThrow(new RuntimeException("Connection failed"));

            Map<String, Object> dbHealth = healthService.checkDatabase();

            assertNotNull(dbHealth);
            assertEquals(false, dbHealth.get("connected"));
            assertEquals("ERROR", dbHealth.get("status"));
            assertTrue(dbHealth.get("message").toString().contains("Error al conectar"));
        }

        @Test
        @DisplayName("Conexión inválida debe ser detectada")
        public void invalidConnectionShouldBeDetected() throws Exception {
            when(connection.isValid(anyInt())).thenReturn(false);

            Map<String, Object> dbHealth = healthService.checkDatabase();

            assertNotNull(dbHealth);
            assertEquals(false, dbHealth.get("connected"));
            assertEquals("UNHEALTHY", dbHealth.get("status"));
        }
    }

    @Nested
    @DisplayName("System Info Tests")
    class SystemInfoTests {

        @Test
        @DisplayName("Información del sistema debe ser completa")
        public void systemInfoShouldBeComplete() {
            Map<String, Object> systemInfo = healthService.getSystemInfo();

            assertNotNull(systemInfo);
            assertTrue(systemInfo.containsKey("memory"));
            assertTrue(systemInfo.containsKey("processors"));
            assertTrue(systemInfo.containsKey("os"));
            assertTrue(systemInfo.containsKey("java"));
            assertTrue(systemInfo.containsKey("user"));
            assertNotNull(systemInfo.get("timestamp"));
        }

        @Test
        @DisplayName("Información de memoria debe estar presente")
        public void memoryInfoShouldBePresent() {
            Map<String, Object> systemInfo = healthService.getSystemInfo();
            
            @SuppressWarnings("unchecked")
            Map<String, Object> memory = (Map<String, Object>) systemInfo.get("memory");
            
            assertNotNull(memory);
            assertTrue(memory.containsKey("max"));
            assertTrue(memory.containsKey("total"));
            assertTrue(memory.containsKey("free"));
            assertTrue(memory.containsKey("used"));
        }
    }

    @Nested
    @DisplayName("Ping Tests")
    class PingTests {

        @Test
        @DisplayName("Ping debe responder correctamente")
        public void pingShouldRespondCorrectly() {
            Map<String, Object> pingResult = healthService.ping();

            assertNotNull(pingResult);
            assertEquals("pong", pingResult.get("message"));
            assertEquals("alive", pingResult.get("status"));
            assertNotNull(pingResult.get("timestamp"));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Health check con servicios funcionando debe ser exitoso")
        public void healthCheckWithWorkingServicesShouldSucceed() throws Exception {
            when(usuarioService.countUsers()).thenReturn(10);

            Map<String, Object> health = healthService.healthCheck();

            assertNotNull(health);
            assertEquals("UP", health.get("status"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> services = (Map<String, Object>) health.get("services");
            assertEquals("UP", services.get("status"));
            assertTrue(services.get("userService").toString().contains("10 usuarios"));
        }

        @Test
        @DisplayName("Health check con servicio fallando debe ser manejado")
        public void healthCheckWithFailingServiceShouldBeHandled() throws Exception {
            when(usuarioService.countUsers()).thenThrow(new RuntimeException("Service error"));

            Map<String, Object> health = healthService.healthCheck();

            assertNotNull(health);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> services = (Map<String, Object>) health.get("services");
            assertEquals("DOWN", services.get("status"));
            assertTrue(services.get("userService").toString().contains("Error"));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Errores de BD deben ser capturados gracefully")
        public void databaseErrorsShouldBeCaughtGracefully() throws Exception {
            when(connection.isValid(anyInt())).thenThrow(new RuntimeException("Database timeout"));

            assertDoesNotThrow(() -> {
                Map<String, Object> dbHealth = healthService.checkDatabase();
                assertNotNull(dbHealth);
            });
        }

        @Test
        @DisplayName("Métodos básicos no deben fallar nunca")
        public void basicMethodsShouldNeverFail() {
            assertDoesNotThrow(() -> {
                healthService.status();
                healthService.getSystemInfo();
                healthService.ping();
            });
        }
    }

    /**
     * Inyecta dependencias simulando @Autowired usando reflexión
     */
    private void injectDependencies() {
        try {
            java.lang.reflect.Field dataSourceField = HealthService.class.getDeclaredField("dataSource");
            dataSourceField.setAccessible(true);
            dataSourceField.set(healthService, dataSource);

            java.lang.reflect.Field usuarioServiceField = HealthService.class.getDeclaredField("usuarioService");
            usuarioServiceField.setAccessible(true);
            usuarioServiceField.set(healthService, usuarioService);
        } catch (Exception e) {
            throw new RuntimeException("Error inyectando dependencias en test", e);
        }
    }
}