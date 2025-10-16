package com.mycompany.service;

import com.mycompany.model.HistorialClinico;
import com.mycompany.model.Mascota;
import com.mycompany.repository.HistorialClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialRepository;

    public List<HistorialClinico> findAll() {
        return historialRepository.findAll();
    }

    public Optional<HistorialClinico> findById(Long id) {
        return historialRepository.findById(id);
    }

    public HistorialClinico create(HistorialClinico historial) {
        if (historial.getFecha() == null) {
            historial.setFecha(LocalDateTime.now());
        }
        return historialRepository.save(historial);
    }

    public HistorialClinico update(Long id, HistorialClinico historialDetails) {
        Optional<HistorialClinico> existente = historialRepository.findById(id);
        if (existente.isPresent()) {
            HistorialClinico historial = existente.get();
            historial.setDiagnostico(historialDetails.getDiagnostico());
            historial.setTratamiento(historialDetails.getTratamiento());
            historial.setObservaciones(historialDetails.getObservaciones());
            historial.setNotas(historialDetails.getNotas());
            if (historialDetails.getFecha() != null) {
                historial.setFecha(historialDetails.getFecha());
            }
            return historialRepository.save(historial);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (historialRepository.existsById(id)) {
            historialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos específicos del historial clínico
    public List<HistorialClinico> findByMascota(Mascota mascota) {
        return historialRepository.findByMascotaOrderByFechaDesc(mascota);
    }

    public List<HistorialClinico> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return historialRepository.findByFechaBetween(inicio, fin);
    }

    public List<HistorialClinico> findByNotasContaining(String texto) {
        return historialRepository.findByNotasContaining(texto);
    }

    // Métodos para estadísticas
    public long count() {
        return historialRepository.count();
    }

    public long countByMascota(Mascota mascota) {
        return historialRepository.findByMascota(mascota).size();
    }
}