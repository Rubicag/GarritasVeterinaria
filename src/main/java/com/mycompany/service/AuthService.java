package com.mycompany.service;

import com.mycompany.model.Usuario;
import com.mycompany.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "jwt.enabled", havingValue = "true", matchIfMissing = true)
public class AuthService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired(required = false)
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica un usuario y genera un token JWT
     */
    public Map<String, Object> authenticate(String username, String password) {
        try {
            // Intentar autenticación con Spring Security
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Si la autenticación es exitosa, obtener el usuario
            Optional<Usuario> usuario = usuarioService.findByUsername(username);
            if (usuario.isPresent()) {
                Usuario user = usuario.get();
                
                // Generar token JWT con rol
                String token = jwtUtil.generateTokenWithRole(username, user.getRol().getNombre());
                
                // Preparar respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Autenticación exitosa");
                response.put("token", token);
                response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsuario(),
                    "email", user.getCorreo(),
                    "role", user.getRol().getNombre(),
                    "active", true
                ));
                response.put("expiresIn", jwtUtil.getTokenRemainingTime(token));
                
                return response;
            } else {
                throw new BadCredentialsException("Usuario no encontrado");
            }

        } catch (AuthenticationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            response.put("error", e.getMessage());
            return response;
        }
    }

    /**
     * Registra un nuevo usuario
     */
    public Map<String, Object> register(String username, String email, String password, String role) {
        try {
            // Validar que el usuario no exista
            if (usuarioService.findByUsername(username).isPresent()) {
                return Map.of(
                    "success", false,
                    "message", "El nombre de usuario ya existe"
                );
            }

            if (usuarioService.findByEmail(email).isPresent()) {
                return Map.of(
                    "success", false,
                    "message", "El email ya está registrado"
                );
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsuario(username);
            nuevoUsuario.setCorreo(email);
            nuevoUsuario.setContrasena(password); // Se encripta en el servicio
            
            // Crear y asignar rol
            com.mycompany.model.Rol userRole = new com.mycompany.model.Rol();
            userRole.setNombre(role != null ? role : "USER");
            nuevoUsuario.setRol(userRole);

            Usuario usuarioCreado = usuarioService.create(nuevoUsuario);

            // Generar token automáticamente
            String token = jwtUtil.generateTokenWithRole(username, usuarioCreado.getRol().getNombre());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("token", token);
            response.put("user", Map.of(
                "id", usuarioCreado.getId(),
                "username", usuarioCreado.getUsuario(),
                "email", usuarioCreado.getCorreo(),
                "role", usuarioCreado.getRol().getNombre(),
                "active", true
            ));

            return response;

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error al registrar usuario: " + e.getMessage()
            );
        }
    }

    /**
     * Valida un token JWT
     */
    public Map<String, Object> validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            
            if (jwtUtil.validateToken(token, username)) {
                Optional<Usuario> usuario = usuarioService.findByUsername(username);
                if (usuario.isPresent()) {
                    Usuario user = usuario.get();
                    return Map.of(
                        "valid", true,
                        "username", username,
                        "role", user.getRol().getNombre(),
                        "expiresIn", jwtUtil.getTokenRemainingTime(token)
                    );
                }
            }
            
            return Map.of("valid", false, "message", "Token inválido");
            
        } catch (Exception e) {
            return Map.of("valid", false, "message", "Error al validar token: " + e.getMessage());
        }
    }

    /**
     * Cambia la contraseña de un usuario
     */
    public Map<String, Object> changePassword(String username, String oldPassword, String newPassword) {
        try {
            Optional<Usuario> usuario = usuarioService.findByUsername(username);
            if (usuario.isEmpty()) {
                return Map.of(
                    "success", false,
                    "message", "Usuario no encontrado"
                );
            }

            Usuario user = usuario.get();
            
            // Verificar contraseña actual
            if (!passwordEncoder.matches(oldPassword, user.getContrasena())) {
                return Map.of(
                    "success", false,
                    "message", "Contraseña actual incorrecta"
                );
            }

            // Cambiar contraseña
            boolean changed = usuarioService.changePassword(user.getId(), oldPassword, newPassword);
            
            if (changed) {
                return Map.of(
                    "success", true,
                    "message", "Contraseña cambiada exitosamente"
                );
            } else {
                return Map.of(
                    "success", false,
                    "message", "Error al cambiar la contraseña"
                );
            }

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error al cambiar contraseña: " + e.getMessage()
            );
        }
    }

    /**
     * Refresca un token JWT
     */
    public Map<String, Object> refreshToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            
            if (jwtUtil.validateToken(token, username)) {
                Optional<Usuario> usuario = usuarioService.findByUsername(username);
                if (usuario.isPresent()) {
                    Usuario user = usuario.get();
                    String newToken = jwtUtil.generateTokenWithRole(username, user.getRol().getNombre());
                    
                    return Map.of(
                        "success", true,
                        "token", newToken,
                        "expiresIn", jwtUtil.getTokenRemainingTime(newToken)
                    );
                }
            }
            
            return Map.of("success", false, "message", "Token inválido para renovar");
            
        } catch (Exception e) {
            return Map.of("success", false, "message", "Error al renovar token: " + e.getMessage());
        }
    }
}
