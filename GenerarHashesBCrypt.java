import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHashesBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== HASHES BCRYPT PARA CONTRASEÑAS ===\n");
        
        // Generar hashes para las contraseñas comunes
        String[] usuarios = {"admin", "veterinario", "recepcionista"};
        String[] passwords = {"admin123", "veterinario123", "recepcionista123"};
        
        for (int i = 0; i < usuarios.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println("Usuario: " + usuarios[i]);
            System.out.println("Contraseña: " + passwords[i]);
            System.out.println("Hash BCrypt: " + hash);
            System.out.println("Verificación: " + encoder.matches(passwords[i], hash));
            System.out.println();
        }
        
        // SQL para actualizar
        System.out.println("\n=== SQL PARA ACTUALIZAR LA BASE DE DATOS ===\n");
        System.out.println("USE garritas_veterinaria;\n");
        for (int i = 0; i < usuarios.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println("UPDATE usuario SET contrasena = '" + hash + "' WHERE usuario = '" + usuarios[i] + "';");
        }
        
        System.out.println("\n=== CREDENCIALES DE PRUEBA ===");
        System.out.println("Usuario: admin | Contraseña: admin123");
        System.out.println("Usuario: veterinario | Contraseña: veterinario123");
        System.out.println("Usuario: recepcionista | Contraseña: recepcionista123");
    }
}
