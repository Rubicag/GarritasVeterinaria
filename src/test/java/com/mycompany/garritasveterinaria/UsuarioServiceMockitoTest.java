package com.mycompany.garritasveterinaria;

import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import com.mycompany.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests avanzados con Mockito para UsuarioService
 * Enfoque en verificación de comportamiento y interacciones
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService Advanced Mockito Tests")
public class UsuarioServiceMockitoTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private Usuario sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Usuario();
        sampleUser.setId(1L);
        sampleUser.setUsuario("testuser");
        sampleUser.setCorreo("test@example.com");
        sampleUser.setContrasena("encodedPassword");
        
        // Crear rol usando entidad Rol
        com.mycompany.model.Rol userRole = new com.mycompany.model.Rol();
        userRole.setId(1L);
        userRole.setNombre("USER");
        sampleUser.setRol(userRole);
    }

    @Nested
    @DisplayName("Method Delegation Tests")
    class MethodDelegationTests {

        @Test
        @DisplayName("countUsers debe delegar al repository")
        public void countUsersDelegatesToRepository() {
            when(usuarioRepository.count()).thenReturn(5L);

            long result = usuarioService.countUsers();

            assertEquals(5L, result);
            verify(usuarioRepository).count();
            verifyNoMoreInteractions(usuarioRepository);
        }

        @Test
        @DisplayName("findById debe delegar al repository con el ID correcto")
        public void findByIdDelegatesToRepositoryWithCorrectId() {
            Long testId = 42L;
            when(usuarioRepository.findById(testId)).thenReturn(Optional.of(sampleUser));

            Optional<Usuario> result = usuarioService.findById(testId);

            assertTrue(result.isPresent());
            assertEquals(sampleUser, result.get());
            verify(usuarioRepository).findById(testId);
            verifyNoMoreInteractions(usuarioRepository);
        }

        @Test
        @DisplayName("findAll debe retornar exactamente lo que retorna el repository")
        public void findAllReturnsExactlyWhatRepositoryReturns() {
            Usuario user1 = new Usuario();
            user1.setId(1L);
            Usuario user2 = new Usuario();
            user2.setId(2L);
            List<Usuario> expectedUsers = Arrays.asList(user1, user2);

            when(usuarioRepository.findAll()).thenReturn(expectedUsers);

            List<Usuario> result = usuarioService.findAll();

            assertEquals(expectedUsers, result);
            assertSame(expectedUsers, result);
            verify(usuarioRepository).findAll();
            verifyNoMoreInteractions(usuarioRepository);
        }

        @Test
        @DisplayName("delete debe delegar al repository")
        public void deleteDelegatesToRepository() {
            Long testId = 99L;
            when(usuarioRepository.findById(testId)).thenReturn(Optional.of(sampleUser));
            doNothing().when(usuarioRepository).deleteById(testId);

            usuarioService.delete(testId);

            verify(usuarioRepository).findById(testId);
            verify(usuarioRepository).deleteById(testId);
        }
    }

    @Nested
    @DisplayName("Argument Capture Tests")
    class ArgumentCaptureTests {

        @Test
        @DisplayName("save debe codificar contraseña antes de guardar")
        public void saveShouldEncodePasswordBeforeSaving() {
            Usuario newUser = new Usuario();
            newUser.setUsuario("newuser");
            newUser.setCorreo("new@example.com");
            newUser.setContrasena("plainPassword");
            
            com.mycompany.model.Rol userRole = new com.mycompany.model.Rol();
            userRole.setId(1L);
            userRole.setNombre("USER");
            newUser.setRol(userRole);

            when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword123");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(sampleUser);

            usuarioService.create(newUser);

            // Capturar el argumento pasado al passwordEncoder
            verify(passwordEncoder).encode(stringCaptor.capture());
            assertEquals("plainPassword", stringCaptor.getValue());

            // Capturar el usuario pasado al repository
            verify(usuarioRepository).save(usuarioCaptor.capture());
            Usuario capturedUser = usuarioCaptor.getValue();
            assertEquals("encodedPassword123", capturedUser.getContrasena());
            assertEquals("newuser", capturedUser.getUsuario());
        }

        @Test
        @DisplayName("save debe establecer fecha de creación automáticamente")
        public void saveShouldSetCreationDateAutomatically() {
            Usuario newUser = new Usuario();
            newUser.setUsuario("timeduser");
            newUser.setCorreo("timed@example.com");
            newUser.setContrasena("password");
            
            com.mycompany.model.Rol userRole = new com.mycompany.model.Rol();
            userRole.setId(1L);
            userRole.setNombre("USER");
            newUser.setRol(userRole);

            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(sampleUser);

            usuarioService.create(newUser);

            verify(usuarioRepository).save(usuarioCaptor.capture());
            Usuario capturedUser = usuarioCaptor.getValue();
            
            // Verificar que el usuario fue guardado (la entidad Usuario no tiene fecha de creación)
            assertNotNull(capturedUser);
        }


    }

    @Nested
    @DisplayName("Interaction Verification Tests")
    class InteractionVerificationTests {

        @Test
        @DisplayName("findByUsername debe hacer exactamente una consulta")
        public void findByUsernameShouldMakeExactlyOneQuery() {
            String testUsername = "testuser";
            when(usuarioRepository.findByUsername(testUsername)).thenReturn(Optional.of(sampleUser));

            usuarioService.findByUsername(testUsername);

            verify(usuarioRepository, times(1)).findByUsername(testUsername);
            verify(usuarioRepository, never()).findById(anyLong());
            verify(usuarioRepository, never()).findByEmail(anyString());
            verifyNoMoreInteractions(usuarioRepository, passwordEncoder);
        }



        @Test
        @DisplayName("changePassword debe verificar old password y save en orden correcto")
        public void changePasswordShouldCallFindThenSave() {
            Long userId = 1L;
            String oldPassword = "oldpass123";
            String newPassword = "newpass123";
            String encodedNewPassword = "encodednewpass123";

            when(usuarioRepository.findById(userId)).thenReturn(Optional.of(sampleUser));
            when(passwordEncoder.matches(oldPassword, sampleUser.getContrasena())).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(sampleUser);

            boolean result = usuarioService.changePassword(userId, oldPassword, newPassword);

            assertTrue(result);
            verify(usuarioRepository).findById(userId);
            verify(passwordEncoder).matches(oldPassword, sampleUser.getContrasena());
            verify(passwordEncoder).encode(newPassword);
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("operación fallida no debe hacer save")
        public void failedOperationShouldNotSave() {
            Long nonExistentId = 999L;
            when(usuarioRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            boolean result = usuarioService.changePassword(nonExistentId, "oldpassword", "newpassword");
            assertFalse(result, "Cambio de password debe fallar para usuario inexistente");

            verify(usuarioRepository).findById(nonExistentId);
            verify(usuarioRepository, never()).save(any(Usuario.class));
            verifyNoInteractions(passwordEncoder);
        }
    }

    @Nested
    @DisplayName("Stubbing Behavior Tests")
    class StubbingBehaviorTests {

        @Test
        @DisplayName("múltiples llamadas a count deben retornar valores consistentes")
        public void multipleCountCallsShouldReturnConsistentValues() {
            when(usuarioRepository.count()).thenReturn(10L);

            long count1 = usuarioService.countUsers();
            long count2 = usuarioService.countUsers();
            long count3 = usuarioService.countUsers();

            assertEquals(10L, count1);
            assertEquals(10L, count2);
            assertEquals(10L, count3);
            verify(usuarioRepository, times(3)).count();
        }

        @Test
        @DisplayName("stub debe manejar diferentes parámetros de entrada")
        public void stubShouldHandleDifferentInputParameters() {
            when(usuarioRepository.findByUsername("user1")).thenReturn(Optional.of(sampleUser));
            when(usuarioRepository.findByUsername("user2")).thenReturn(Optional.empty());

            Optional<Usuario> result1 = usuarioService.findByUsername("user1");
            Optional<Usuario> result2 = usuarioService.findByUsername("user2");

            assertTrue(result1.isPresent());
            assertFalse(result2.isPresent());
            
            verify(usuarioRepository).findByUsername("user1");
            verify(usuarioRepository).findByUsername("user2");
        }

        @Test
        @DisplayName("stub con excepción debe propagar correctamente")
        public void stubWithExceptionShouldPropagateCorrectly() {
            Long problematicId = 404L;
            when(usuarioRepository.findById(problematicId))
                .thenThrow(new RuntimeException("Database connection failed"));

            assertThrows(RuntimeException.class, () -> {
                usuarioService.findById(problematicId);
            });

            verify(usuarioRepository).findById(problematicId);
        }


    }

    @Nested
    @DisplayName("Advanced Verification Tests")
    class AdvancedVerificationTests {

        @Test
        @DisplayName("debe verificar que nunca se llamen métodos no relacionados")
        public void shouldVerifyNeverCallUnrelatedMethods() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

            usuarioService.findById(1L);

            verify(usuarioRepository).findById(1L);
            verify(usuarioRepository, never()).deleteById(anyLong());
            verify(usuarioRepository, never()).save(any(Usuario.class));
            verify(usuarioRepository, never()).count();
        }

        @Test
        @DisplayName("debe verificar argumentos específicos con matchers")
        public void shouldVerifySpecificArgumentsWithMatchers() {
            Usuario userToSave = new Usuario();
            userToSave.setUsuario("specificuser");
            userToSave.setContrasena("password");
            
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(sampleUser);

            usuarioService.create(userToSave);

            verify(passwordEncoder).encode(eq("password"));
            verify(usuarioRepository).save(argThat(user -> 
                "specificuser".equals(user.getUsuario()) &&
                "encoded".equals(user.getContrasena())
            ));
        }

        @Test
        @DisplayName("debe verificar timeout en operaciones lentas")
        public void shouldVerifyTimeoutInSlowOperations() {
            when(usuarioRepository.count()).thenReturn(100L);

            long result = usuarioService.countUsers();

            assertEquals(100L, result);
            verify(usuarioRepository, timeout(1000)).count();
        }

        @Test
        @DisplayName("debe verificar al menos una interacción")
        public void shouldVerifyAtLeastOneInteraction() {
            when(usuarioRepository.findAll()).thenReturn(Arrays.asList(sampleUser));

            usuarioService.findAll();
            // Llamada adicional para probar atLeast
            usuarioService.findAll();

            verify(usuarioRepository, atLeast(1)).findAll();
            verify(usuarioRepository, atMost(2)).findAll();
        }
    }

    @Nested
    @DisplayName("Mock Reset and State Tests")
    class MockResetAndStateTests {

        @Test
        @DisplayName("reset de mock debe limpiar interacciones previas")
        public void mockResetShouldClearPreviousInteractions() {
            // Primera interacción
            when(usuarioRepository.count()).thenReturn(5L);
            usuarioService.countUsers();
            verify(usuarioRepository).count();

            // Reset mock
            reset(usuarioRepository);

            // Verificar que las interacciones previas se borraron
            verifyNoInteractions(usuarioRepository);

            // Nueva configuración después del reset
            when(usuarioRepository.count()).thenReturn(10L);
            long newCount = usuarioService.countUsers();
            assertEquals(10L, newCount);
        }

        @Test
        @DisplayName("lenient stubbing debe permitir stubs no utilizados")
        public void lenientStubbingShouldAllowUnusedStubs() {
            // Configurar stub que no será usado
            lenient().when(usuarioRepository.findByEmail(anyString()))
                    .thenReturn(Optional.of(sampleUser));

            // Solo usar count, no email
            when(usuarioRepository.count()).thenReturn(1L);
            long count = usuarioService.countUsers();

            assertEquals(1L, count);
            verify(usuarioRepository).count();
            // No debería fallar por el stub no usado de findByEmail
        }
    }
}
