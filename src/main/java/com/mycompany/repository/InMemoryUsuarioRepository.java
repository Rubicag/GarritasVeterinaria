package com.mycompany.repository;

import com.mycompany.model.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

/**
 * Implementación en memoria simple para pruebas locales y CI.
 */
@Repository
public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final Map<Long, Usuario> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public InMemoryUsuarioRepository() {
        // seed with a few users for demo/testing
        save(new Usuario(null, "admin", "admin@example.com", "ADMIN"));
        save(new Usuario(null, "user1", "user1@example.com", "USER"));
        save(new Usuario(null, "user2", "user2@example.com", "USER"));
    }

    @Override
    public int count() {
        return storage.size();
    }

    @Override
    public List<Usuario> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Usuario save(Usuario u) {
        if (u.getId() == null) {
            long id = idGen.getAndIncrement();
            u.setId(id);
        }
        storage.put(u.getId(), u);
        return u;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}

