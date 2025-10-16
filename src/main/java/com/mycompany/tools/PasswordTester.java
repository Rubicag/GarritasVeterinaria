package com.mycompany.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Herramienta para verificar y generar contraseñas BCrypt
 */
public class PasswordTester {
    
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Contraseña original
        String rawPassword = "123456";
        
        // Generar hash BCrypt
        String hashedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Contraseña original: " + rawPassword);
        System.out.println("Hash BCrypt generado: " + hashedPassword);
        System.out.println("Longitud del hash: " + hashedPassword.length());
        
        // Verificar si el hash funciona
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
        System.out.println("¿Coincide la contraseña? " + matches);
        
        // Probar con algunos hashes que deberían estar en la BD
        String[] testHashes = {
            "$2a$10$YhPUX.ZhZf8U1V5mD5jRm.J3QUV5fJ8XvN8CdGhJQNOiRZ1i1GhyG", // admin
            "$2a$10$ZrEb7h8yY1Ey2DJyOLFj.eK7Y8fA3xJh7S9yLgJfO3mQ8dP1b4XzS", // veterinario
            "$2a$10$8pE9xDQXgT2fE9mF4tN3R.b7mC9eN8sK1wT6vS3uG7pL2qA5yB9nH"  // doctora
        };
        
        System.out.println("\n--- Probando hashes predefinidos ---");
        for (int i = 0; i < testHashes.length; i++) {
            boolean testMatch = passwordEncoder.matches(rawPassword, testHashes[i]);
            System.out.println("Hash " + (i+1) + ": " + testMatch);
        }
        
        // Generar nuevos hashes para cada usuario
        System.out.println("\n--- Generando nuevos hashes ---");
        String[] users = {"admin", "veterinario", "doctora", "cliente1", "recepcion"};
        for (String user : users) {
            String newHash = passwordEncoder.encode(rawPassword);
            System.out.println("UPDATE usuarios SET contrasena = '" + newHash + "' WHERE usuario = '" + user + "';");
        }
    }
}