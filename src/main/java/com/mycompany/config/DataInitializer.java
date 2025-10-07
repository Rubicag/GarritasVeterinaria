package com.mycompany.config;

import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import com.mycompany.repository.JpaUsuarioRepository;
import com.mycompany.repository.MascotaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("h2")
public class DataInitializer {

    @Bean
    public CommandLineRunner seed(JpaUsuarioRepository userRepo, MascotaRepository mascotaRepo) {
        return args -> {
            // seed users
            if (userRepo.count() == 0) {
                userRepo.save(new Usuario(null, "admin", "admin@example.com", "ADMIN"));
                userRepo.save(new Usuario(null, "user1", "user1@example.com", "USER"));
                userRepo.save(new Usuario(null, "user2", "user2@example.com", "USER"));
            }
            // seed mascotas
            if (mascotaRepo.count() == 0) {
                mascotaRepo.save(new Mascota(null, "Firulais", "Perro", "Criollo", 3, 1L, null));
                mascotaRepo.save(new Mascota(null, "Michi", "Gato", "Siames", 2, 2L, null));
            }
        };
    }
}
