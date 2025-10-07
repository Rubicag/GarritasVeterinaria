/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de acceso a usuarios (simple, para pruebas).
 */
public interface UsuarioRepository {
	int count();

	List<Usuario> findAll();

	Optional<Usuario> findById(Long id);

	Usuario save(Usuario u);

	void deleteById(Long id);
}
