package com.mycompany.garritasveterinaria;

import com.mycompany.VeterinariaApplication;
import com.mycompany.config.TestSecurityConfig;
import com.mycompany.model.*;
import com.mycompany.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VeterinariaApplication.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("h2")
@Transactional
public class DataVerificationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MascotaRepository mascotaRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private CitaRepository citaRepository;

    @Test
    @DisplayName("Verificar que los datos de ejemplo se cargan correctamente")
    public void shouldLoadSampleDataCorrectly() {
        // Verificar usuarios
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertTrue(usuarios.size() >= 4, "Debe haber al menos 4 usuarios");
        
        Optional<Usuario> admin = usuarioRepository.findByUsername("admin");
        assertTrue(admin.isPresent(), "Usuario admin debe existir");
        assertEquals("ADMIN", admin.get().getRol().getNombre(), "Admin debe tener rol ADMIN");
        
        // Verificar servicios
        List<Servicio> servicios = servicioRepository.findAll();
        assertTrue(servicios.size() >= 6, "Debe haber al menos 6 servicios");
        
        // Verificar mascotas
        List<Mascota> mascotas = mascotaRepository.findAll();
        assertTrue(mascotas.size() >= 5, "Debe haber al menos 5 mascotas");
        
        // Verificar relación mascota-usuario
        Mascota firulais = mascotas.stream()
            .filter(m -> "Firulais".equals(m.getNombre()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(firulais, "Mascota Firulais debe existir");
        assertNotNull(firulais.getPropietario(), "Firulais debe tener propietario");
        assertEquals("juan", firulais.getPropietario().getUsuario(), "Firulais debe pertenecer a juan");
        
        // Verificar citas
        List<Cita> citas = citaRepository.findAll();
        assertTrue(citas.size() >= 5, "Debe haber al menos 5 citas");
        
        System.out.println("✅ VERIFICACIÓN EXITOSA:");
        System.out.println("📊 Usuarios: " + usuarios.size());
        System.out.println("🐕 Mascotas: " + mascotas.size());
        System.out.println("🏥 Servicios: " + servicios.size());
        System.out.println("📅 Citas: " + citas.size());
    }
    
    @Test
    @DisplayName("Verificar funcionalidad CRUD básica")
    public void shouldPerformBasicCrudOperations() {
        // Test crear usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsuario("testuser");
        nuevoUsuario.setCorreo("test@test.com");
        nuevoUsuario.setContrasena("encodedpassword");
        
        com.mycompany.model.Rol userRole = new com.mycompany.model.Rol();
        userRole.setId(1L);
        userRole.setNombre("USER");
        nuevoUsuario.setRol(userRole);
        
        Usuario savedUser = usuarioRepository.save(nuevoUsuario);
        assertNotNull(savedUser.getId(), "Usuario guardado debe tener ID");
        
        // Test buscar usuario
        Optional<Usuario> foundUser = usuarioRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent(), "Usuario debe encontrarse por username");
        
        // Test crear mascota para el usuario
        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre("TestDog");
        nuevaMascota.setEspecie("Perro");
        nuevaMascota.setRaza("Test Breed");
        nuevaMascota.setEdad(2);
        nuevaMascota.setPropietario(savedUser);
        
        Mascota savedMascota = mascotaRepository.save(nuevaMascota);
        assertNotNull(savedMascota.getId(), "Mascota guardada debe tener ID");
        assertEquals(savedUser.getId(), savedMascota.getPropietario().getId(), "Relación debe mantenerse");
        
        System.out.println("✅ CRUD OPERATIONS EXITOSAS");
    }
}