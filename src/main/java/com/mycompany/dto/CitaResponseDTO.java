package com.mycompany.dto;

import com.mycompany.model.Cita;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para la respuesta de citas.
 * Expone solo los datos necesarios sin exponer la estructura interna del modelo.
 */
public class CitaResponseDTO {
    
    private Long id;
    private String mascotaNombre;
    private Long mascotaId;
    private String servicioNombre;
    private Long servicioId;
    private Double servicioPrecio;
    private String veterinarioNombre;
    private Long veterinarioId;
    private LocalDateTime fecha;
    private LocalTime hora;
    private String estado;
    private String observaciones;

    // Constructors
    public CitaResponseDTO() {}

    /**
     * Constructor que convierte una entidad Cita a DTO
     */
    public CitaResponseDTO(Cita cita) {
        this.id = cita.getId();
        
        if (cita.getMascota() != null) {
            this.mascotaId = cita.getMascota().getId();
            this.mascotaNombre = cita.getMascota().getNombre();
        }
        
        if (cita.getServicio() != null) {
            this.servicioId = cita.getServicio().getId();
            this.servicioNombre = cita.getServicio().getNombre();
            this.servicioPrecio = cita.getServicio().getPrecio();
        }
        
        if (cita.getVeterinario() != null) {
            this.veterinarioId = cita.getVeterinario().getId();
            this.veterinarioNombre = cita.getVeterinario().getNombre() + " " + 
                                    cita.getVeterinario().getApellido();
        }
        
        this.fecha = cita.getFecha();
        this.hora = cita.getHora();
        this.estado = cita.getEstado() != null ? cita.getEstado().name() : null;
        this.observaciones = cita.getObservaciones();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMascotaNombre() {
        return mascotaNombre;
    }

    public void setMascotaNombre(String mascotaNombre) {
        this.mascotaNombre = mascotaNombre;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getServicioNombre() {
        return servicioNombre;
    }

    public void setServicioNombre(String servicioNombre) {
        this.servicioNombre = servicioNombre;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Double getServicioPrecio() {
        return servicioPrecio;
    }

    public void setServicioPrecio(Double servicioPrecio) {
        this.servicioPrecio = servicioPrecio;
    }

    public String getVeterinarioNombre() {
        return veterinarioNombre;
    }

    public void setVeterinarioNombre(String veterinarioNombre) {
        this.veterinarioNombre = veterinarioNombre;
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

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "CitaResponseDTO{" +
                "id=" + id +
                ", mascotaNombre='" + mascotaNombre + '\'' +
                ", servicioNombre='" + servicioNombre + '\'' +
                ", veterinarioNombre='" + veterinarioNombre + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}
