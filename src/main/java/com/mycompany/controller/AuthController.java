package com.mycompany.controller;

import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username y password son requeridos"));
            }

            // Verificar si el usuario existe
            Optional<Usuario> userOpt = usuarioService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no encontrado"));
            }

            Usuario user = userOpt.get();
            boolean authenticated = usuarioService.authenticateUser(username, password);
            
            if (authenticated) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Autenticación exitosa",
                    "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsuario(),
                        "email", user.getCorreo(),
                        "role", user.getRol() != null ? user.getRol().getNombre() : "USER"
                    )
                ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            Usuario newUser = usuarioService.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Usuario registrado exitosamente",
                "user", Map.of(
                    "id", newUser.getId(),
                    "username", newUser.getUsuario(),
                    "email", newUser.getCorreo(),
                    "role", newUser.getRol() != null ? newUser.getRol().getNombre() : "USER"
                )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
        try {
            Long userId = Long.valueOf(passwordData.get("userId"));
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");

            boolean changed = usuarioService.changePassword(userId, oldPassword, newPassword);
            
            if (changed) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Contraseña cambiada exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Contraseña actual incorrecta"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String username) {
        try {
            Optional<Usuario> user = usuarioService.findByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "user", Map.of(
                        "id", user.get().getId(),
                        "username", user.get().getUsuario(),
                        "role", user.get().getRol() != null ? user.get().getRol().getNombre() : "USER"
                    )
                ));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "error", "Token inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
