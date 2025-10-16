package com.mycompany.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Herramienta para generar y actualizar el hash correcto de BCrypt
 */
public class FixPasswordHash {
    
    public static void main(String[] args) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            
            // Generar hash correcto para "123456"
            String password = "123456";
            String correctHash = encoder.encode(password);
            
            System.out.println("=== GENERANDO HASH CORRECTO ===");
            System.out.println("Contraseña: " + password);
            System.out.println("Hash generado: " + correctHash);
            
            // Verificar que el hash funciona
            boolean verification = encoder.matches(password, correctHash);
            System.out.println("Verificación: " + (verification ? "✅ CORRECTO" : "❌ ERROR"));
            
            if (verification) {
                // Conectar a MySQL y actualizar
                String url = "jdbc:mysql://localhost:3306/garritas_veterinaria";
                String username = "root";
                String dbPassword = "";
                
                Connection conn = DriverManager.getConnection(url, username, dbPassword);
                
                String sql = "UPDATE usuario SET contrasena = ? WHERE usuario = 'admin'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, correctHash);
                
                int rows = stmt.executeUpdate();
                System.out.println("\n=== ACTUALIZANDO BASE DE DATOS ===");
                System.out.println("Filas actualizadas: " + rows);
                
                if (rows > 0) {
                    System.out.println("✅ Hash actualizado correctamente en la base de datos!");
                    System.out.println("Ahora puedes usar: admin / 123456");
                }
                
                stmt.close();
                conn.close();
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}