package com.mycompany.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Herramienta para probar la conexión a la base de datos MySQL
 */
public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/garritas_veterinaria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "";
        
        System.out.println("=== Test de Conexión a Base de Datos ===");
        System.out.println("URL: " + url);
        System.out.println("Usuario: " + username);
        System.out.println("Contraseña: " + (password.isEmpty() ? "(vacía)" : "****"));
        System.out.println();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ Driver MySQL cargado correctamente");
            
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Conexión establecida exitosamente");
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("✓ Conexión verificada - Base de datos disponible");
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: Driver MySQL no encontrado");
            System.err.println("  Detalle: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Error de conexión a la base de datos");
            System.err.println("  Código: " + e.getErrorCode());
            System.err.println("  Detalle: " + e.getMessage());
            System.err.println();
            System.err.println("Posibles causas:");
            System.err.println("- XAMPP no está ejecutándose");
            System.err.println("- MySQL no está iniciado en XAMPP");
            System.err.println("- La base de datos 'garritas_veterinaria' no existe");
            System.err.println("- Puerto 3306 no está disponible");
        } catch (Exception e) {
            System.err.println("✗ Error inesperado: " + e.getMessage());
        }
        
        System.out.println("\n=== Fin del Test ===");
    }
}