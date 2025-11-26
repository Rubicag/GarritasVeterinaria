package com.mycompany.service;

import com.mycompany.model.HistorialMedico;
import com.mycompany.model.Cita;
import com.mycompany.repository.HistorialMedicoRepository;
import com.mycompany.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar el historial médico de las mascotas.
 * Usa la tabla historial_medico.
 */
@Service
@Transactional
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialRepository;
    private final CitaRepository citaRepository;

    public HistorialMedicoService(HistorialMedicoRepository historialRepository,
                                  CitaRepository citaRepository) {
        this.historialRepository = historialRepository;
        this.citaRepository = citaRepository;
    }

    /**
     * Crea un nuevo registro en el historial médico.
     */
    public HistorialMedico crear(Long citaId, String diagnostico, String tratamiento, 
                                String observaciones, MultipartFile archivo) throws IOException {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        HistorialMedico historial = new HistorialMedico();
        historial.setCita(cita);
        historial.setDiagnostico(diagnostico);
        historial.setTratamiento(tratamiento);
        historial.setObservaciones(observaciones);
        historial.setFechaRegistro(LocalDateTime.now());
        historial.setFechaConsulta(LocalDateTime.now());
        historial.setEstado(HistorialMedico.Estado.COMPLETADO);

        if (archivo != null && !archivo.isEmpty()) {
            historial.setArchivo(archivo.getBytes());
            historial.setNombreArchivo(archivo.getOriginalFilename());
            historial.setTipoArchivo(archivo.getContentType());
            historial.setTamanioArchivo(archivo.getSize());
        }

        return historialRepository.save(historial);
    }

    /**
     * Obtiene todos los registros del historial médico.
     */
    public List<HistorialMedico> listarTodos() {
        return historialRepository.findAll();
    }

    /**
     * Obtiene un historial médico por ID.
     */
    public Optional<HistorialMedico> obtenerPorId(Long id) {
        return historialRepository.findById(id);
    }

    /**
     * Obtiene todo el historial médico de una mascota.
     */
    public List<HistorialMedico> obtenerPorMascota(Long mascotaId) {
        return historialRepository.findByMascotaId(mascotaId);
    }

    /**
     * Actualiza un registro del historial médico.
     */
    public HistorialMedico actualizar(Long id, String diagnostico, String tratamiento, 
                                     String observaciones, MultipartFile archivo) throws IOException {
        HistorialMedico historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));

        if (diagnostico != null) {
            historial.setDiagnostico(diagnostico);
        }
        if (tratamiento != null) {
            historial.setTratamiento(tratamiento);
        }
        if (observaciones != null) {
            historial.setObservaciones(observaciones);
        }

        if (archivo != null && !archivo.isEmpty()) {
            historial.setArchivo(archivo.getBytes());
            historial.setNombreArchivo(archivo.getOriginalFilename());
            historial.setTipoArchivo(archivo.getContentType());
            historial.setTamanioArchivo(archivo.getSize());
        }

        return historialRepository.save(historial);
    }

    /**
     * Elimina un registro del historial médico.
     */
    public boolean eliminar(Long id) {
        if (!historialRepository.existsById(id)) {
            return false;
        }
        historialRepository.deleteById(id);
        return true;
    }
}
