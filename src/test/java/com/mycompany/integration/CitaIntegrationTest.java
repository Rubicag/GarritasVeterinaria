package com.mycompany.integration;

import com.mycompany.VeterinariaApplication;
import com.mycompany.config.TestSecurityConfig;
import com.mycompany.dto.CitaRequestDTO;
import com.mycompany.dto.CitaResponseDTO;
import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.model.Usuario;
import com.mycompany.repository.CitaRepository;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import com.mycompany.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para el módulo de Citas.
 * Prueba los endpoints REST end-to-end con base de datos H2.
 */
@SpringBootTest(
    classes = VeterinariaApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Tests de Integración - API de Citas")
public class CitaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    private String baseUrl;
    private Usuario propietario;
    private Usuario veterinario;
    private Mascota mascota;
    private Servicio servicio;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/citas";
        
        // Limpiar base de datos
        citaRepository.deleteAll();
        mascotaRepository.deleteAll();
        usuarioRepository.deleteAll();
        servicioRepository.deleteAll();
        
        // Crear datos de prueba
        propietario = new Usuario();
        propietario.setNombre("Juan");
        propietario.setApellido("Pérez");
        propietario.setDni("12345678");
        propietario.setUsuario("jpropietario");
        propietario.setCorreo("juan@test.com");
        propietario.setContrasena("password");
        propietario = usuarioRepository.save(propietario);

        veterinario = new Usuario();
        veterinario.setNombre("María");
        veterinario.setApellido("Veterinaria");
        veterinario.setDni("87654321");
        veterinario.setUsuario("mveterinaria");
        veterinario.setCorreo("maria@test.com");
        veterinario.setContrasena("password");
        veterinario = usuarioRepository.save(veterinario);

        mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setEspecie("Perro");
        mascota.setRaza("Labrador");
        mascota.setEdad(3);
        mascota.setSexo(Mascota.Sexo.Macho);
        mascota.setFechaNacimiento(LocalDate.now().minusYears(3));
        mascota.setPropietario(propietario);
        mascota = mascotaRepository.save(mascota);

        servicio = new Servicio();
        servicio.setNombre("Consulta General");
        servicio.setPrecio(50.0);
        servicio = servicioRepository.save(servicio);
    }

    @AfterEach
    void tearDown() {
        citaRepository.deleteAll();
        mascotaRepository.deleteAll();
        usuarioRepository.deleteAll();
        servicioRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("GET /api/citas - Debe listar todas las citas")
    void testListarCitas() {
        // Crear una cita de prueba
        Cita cita = new Cita();
        cita.setFecha(LocalDateTime.now().plusDays(1));
        cita.setMascota(mascota);
        cita.setServicio(servicio);
        cita.setVeterinario(veterinario);
        cita.setEstado(Cita.EstadoCita.Pendiente);
        cita.setHora(LocalDateTime.now().plusDays(1).toLocalTime());
        citaRepository.save(cita);

        // Llamar al endpoint
        ResponseEntity<List<CitaResponseDTO>> response = restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CitaResponseDTO>>() {}
        );

        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        List<CitaResponseDTO> body = response.getBody();
        assertNotNull(body, "La respuesta del cuerpo no debe ser nula");
        assertEquals(1, body.size());
        CitaResponseDTO citaDTO = body.get(0);
        assertEquals("Firulais", citaDTO.getMascotaNombre());
        assertEquals("Consulta General", citaDTO.getServicioNombre());
        assertEquals("María Veterinaria", citaDTO.getVeterinarioNombre());
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/citas - Debe crear una cita correctamente")
    void testCrearCita() {
        // Preparar request DTO
        CitaRequestDTO request = new CitaRequestDTO();
        request.setMascotaId(mascota.getId());
        request.setServicioId(servicio.getId());
        request.setVeterinarioId(veterinario.getId());
        request.setFecha(LocalDateTime.now().plusDays(2));
        request.setObservaciones("Revisión de rutina");

        // Llamar al endpoint
        ResponseEntity<CitaResponseDTO> response = restTemplate.postForEntity(
            baseUrl,
            request,
            CitaResponseDTO.class
        );

        // Verificar
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        CitaResponseDTO body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
        assertEquals("Firulais", body.getMascotaNombre());
        assertEquals("María Veterinaria", body.getVeterinarioNombre());
        assertEquals("Pendiente", body.getEstado());

        // Verificar en base de datos
        assertEquals(1, citaRepository.count());
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/citas - Debe asignar veterinario automáticamente si no se especifica")
    void testCrearCitaSinVeterinario() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setMascotaId(mascota.getId());
        request.setServicioId(servicio.getId());
        request.setFecha(LocalDateTime.now().plusDays(3));

        ResponseEntity<CitaResponseDTO> response = restTemplate.postForEntity(
            baseUrl,
            request,
            CitaResponseDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        CitaResponseDTO body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getVeterinarioNombre(), "El nombre del veterinario no debe ser nulo");
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/citas/{id} - Debe obtener cita por ID")
    void testObtenerCitaPorId() {
        // Crear cita
        Cita cita = new Cita();
        cita.setFecha(LocalDateTime.now().plusDays(1));
        cita.setMascota(mascota);
        cita.setServicio(servicio);
        cita.setVeterinario(veterinario);
        cita.setEstado(Cita.EstadoCita.Pendiente);
        cita.setHora(LocalDateTime.now().plusDays(1).toLocalTime());
        cita = citaRepository.save(cita);

        // Obtener por ID
        ResponseEntity<CitaResponseDTO> response = restTemplate.getForEntity(
            baseUrl + "/" + cita.getId(),
            CitaResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        CitaResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(cita.getId(), body.getId());
    }

    @Test
    @Order(5)
    @DisplayName("GET /api/citas/{id} - Debe retornar 404 si cita no existe")
    void testObtenerCitaInexistente() {
        ResponseEntity<CitaResponseDTO> response = restTemplate.getForEntity(
            baseUrl + "/99999",
            CitaResponseDTO.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /api/citas/{id} - Debe eliminar una cita")
    void testEliminarCita() {
        // Crear cita
        Cita cita = new Cita();
        cita.setFecha(LocalDateTime.now().plusDays(1));
        cita.setMascota(mascota);
        cita.setServicio(servicio);
        cita.setVeterinario(veterinario);
        cita.setEstado(Cita.EstadoCita.Pendiente);
        cita.setHora(LocalDateTime.now().plusDays(1).toLocalTime());
        cita = citaRepository.save(cita);

        assertEquals(1, citaRepository.count());

        // Eliminar
        restTemplate.delete(baseUrl + "/" + cita.getId());

        // Verificar
        assertEquals(0, citaRepository.count());
    }

    @Test
    @Order(7)
    @DisplayName("GET /api/citas/futuras - Debe listar solo citas futuras")
    void testListarCitasFuturas() {
        // Crear cita pasada
        Cita citaPasada = new Cita();
        citaPasada.setFecha(LocalDateTime.now().minusDays(1));
        citaPasada.setMascota(mascota);
        citaPasada.setServicio(servicio);
        citaPasada.setVeterinario(veterinario);
        citaPasada.setEstado(Cita.EstadoCita.Atendida);
        citaPasada.setHora(LocalDateTime.now().minusDays(1).toLocalTime());
        citaRepository.save(citaPasada);

        // Crear cita futura
        Cita citaFutura = new Cita();
        citaFutura.setFecha(LocalDateTime.now().plusDays(1));
        citaFutura.setMascota(mascota);
        citaFutura.setServicio(servicio);
        citaFutura.setVeterinario(veterinario);
        citaFutura.setEstado(Cita.EstadoCita.Pendiente);
        citaFutura.setHora(LocalDateTime.now().plusDays(1).toLocalTime());
        citaRepository.save(citaFutura);

        // Llamar endpoint
        ResponseEntity<List<CitaResponseDTO>> response = restTemplate.exchange(
            baseUrl + "/futuras",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CitaResponseDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        List<CitaResponseDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(8)
    @DisplayName("POST /api/citas - Debe validar campos obligatorios")
    void testValidacionCamposObligatorios() {
        CitaRequestDTO request = new CitaRequestDTO();
        // No se establecen campos requeridos

        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("POST /api/citas - Debe validar fecha futura")
    void testValidacionFechaFutura() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setMascotaId(mascota.getId());
        request.setServicioId(servicio.getId());
        request.setFecha(LocalDateTime.now().minusDays(1)); // Fecha pasada

        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
