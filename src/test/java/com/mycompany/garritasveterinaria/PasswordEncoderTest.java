package com.mycompany.garritasveterinaria;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para verificar que BCrypt funciona correctamente
 */
public class PasswordEncoderTest {
    
    @Test
    public void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash que debe estar en la base de datos
        String hashEnBaseDeDatos = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        // Contraseña en texto plano que el usuario escribe
        String passwordUsuario = "123456";
        
        // Verificar que coinciden
        boolean matches = encoder.matches(passwordUsuario, hashEnBaseDeDatos);
        
        System.out.println("========================================");
        System.out.println("PRUEBA DE BCRYPT PASSWORD ENCODER");
        System.out.println("========================================");
        System.out.println("Password ingresado: " + passwordUsuario);
        System.out.println("Hash en BD: " + hashEnBaseDeDatos);
        System.out.println("¿Coinciden?: " + matches);
        System.out.println("========================================");
        
        assertTrue(matches, "El password '123456' debe coincidir con el hash BCrypt");
    }
    
    @Test
    public void testGenerateNewHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "123456";
        String newHash = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("GENERAR NUEVO HASH");
        System.out.println("========================================");
        System.out.println("Password: " + password);
        System.out.println("Nuevo Hash: " + newHash);
        System.out.println("Longitud: " + newHash.length());
        System.out.println("========================================");
        
        // Verificar que el nuevo hash también funciona
        assertTrue(encoder.matches(password, newHash));
    }
}
