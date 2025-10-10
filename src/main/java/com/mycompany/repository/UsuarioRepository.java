/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

import com.mycompany.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByUsuario(String usuario);
    boolean existsByCorreo(String correo);
    
    // Alias methods for compatibility
    default Optional<Usuario> findByUsername(String username) {
        return findByUsuario(username);
    }
    
    default Optional<Usuario> findByEmail(String email) {
        return findByCorreo(email);
    }
    
    default boolean existsByUsername(String username) {
        return existsByUsuario(username);
    }
    
    default boolean existsByEmail(String email) {
        return existsByCorreo(email);
    }
}
