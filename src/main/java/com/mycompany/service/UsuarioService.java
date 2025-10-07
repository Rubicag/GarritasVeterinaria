/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Usuario;
import com.mycompany.repository.JpaUsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Servicio de usuario (ahora usando Spring Data JPA).
 */
@Service
public class UsuarioService {

	private final JpaUsuarioRepository usuarioRepository;

	public UsuarioService(JpaUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
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
		u.setId(null); // ensure new
		return usuarioRepository.save(u);
	}

	public Usuario update(Long id, Usuario u) {
		Optional<Usuario> existing = usuarioRepository.findById(id);
		if (existing.isEmpty()) {
			return null;
		}
		Usuario ex = existing.get();
		ex.setUsername(u.getUsername());
		ex.setEmail(u.getEmail());
		ex.setRole(u.getRole());
		ex.setActive(u.isActive());
		return usuarioRepository.save(ex);
	}

	public boolean delete(Long id) {
		Optional<Usuario> existing = usuarioRepository.findById(id);
		if (existing.isEmpty()) return false;
		usuarioRepository.deleteById(id);
		return true;
	}
}

