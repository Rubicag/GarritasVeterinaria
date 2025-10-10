package com.mycompany.garritasveterinaria;

import com.mycompany.model.Cita;
import com.mycompany.model.Mascota;
import com.mycompany.model.Servicio;
import com.mycompany.repository.CitaRepository;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.ServicioRepository;
import com.mycompany.service.CitaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests completos para CitaService (compatibles con métodos reales)
 * Valida gestión de citas veterinarias y programación
 */
@DisplayName("CitaService Tests")
public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private ServicioRepository servicioRepository;

    private CitaService citaService;
    private Cita sampleCita;
    private Mascota sampleMascota;
    private Servicio sampleServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        citaService = new CitaService(citaRepository, mascotaRepository, servicioRepository);
        
        // Crear mascota de prueba
        sampleMascota = new Mascota();
        sampleMascota.setId(1L);
        sampleMascota.setNombre("Firulais");
        sampleMascota.setEspecie("Perro");
        
        // Crear servicio de prueba
        sampleServicio = new Servicio();
        sampleServicio.setId(1L);
        sampleServicio.setNombre("Consulta General");
        sampleServicio.setPrecio(50.0);
        
        // Crear cita de prueba
        sampleCita = new Cita();
        sampleCita.setId(1L);
        sampleCita.setFecha(LocalDateTime.now().plusDays(1));
        sampleCita.setMascota(sampleMascota);
        sampleCita.setServicio(sampleServicio);
        sampleCita.setObservaciones("Cita de rutina");
    }

    @Nested
    @DisplayName("CRUD Operations Tests")
    class CRUDOperationsTests {

        @Test
        @DisplayName("Crear cita debe funcionar correctamente")
        public void createCitaShouldWork() {
            Cita newCita = new Cita();
            newCita.setFecha(LocalDateTime.now().plusDays(2));
            newCita.setObservaciones("Nueva cita");

            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));
            when(servicioRepository.findById(1L)).thenReturn(Optional.of(sampleServicio));
            when(citaRepository.findByMascota(sampleMascota)).thenReturn(Arrays.asList());
            when(citaRepository.save(any(Cita.class))).thenReturn(sampleCita);

            Cita result = citaService.create(newCita, 1L, 1L);

            assertNotNull(result);
            verify(mascotaRepository).findById(1L);
            verify(servicioRepository).findById(1L);
            verify(citaRepository).save(any(Cita.class));
        }

        @Test
        @DisplayName("Crear cita con mascota inexistente debe fallar")
        public void createCitaWithNonExistentMascotaShouldFail() {
            Cita newCita = new Cita();
            newCita.setFecha(LocalDateTime.now().plusDays(2));

            when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, 
                () -> citaService.create(newCita, 999L, 1L));
            
            verify(mascotaRepository).findById(999L);
            verify(citaRepository, never()).save(any(Cita.class));
        }

        @Test
        @DisplayName("Crear cita con servicio inexistente debe fallar")
        public void createCitaWithNonExistentServicioShouldFail() {
            Cita newCita = new Cita();
            newCita.setFecha(LocalDateTime.now().plusDays(2));

            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));
            when(servicioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, 
                () -> citaService.create(newCita, 1L, 999L));
            
            verify(servicioRepository).findById(999L);
            verify(citaRepository, never()).save(any(Cita.class));
        }

        @Test
        @DisplayName("Encontrar cita por ID debe funcionar")
        public void getByIdShouldWork() {
            when(citaRepository.findById(1L)).thenReturn(Optional.of(sampleCita));

            Optional<Cita> result = citaService.getById(1L);

            assertTrue(result.isPresent());
            assertEquals(sampleCita.getId(), result.get().getId());
            verify(citaRepository).findById(1L);
        }

        @Test
        @DisplayName("Listar todas las citas debe funcionar")
        public void listAllShouldWork() {
            Cita cita2 = new Cita();
            cita2.setId(2L);
            
            List<Cita> citas = Arrays.asList(sampleCita, cita2);
            when(citaRepository.findAll()).thenReturn(citas);

            List<Cita> result = citaService.listAll();

            assertEquals(2, result.size());
            assertEquals(sampleCita.getId(), result.get(0).getId());
            assertEquals(cita2.getId(), result.get(1).getId());
            verify(citaRepository).findAll();
        }

        @Test
        @DisplayName("Actualizar cita debe funcionar")
        public void updateCitaShouldWork() {
            Cita updatedData = new Cita();
            updatedData.setFecha(LocalDateTime.now().plusDays(3));
            updatedData.setObservaciones("Cita actualizada");

            when(citaRepository.findById(1L)).thenReturn(Optional.of(sampleCita));
            when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cita result = citaService.update(1L, updatedData);

            assertNotNull(result);
            assertEquals(updatedData.getFecha(), result.getFecha());
            assertEquals(updatedData.getObservaciones(), result.getObservaciones());
            verify(citaRepository).findById(1L);
            verify(citaRepository).save(sampleCita);
        }

        @Test
        @DisplayName("Eliminar cita debe funcionar")
        public void deleteCitaShouldWork() {
            when(citaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(citaRepository).deleteById(1L);

            boolean result = citaService.delete(1L);

            assertTrue(result);
            verify(citaRepository).existsById(1L);
            verify(citaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Eliminar cita inexistente debe retornar false")
        public void deleteNonExistentCitaShouldReturnFalse() {
            when(citaRepository.existsById(999L)).thenReturn(false);

            boolean result = citaService.delete(999L);

            assertFalse(result);
            verify(citaRepository).existsById(999L);
            verify(citaRepository, never()).deleteById(999L);
        }
    }

    @Nested
    @DisplayName("Search Operations Tests")
    class SearchOperationsTests {

        @Test
        @DisplayName("Buscar citas por mascota debe funcionar")
        public void findByMascotaShouldWork() {
            List<Cita> citas = Arrays.asList(sampleCita);
            when(citaRepository.findByMascota(sampleMascota)).thenReturn(citas);

            List<Cita> result = citaService.findByMascota(sampleMascota);

            assertEquals(1, result.size());
            assertEquals(sampleCita.getMascota().getId(), result.get(0).getMascota().getId());
            verify(citaRepository).findByMascota(sampleMascota);
        }

        @Test
        @DisplayName("Buscar citas por servicio debe funcionar")
        public void findByServicioShouldWork() {
            List<Cita> citas = Arrays.asList(sampleCita);
            when(citaRepository.findByServicio(sampleServicio)).thenReturn(citas);

            List<Cita> result = citaService.findByServicio(sampleServicio);

            assertEquals(1, result.size());
            assertEquals(sampleCita.getServicio().getId(), result.get(0).getServicio().getId());
            verify(citaRepository).findByServicio(sampleServicio);
        }

        @Test
        @DisplayName("Buscar citas futuras debe funcionar")
        public void findCitasFuturasShouldWork() {
            List<Cita> citas = Arrays.asList(sampleCita);
            when(citaRepository.findCitasFuturas(any(LocalDateTime.class))).thenReturn(citas);

            List<Cita> result = citaService.findCitasFuturas();

            assertEquals(1, result.size());
            verify(citaRepository).findCitasFuturas(any(LocalDateTime.class));
        }

        @Test
        @DisplayName("Buscar citas por día debe funcionar")
        public void findCitasPorDiaShouldWork() {
            LocalDateTime fecha = LocalDateTime.now().plusDays(1);
            List<Cita> citas = Arrays.asList(sampleCita);
            when(citaRepository.findCitasPorDia(fecha)).thenReturn(citas);

            List<Cita> result = citaService.findCitasPorDia(fecha);

            assertEquals(1, result.size());
            verify(citaRepository).findCitasPorDia(fecha);
        }

        @Test
        @DisplayName("Buscar citas por rango de fechas debe funcionar")
        public void findByFechaBetweenShouldWork() {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fin = LocalDateTime.now().plusDays(7);
            List<Cita> citas = Arrays.asList(sampleCita);
            
            when(citaRepository.findByFechaBetween(inicio, fin)).thenReturn(citas);

            List<Cita> result = citaService.findByFechaBetween(inicio, fin);

            assertEquals(1, result.size());
            verify(citaRepository).findByFechaBetween(inicio, fin);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Confirmar cita debe funcionar")
        public void confirmarCitaShouldWork() {
            when(citaRepository.findById(1L)).thenReturn(Optional.of(sampleCita));

            boolean result = citaService.confirmarCita(1L);

            assertTrue(result);
            verify(citaRepository).findById(1L);
        }

        @Test
        @DisplayName("Confirmar cita inexistente debe fallar")
        public void confirmarCitaInexistenteShouldFail() {
            when(citaRepository.findById(999L)).thenReturn(Optional.empty());

            boolean result = citaService.confirmarCita(999L);

            assertFalse(result);
            verify(citaRepository).findById(999L);
        }

        @Test
        @DisplayName("Crear cita con conflicto de horario debe fallar")
        public void createCitaWithTimeConflictShouldFail() {
            Cita newCita = new Cita();
            newCita.setFecha(LocalDateTime.now().plusHours(1));

            Cita existingCita = new Cita();
            existingCita.setFecha(LocalDateTime.now().plusHours(1).plusMinutes(30));

            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));
            when(servicioRepository.findById(1L)).thenReturn(Optional.of(sampleServicio));
            when(citaRepository.findByMascota(sampleMascota)).thenReturn(Arrays.asList(existingCita));

            assertThrows(RuntimeException.class, 
                () -> citaService.create(newCita, 1L, 1L));

            verify(citaRepository, never()).save(any(Cita.class));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Actualizar cita inexistente debe retornar null")
        public void updateNonExistentCitaShouldReturnNull() {
            when(citaRepository.findById(999L)).thenReturn(Optional.empty());

            Cita updatedData = new Cita();
            updatedData.setObservaciones("Nueva observación");

            Cita result = citaService.update(999L, updatedData);

            assertNull(result);
            verify(citaRepository).findById(999L);
            verify(citaRepository, never()).save(any(Cita.class));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Buscar con ID inexistente debe retornar Optional vacío")
        public void getByIdWithNonExistentIdShouldReturnEmpty() {
            when(citaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Cita> result = citaService.getById(999L);

            assertFalse(result.isPresent());
            verify(citaRepository).findById(999L);
        }
    }
}