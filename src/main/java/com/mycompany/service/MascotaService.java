package com.mycompany.service;

import com.mycompany.model.Mascota;
import com.mycompany.repository.MascotaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MascotaService {

    private final MascotaRepository repo;

    public MascotaService(MascotaRepository repo) {
        this.repo = repo;
    }

    public long count() { return repo.count(); }

    public List<Mascota> findAll() { return repo.findAll(); }

    public Optional<Mascota> findById(Long id) { return repo.findById(id); }

    public Mascota create(Mascota m) { m.setId(null); return repo.save(m); }

    public Mascota update(Long id, Mascota m) {
        return repo.findById(id).map(existing -> {
            existing.setNombre(m.getNombre());
            existing.setEspecie(m.getEspecie());
            existing.setRaza(m.getRaza());
            existing.setEdad(m.getEdad());
            existing.setPropietarioId(m.getPropietarioId());
            return repo.save(existing);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (repo.findById(id).isPresent()) { repo.deleteById(id); return true; }
        return false;
    }
}
