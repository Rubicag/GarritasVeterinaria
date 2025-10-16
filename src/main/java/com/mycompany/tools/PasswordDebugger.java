package com.mycompany.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Herramienta para probar directamente el hash BCrypt
 * y verificar si la contraseña coincide
 */
public class PasswordDebugger {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash almacenado en la base de datos
        String hashFromDB = "$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW";
        
        // Contraseñas a probar
        String[] passwordsToTest = {
            "123456",
            "admin", 
            "admin123",
            "password",
            ""
        };
        
        System.out.println("=== DEBUG DE CONTRASEÑAS BCrypt ===");
        System.out.println("Hash en BD: " + hashFromDB);
        System.out.println("Longitud del hash: " + hashFromDB.length());
        System.out.println();
        
        for (String password : passwordsToTest) {
            boolean matches = encoder.matches(password, hashFromDB);
            System.out.println("Contraseña: '" + password + "' -> " + (matches ? "✅ COINCIDE" : "❌ NO COINCIDE"));
        }
        
        System.out.println();
        System.out.println("=== GENERANDO NUEVO HASH PARA '123456' ===");
        String newHash = encoder.encode("123456");
        System.out.println("Nuevo hash: " + newHash);
        System.out.println("Verificación: " + encoder.matches("123456", newHash));
        
        System.out.println();
        System.out.println("=== VERIFICANDO FORMATO DEL HASH ===");
        System.out.println("Comienza con $2a$: " + hashFromDB.startsWith("$2a$"));
        System.out.println("Comienza con $2b$: " + hashFromDB.startsWith("$2b$"));
        System.out.println("Comienza con $2y$: " + hashFromDB.startsWith("$2y$"));
    }
}