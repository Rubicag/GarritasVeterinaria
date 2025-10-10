package com.mycompany.service;

import com.mycompany.model.Reporte;
import com.mycompany.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte create(Reporte reporte) {
        reporte.setFechaGeneracion(LocalDateTime.now());
        return reporteRepository.save(reporte);
    }

    public Optional<Reporte> update(Long id, Reporte reporteDetails) {
        Optional<Reporte> reporteExistente = reporteRepository.findById(id);
        if (reporteExistente.isPresent()) {
            Reporte reporte = reporteExistente.get();
            reporte.setTipoReporte(reporteDetails.getTipoReporte());
            reporte.setContenido(reporteDetails.getContenido());
            reporte.setIdUsuarioGenerador(reporteDetails.getIdUsuarioGenerador());
            return Optional.of(reporteRepository.save(reporte));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (reporteRepository.existsById(id)) {
            reporteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Reporte> findByUsuarioGenerador(Long usuarioId) {
        return reporteRepository.findByIdUsuarioGenerador(usuarioId);
    }

    public List<Reporte> findByTipoReporte(String tipoReporte) {
        return reporteRepository.findByTipoReporte(tipoReporte);
    }

    public List<Reporte> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return reporteRepository.findByFechaGeneracionBetween(fechaInicio, fechaFin);
    }
}