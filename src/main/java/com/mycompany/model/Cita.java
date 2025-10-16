package com.mycompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota")
    @JsonIgnore
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario")
    private Usuario veterinario;

    private LocalDateTime fecha;

    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    private String observaciones;

    public enum EstadoCita {
        Pendiente, Atendida, Cancelada
    }

    public Cita() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    
    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cita cita = (Cita) o;
        return Objects.equals(id, cita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cita{" + "id=" + id + ", mascota=" + (mascota != null ? mascota.getNombre() : "null") + 
               ", servicio=" + (servicio != null ? servicio.getNombre() : "null") + ", fecha=" + fecha + '}';
    }
}

