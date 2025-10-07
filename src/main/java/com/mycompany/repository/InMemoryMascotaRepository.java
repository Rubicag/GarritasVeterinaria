package com.mycompany.repository;

import com.mycompany.model.Mascota;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMascotaRepository {

    private final Map<Long, Mascota> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public InMemoryMascotaRepository() {
        // seed sample mascotas
        save(new Mascota(null, "Firulais", "Perro", "Criollo", 3, 1L, java.time.LocalDateTime.now()));
        save(new Mascota(null, "Michi", "Gato", "Siames", 2, 2L, java.time.LocalDateTime.now()));
    }

    public long count() { return storage.size(); }

    public List<Mascota> findAll() {
        List<Mascota> list = new ArrayList<>(storage.values());
        Collections.sort(list, (a,b) -> Long.compare(a.getId(), b.getId()));
        return list;
    }

    public Optional<Mascota> findById(Long id) { return Optional.ofNullable(storage.get(id)); }

    public Mascota save(Mascota m) {
        if (m.getId() == null) {
            m.setId(idGen.getAndIncrement());
            if (m.getCreatedAt() == null) m.setCreatedAt(java.time.LocalDateTime.now());
        }
        storage.put(m.getId(), m);
        return m;
    }

    public void deleteById(Long id) { storage.remove(id); }
}
