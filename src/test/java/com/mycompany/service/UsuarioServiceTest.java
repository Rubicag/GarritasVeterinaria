package com.mycompany.service;

import com.mycompany.model.Rol;
import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;
import com.mycompany.config.TestSecurityConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        assertNotNull(usuarios);
        assertEquals(3, usuarios.size());
    }

    @Test
    public void testFindById() {
        Optional<Usuario> usuario = usuarioService.findById(1L);
        assertTrue(usuario.isPresent());
        assertEquals("admin", usuario.get().getUsuario());
    }

    @Test
    public void testFindByUsername() {
        Optional<Usuario> usuario = usuarioService.findByUsername("jperez");
        assertTrue(usuario.isPresent());
        assertEquals("Juan", usuario.get().getNombre());
        assertEquals("Perez", usuario.get().getApellido());
    }

    @Test
    public void testCreateUsuario() {
        // Crear un nuevo usuario
        Usuario newUsuario = new Usuario();
        newUsuario.setNombre("Test");
        newUsuario.setApellido("Usuario");
        newUsuario.setDni("87654321");
        newUsuario.setCorreo("test@test.com");
        newUsuario.setTelefono("999888777");
        newUsuario.setDireccion("Calle Test 123");
        newUsuario.setUsuario("testuser");
        newUsuario.setContrasena("password123"); // será encriptada por el servicio

        // Configurar rol
        Rol rolUser = new Rol();
        rolUser.setId(3L); // Rol USER
        newUsuario.setRol(rolUser);

        // Guardar el usuario
        Usuario savedUsuario = usuarioService.create(newUsuario);
        assertNotNull(savedUsuario.getId());

        // Verificar que se guardó correctamente
        Optional<Usuario> retrievedUsuario = usuarioService.findById(savedUsuario.getId());
        assertTrue(retrievedUsuario.isPresent());
        assertEquals("Test", retrievedUsuario.get().getNombre());
        
        // Verificar que la contraseña fue encriptada
        assertNotEquals("password123", retrievedUsuario.get().getContrasena());
    }

    @Test
    public void testDeleteUsuario() {
        // Primero verificamos que existe el usuario con ID 3
        assertTrue(usuarioRepository.existsById(3L));
        
        // Eliminamos el usuario
        usuarioService.delete(3L);
        
        // Verificamos que ya no existe
        assertFalse(usuarioRepository.existsById(3L));
    }
}