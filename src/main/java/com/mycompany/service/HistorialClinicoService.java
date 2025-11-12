package com.mycompany.service;

import com.mycompany.model.HistorialClinico;
import com.mycompany.model.Mascota;
import com.mycompany.model.Usuario;
import com.mycompany.repository.HistorialClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    /**
     * Obtiene todos los registros del historial clínico
     */
    public List<HistorialClinico> findAll() {
        return historialClinicoRepository.findAll();
    }

    /**
     * Busca un registro por ID
     */
    public Optional<HistorialClinico> findById(Long id) {
        return historialClinicoRepository.findById(id);
    }

    /**
     * Obtiene el historial clínico de una mascota específica
     */
    public List<HistorialClinico> findByMascota(Mascota mascota) {
        return historialClinicoRepository.findByMascota(mascota);
    }

    /**
     * Obtiene el historial clínico ordenado por fecha descendente
     */
    public List<HistorialClinico> findAllOrderByFechaDesc() {
        return historialClinicoRepository.findAllByOrderByFechaDesc();
    }

    /**
     * Guarda o actualiza un registro de historial clínico
     */
    public HistorialClinico save(HistorialClinico historialClinico) {
        // Si es un nuevo registro, establece la fecha actual si no está definida
        if (historialClinico.getId() == null && historialClinico.getFecha() == null) {
            historialClinico.setFecha(LocalDateTime.now());
        }
        
        // Si fechaConsulta no está definida, usa la fecha principal
        if (historialClinico.getFechaConsulta() == null && historialClinico.getFecha() != null) {
            historialClinico.setFechaConsulta(historialClinico.getFecha());
        }
        
        // Si el costo es null, establece 0.00
        if (historialClinico.getCosto() == null) {
            historialClinico.setCosto(BigDecimal.ZERO);
        }
        
        // Estado por defecto si no está definido
        if (historialClinico.getEstado() == null) {
            historialClinico.setEstado(HistorialClinico.EstadoConsulta.COMPLETADO);
        }
        
        // Tipo de consulta por defecto si no está definido
        if (historialClinico.getTipoConsulta() == null) {
            historialClinico.setTipoConsulta(HistorialClinico.TipoConsulta.CONSULTA);
        }
        
        return historialClinicoRepository.save(historialClinico);
    }

    /**
     * Elimina un registro de historial clínico
     */
    public void deleteById(Long id) {
        historialClinicoRepository.deleteById(id);
    }

    /**
     * Cuenta el total de registros
     */
    public long count() {
        return historialClinicoRepository.count();
    }

    /**
     * Obtiene registros por tipo de consulta
     */
    public List<HistorialClinico> findByTipoConsulta(HistorialClinico.TipoConsulta tipoConsulta) {
        return historialClinicoRepository.findByTipoConsulta(tipoConsulta);
    }

    /**
     * Obtiene registros por estado
     */
    public List<HistorialClinico> findByEstado(HistorialClinico.EstadoConsulta estado) {
        return historialClinicoRepository.findByEstado(estado);
    }

    /**
     * Obtiene registros de un veterinario específico
     */
    public List<HistorialClinico> findByVeterinario(Usuario veterinario) {
        return historialClinicoRepository.findByVeterinario(veterinario);
    }

    /**
     * Cuenta registros por tipo de consulta
     */
    public long countByTipoConsulta(HistorialClinico.TipoConsulta tipoConsulta) {
        return historialClinicoRepository.findByTipoConsulta(tipoConsulta).size();
    }

    /**
     * Calcula el costo total de todas las consultas
     */
    public BigDecimal calcularCostoTotal() {
        return historialClinicoRepository.findAll()
            .stream()
            .map(h -> h.getCosto() != null ? h.getCosto() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
