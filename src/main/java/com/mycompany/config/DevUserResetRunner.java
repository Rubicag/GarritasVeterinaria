package com.mycompany.config;

import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Dev runner para restablecer la contraseña del usuario 'admin' a un valor conocido.
 * Solo se ejecuta cuando se arranca la app con la propiedad `app.dev.resetAdmin=true`.
 */
@Component
@ConditionalOnProperty(name = "app.dev.resetAdmin", havingValue = "true")
public class DevUserResetRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DevUserResetRunner.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DevUserResetRunner(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Optional<Usuario> maybe = usuarioRepository.findByUsuario("admin");
        if (maybe.isPresent()) {
            Usuario admin = maybe.get();
            admin.setContrasena(passwordEncoder.encode("123456"));
            usuarioRepository.save(admin);
            LOG.info("DevUserResetRunner: contraseña del usuario 'admin' establecida a '123456' (solo desarrollo)");
        } else {
            LOG.warn("DevUserResetRunner: usuario 'admin' no encontrado, no se realizó cambio");
        }
    }
}
