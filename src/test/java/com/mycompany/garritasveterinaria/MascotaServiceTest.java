package com.mycompany.garritasveterinaria;

import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import com.mycompany.repository.MascotaRepository;
import com.mycompany.repository.UsuarioRepository;
import com.mycompany.service.MascotaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests completos para MascotaService (compatibles con métodos reales)
 * Valida operaciones CRUD y lógica específica de mascotas veterinarias
 */
@DisplayName("MascotaService Tests")
public class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private MascotaService mascotaService;
    private Mascota sampleMascota;
    private Usuario samplePropietario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mascotaService = new MascotaService(mascotaRepository, usuarioRepository);
        
        // Crear propietario de prueba
        samplePropietario = new Usuario();
        samplePropietario.setId(1L);
        samplePropietario.setUsuario("propietario");
        samplePropietario.setCorreo("propietario@test.com");
        
        // Crear mascota de prueba
        sampleMascota = new Mascota();
        sampleMascota.setId(1L);
        sampleMascota.setNombre("Firulais");
        sampleMascota.setEspecie("Perro");
        sampleMascota.setRaza("Labrador");
        sampleMascota.setEdad(3);
        sampleMascota.setPropietario(samplePropietario);
    }

    @Nested
    @DisplayName("CRUD Operations Tests")
    class CRUDOperationsTests {

        @Test
        @DisplayName("Crear mascota debe funcionar correctamente")
        public void createMascotaShouldWork() {
            Mascota newMascota = new Mascota();
            newMascota.setNombre("Rex");
            newMascota.setEspecie("Perro");
            newMascota.setRaza("Pastor");
            newMascota.setEdad(2);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(samplePropietario));
            when(mascotaRepository.save(any(Mascota.class))).thenReturn(sampleMascota);

            Mascota result = mascotaService.create(newMascota, 1L);

            assertNotNull(result);
            verify(usuarioRepository).findById(1L);
            verify(mascotaRepository).save(any(Mascota.class));
        }

        @Test
        @DisplayName("Crear mascota con propietario inexistente debe fallar")
        public void createMascotaWithNonExistentOwnerShouldFail() {
            Mascota newMascota = new Mascota();
            newMascota.setNombre("Rex");

            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, 
                () -> mascotaService.create(newMascota, 999L));
            
            verify(usuarioRepository).findById(999L);
            verify(mascotaRepository, never()).save(any(Mascota.class));
        }

        @Test
        @DisplayName("Encontrar mascota por ID debe funcionar")
        public void findByIdShouldWork() {
            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));

            Optional<Mascota> result = mascotaService.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(sampleMascota.getId(), result.get().getId());
            verify(mascotaRepository).findById(1L);
        }

        @Test
        @DisplayName("Encontrar todas las mascotas debe funcionar")
        public void findAllShouldWork() {
            Mascota mascota2 = new Mascota();
            mascota2.setId(2L);
            mascota2.setNombre("Michi");
            
            List<Mascota> mascotas = Arrays.asList(sampleMascota, mascota2);
            when(mascotaRepository.findAll()).thenReturn(mascotas);

            List<Mascota> result = mascotaService.findAll();

            assertEquals(2, result.size());
            assertEquals(sampleMascota.getId(), result.get(0).getId());
            assertEquals(mascota2.getId(), result.get(1).getId());
            verify(mascotaRepository).findAll();
        }

        @Test
        @DisplayName("Actualizar mascota debe funcionar")
        public void updateMascotaShouldWork() {
            Mascota updatedData = new Mascota();
            updatedData.setNombre("Rex Actualizado");
            updatedData.setEspecie("Perro");
            updatedData.setRaza("Pastor Alemán");
            updatedData.setEdad(4);

            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));
            when(mascotaRepository.save(any(Mascota.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Mascota result = mascotaService.update(1L, updatedData);

            assertNotNull(result);
            assertEquals("Rex Actualizado", result.getNombre());
            verify(mascotaRepository).findById(1L);
            verify(mascotaRepository).save(sampleMascota);
        }

        @Test
        @DisplayName("Eliminar mascota debe funcionar")
        public void deleteMascotaShouldWork() {
            when(mascotaRepository.findById(1L)).thenReturn(Optional.of(sampleMascota));
            doNothing().when(mascotaRepository).deleteById(1L);

            boolean result = mascotaService.delete(1L);

            assertTrue(result);
            verify(mascotaRepository).findById(1L);
            verify(mascotaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Eliminar mascota inexistente debe retornar false")
        public void deleteNonExistentMascotaShouldReturnFalse() {
            when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

            boolean result = mascotaService.delete(999L);

            assertFalse(result);
            verify(mascotaRepository).findById(999L);
            verify(mascotaRepository, never()).deleteById(999L);
        }
    }

    @Nested
    @DisplayName("Search Operations Tests")
    class SearchOperationsTests {

        @Test
        @DisplayName("Buscar por propietario debe funcionar")
        public void findByPropietarioShouldWork() {
            List<Mascota> mascotas = Arrays.asList(sampleMascota);
            when(mascotaRepository.findByPropietario(samplePropietario)).thenReturn(mascotas);

            List<Mascota> result = mascotaService.findByPropietario(samplePropietario);

            assertEquals(1, result.size());
            assertEquals(sampleMascota.getId(), result.get(0).getId());
            verify(mascotaRepository).findByPropietario(samplePropietario);
        }

        @Test
        @DisplayName("Buscar por propietario ID debe funcionar")
        public void findByPropietarioIdShouldWork() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(samplePropietario));
            when(mascotaRepository.findByPropietario(samplePropietario)).thenReturn(Arrays.asList(sampleMascota));

            List<Mascota> result = mascotaService.findByPropietarioId(1L);

            assertEquals(1, result.size());
            assertEquals(sampleMascota.getId(), result.get(0).getId());
            verify(usuarioRepository).findById(1L);
            verify(mascotaRepository).findByPropietario(samplePropietario);
        }

        @Test
        @DisplayName("Buscar por especie debe funcionar")
        public void findByEspecieShouldWork() {
            List<Mascota> perros = Arrays.asList(sampleMascota);
            when(mascotaRepository.findByEspecie("Perro")).thenReturn(perros);

            List<Mascota> result = mascotaService.findByEspecie("Perro");

            assertEquals(1, result.size());
            assertEquals("Perro", result.get(0).getEspecie());
            verify(mascotaRepository).findByEspecie("Perro");
        }

        @Test
        @DisplayName("Buscar por nombre debe funcionar")
        public void findByNombreShouldWork() {
            List<Mascota> mascotas = Arrays.asList(sampleMascota);
            when(mascotaRepository.findByNombreContainingIgnoreCase("firu")).thenReturn(mascotas);

            List<Mascota> result = mascotaService.findByNombre("firu");

            assertEquals(1, result.size());
            assertTrue(result.get(0).getNombre().toLowerCase().contains("firu"));
            verify(mascotaRepository).findByNombreContainingIgnoreCase("firu");
        }
    }

    @Nested
    @DisplayName("Count Operations Tests")
    class CountOperationsTests {

        @Test
        @DisplayName("Contar mascotas debe funcionar")
        public void countMascotasShouldWork() {
            when(mascotaRepository.count()).thenReturn(5L);

            long count = mascotaService.count();

            assertEquals(5L, count);
            verify(mascotaRepository).count();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Crear mascota con datos válidos debe funcionar")
        public void createMascotaWithValidDataShouldWork() {
            Mascota validMascota = new Mascota();
            validMascota.setNombre("Rex");
            validMascota.setEspecie("Perro");
            validMascota.setRaza("Pastor");
            validMascota.setEdad(2);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(samplePropietario));
            when(mascotaRepository.save(any(Mascota.class))).thenReturn(sampleMascota);

            assertDoesNotThrow(() -> mascotaService.create(validMascota, 1L));
            verify(usuarioRepository).findById(1L);
            verify(mascotaRepository).save(any(Mascota.class));
        }

        @Test
        @DisplayName("Actualizar mascota inexistente debe retornar null")
        public void updateNonExistentMascotaShouldReturnNull() {
            when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

            Mascota updatedData = new Mascota();
            updatedData.setNombre("Nuevo Nombre");

            Mascota result = mascotaService.update(999L, updatedData);

            assertNull(result);
            verify(mascotaRepository).findById(999L);
            verify(mascotaRepository, never()).save(any(Mascota.class));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Buscar con ID inexistente debe retornar Optional vacío")
        public void findWithNonExistentIdShouldReturnEmpty() {
            when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Mascota> result = mascotaService.findById(999L);

            assertFalse(result.isPresent());
            verify(mascotaRepository).findById(999L);
        }

        @Test
        @DisplayName("Buscar por propietario inexistente debe retornar lista vacía")
        public void findByNonExistentOwnerShouldReturnEmptyList() {
            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            List<Mascota> result = mascotaService.findByPropietarioId(999L);

            assertTrue(result.isEmpty());
            verify(usuarioRepository).findById(999L);
        }
    }
}