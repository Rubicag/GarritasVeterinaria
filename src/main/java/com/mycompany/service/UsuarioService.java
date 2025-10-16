/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de usuario (ahora usando Spring Data JPA).
 */
@Service
@Transactional
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = repo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
		
		if (usuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuario no encontrado: " + username);
		}
		
		Usuario user = usuario.get();
		String role = user.getRol() != null ? user.getRol().getNombre() : "USER";
		
		return User.builder()
				.username(user.getUsuario())
				.password(user.getContrasena())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}

	public int countUsers() {
		return (int) usuarioRepository.count();
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}

	public Usuario create(Usuario u) {
		// Validaciones
		if (usuarioRepository.existsByUsuario(u.getUsuario())) {
			throw new RuntimeException("El nombre de usuario ya existe");
		}
		if (usuarioRepository.existsByCorreo(u.getCorreo())) {
			throw new RuntimeException("El correo electrónico ya está registrado");
		}
		
		u.setId(null); // asegurar que es nuevo
		u.setContrasena(passwordEncoder.encode(u.getContrasena()));
		return usuarioRepository.save(u);
	}

	public Usuario update(Long id, Usuario u) {
		Optional<Usuario> existing = usuarioRepository.findById(id);
		if (existing.isEmpty()) {
			return null;
		}
		Usuario ex = existing.get();
		ex.setNombre(u.getNombre());
		ex.setApellido(u.getApellido());
		ex.setDni(u.getDni());
		ex.setCorreo(u.getCorreo());
		ex.setTelefono(u.getTelefono());
		ex.setDireccion(u.getDireccion());
		ex.setUsuario(u.getUsuario());
		ex.setRol(u.getRol());
		
		// Solo actualizar contraseña si se proporciona una nueva
		if (u.getContrasena() != null && !u.getContrasena().trim().isEmpty()) {
			ex.setContrasena(passwordEncoder.encode(u.getContrasena()));
		}
		
		return usuarioRepository.save(ex);
	}

	public boolean delete(Long id) {
		Optional<Usuario> existing = usuarioRepository.findById(id);
		if (existing.isEmpty()) return false;
		usuarioRepository.deleteById(id);
		return true;
	}

	// Métodos adicionales para autenticación y búsqueda
	public Optional<Usuario> findByUsername(String username) {
		return usuarioRepository.findByUsuario(username);
	}

	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByCorreo(email);
	}

	public boolean authenticateUser(String username, String rawPassword) {
		System.out.println("=== DEBUG AUTHENTICATION ===");
		System.out.println("Username: " + username);
		System.out.println("Raw Password: " + rawPassword);
		
		Optional<Usuario> user = usuarioRepository.findByUsuario(username);
		if (user.isPresent()) {
			String storedPassword = user.get().getContrasena();
			System.out.println("Stored Password: " + storedPassword);
			
			// TEMPORALMENTE: Usar solo texto plano hasta solucionar BCrypt
			boolean equals = rawPassword.equals(storedPassword);
			System.out.println("Plain text equals result: " + equals);
			return equals;
			
			// TODO: Rehabilitar BCrypt cuando se solucione el problema
			/*
			if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
				boolean matches = passwordEncoder.matches(rawPassword, storedPassword);
				System.out.println("BCrypt matches result: " + matches);
				return matches;
			} else {
				boolean equals = rawPassword.equals(storedPassword);
				System.out.println("Plain text equals result: " + equals);
				return equals;
			}
			*/
		} else {
			System.out.println("User not found!");
		}
		return false;
	}



	public boolean changePassword(Long userId, String oldPassword, String newPassword) {
		Optional<Usuario> user = usuarioRepository.findById(userId);
		if (user.isPresent()) {
			if (passwordEncoder.matches(oldPassword, user.get().getContrasena())) {
				user.get().setContrasena(passwordEncoder.encode(newPassword));
				usuarioRepository.save(user.get());
				return true;
			}
		}
		return false;
	}
}

