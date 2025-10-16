package com.mycompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "historialclinico")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota")
    @JsonIgnore
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario")
    @JsonIgnore
    private Usuario veterinario;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "tipo_consulta")
    private String tipoConsulta;

    @Column(name = "motivo_consulta", length = 1000)
    private String motivoConsulta;

    @Column(length = 2000)
    private String diagnostico;
    
    @Column(length = 2000)
    private String tratamiento;
    
    @Column(length = 1000)
    private String medicamentos;
    
    @Column(length = 1000)
    private String observaciones;

    @Column(length = 2000)
    private String notas;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "estado")
    private String estado;

    // Para compatibilidad con el código existente
    private LocalDateTime fecha;

    public HistorialClinico() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    
    // Getters y setters para compatibilidad
    public LocalDateTime getFecha() { 
        return fechaConsulta != null ? fechaConsulta : fecha; 
    }
    public void setFecha(LocalDateTime fecha) { 
        this.fecha = fecha;
        if (this.fechaConsulta == null) {
            this.fechaConsulta = fecha;
        }
    }
    
    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { 
        this.fechaConsulta = fechaConsulta;
        if (this.fecha == null) {
            this.fecha = fechaConsulta;
        }
    }
    
    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    
    public String getTipoConsulta() { return tipoConsulta; }
    public void setTipoConsulta(String tipoConsulta) { this.tipoConsulta = tipoConsulta; }
    
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    
    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialClinico that = (HistorialClinico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HistorialClinico{" + 
               "id=" + id + 
               ", mascota=" + (mascota != null ? mascota.getNombre() : "null") + 
               ", veterinario=" + (veterinario != null ? veterinario.getUsuario() : "null") + 
               ", tipoConsulta=" + tipoConsulta +
               ", fechaConsulta=" + fechaConsulta + 
               ", estado=" + estado + '}';
    }
}

