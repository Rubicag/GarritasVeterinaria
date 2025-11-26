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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = repo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("========================================");
		logger.info("INTENTO DE LOGIN");
		logger.info("========================================");
		logger.info("Usuario solicitado: '{}'", username);
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
		
		if (usuario.isEmpty()) {
			logger.error("❌ USUARIO NO ENCONTRADO: '{}'", username);
			logger.error("El usuario '{}' no existe en la base de datos", username);
			logger.info("========================================");
			throw new UsernameNotFoundException("Usuario no encontrado: " + username);
		}
		
		Usuario user = usuario.get();
		String role = user.getRol() != null ? user.getRol().getNombre() : "USER";
		
		logger.info("✓ Usuario encontrado en BD:");
		logger.info("  - ID: {}", user.getId());
		logger.info("  - Usuario: '{}'", user.getUsuario());
		logger.info("  - Nombre: {} {}", user.getNombre(), user.getApellido());
		logger.info("  - Email: {}", user.getCorreo());
		logger.info("  - Rol: {}", role);
		logger.info("  - Hash contraseña (primeros 20 chars): {}", 
			user.getContrasena() != null ? user.getContrasena().substring(0, Math.min(20, user.getContrasena().length())) : "NULL");
		logger.info("  - Longitud hash: {}", user.getContrasena() != null ? user.getContrasena().length() : 0);
		logger.info("  - Formato BCrypt: {}", user.getContrasena() != null && user.getContrasena().startsWith("$2a$") ? "SÍ ✓" : "NO ✗");
		logger.info("========================================");
		logger.info("Spring Security validará la contraseña automáticamente");
		logger.info("Si el login falla después de esto, la contraseña es incorrecta");
		logger.info("========================================");
		
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
		Optional<Usuario> user = usuarioRepository.findByUsuario(username);
		if (user.isPresent()) {
			return passwordEncoder.matches(rawPassword, user.get().getContrasena());
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

