package com.mycompany.repository;

import com.mycompany.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para el historial médico (tabla historial_medico).
 */
@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {

    /**
     * Busca el historial médico de una mascota por ID (a través de la cita).
     */
    @Query("SELECT h FROM HistorialMedico h WHERE h.cita.mascota.id = :mascotaId ORDER BY h.fechaRegistro DESC")
    List<HistorialMedico> findByMascotaId(@Param("mascotaId") Long mascotaId);

    /**
     * Busca historiales médicos que contienen archivos adjuntos.
     */
    @Query("SELECT h FROM HistorialMedico h WHERE h.archivo IS NOT NULL")
    List<HistorialMedico> findConArchivosAdjuntos();
}
