package com.mycompany.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO para la creación de citas.
 * Separa la capa de presentación de la capa de modelo.
 */
public class CitaRequestDTO {
    
    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;
    
    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicioId;
    
    private Long veterinarioId; // Opcional, se asigna automáticamente si es null
    
    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fecha;
    
    private String observaciones;

    // Constructors
    public CitaRequestDTO() {}

    public CitaRequestDTO(Long mascotaId, Long servicioId, Long veterinarioId, 
                         LocalDateTime fecha, String observaciones) {
        this.mascotaId = mascotaId;
        this.servicioId = servicioId;
        this.veterinarioId = veterinarioId;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    // Getters and Setters
    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "CitaRequestDTO{" +
                "mascotaId=" + mascotaId +
                ", servicioId=" + servicioId +
                ", veterinarioId=" + veterinarioId +
                ", fecha=" + fecha +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
