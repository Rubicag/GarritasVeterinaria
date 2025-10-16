package com.mycompany.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar y verificar hashes BCrypt
 * Esta clase es solo para desarrollo y testing
 */
public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("Generando hashes BCrypt:");
        System.out.println("admin -> " + encoder.encode("admin"));
        System.out.println("secret -> " + encoder.encode("secret"));
        System.out.println("vetpass -> " + encoder.encode("vetpass"));
        
        // Verificar el hash actual
        String currentHash = "$2a$10$DyVkSTzrlwXY4KQAJ5KjfO7RSV9x7a3W9dBlq5PSN.WqSmZLgXuKa";
        System.out.println("\nVerificando hash actual:");
        System.out.println("Hash actual matches 'admin': " + encoder.matches("admin", currentHash));
        System.out.println("Hash actual matches 'secret': " + encoder.matches("secret", currentHash));
        System.out.println("Hash actual matches 'vetpass': " + encoder.matches("vetpass", currentHash));
        System.out.println("Hash actual matches 'hello': " + encoder.matches("hello", currentHash));
    }
}