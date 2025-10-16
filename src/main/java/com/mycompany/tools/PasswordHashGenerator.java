package com.mycompany.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== PASSWORD HASH GENERATOR ===");
        System.out.println("admin -> " + encoder.encode("admin"));
        System.out.println("secret -> " + encoder.encode("secret"));  
        System.out.println("vetpass -> " + encoder.encode("vetpass"));
        
        // Test the hashes we have in data.sql
        String adminHash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        String secretHash = "$2a$10$fUKdmzJUXWUzKrTiWDieIeRSfqLKpPnveMNc6oYBkj8/7P2DRHfBy";
        String vetpassHash = "$2a$10$7xJKgO4TvHkDGJwfBk/NGuaLvU0.8Ro7jM8xlHzv4T/6lTLOjM.2S";
        
        System.out.println("\n=== VERIFICATION ===");
        System.out.println("admin matches stored hash: " + encoder.matches("admin", adminHash));
        System.out.println("secret matches stored hash: " + encoder.matches("secret", secretHash));
        System.out.println("vetpass matches stored hash: " + encoder.matches("vetpass", vetpassHash));
    }
}