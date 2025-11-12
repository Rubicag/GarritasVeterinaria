package com.mycompany.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "historialclinico")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario")
    private Usuario veterinario;

    private LocalDateTime fecha;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta")
    private TipoConsulta tipoConsulta;

    @Column(name = "motivo_consulta", length = 500)
    private String motivoConsulta;

    private String diagnostico;
    
    private String tratamiento;

    @Column(length = 500)
    private String medicamentos;
    
    private String observaciones;

    @Enumerated(EnumType.STRING)
    private EstadoConsulta estado;

    private BigDecimal costo;

    private String notas;

    // Enums
    public enum TipoConsulta {
        CONSULTA, VACUNACION, CIRUGIA, EMERGENCIA, CONTROL
    }

    public enum EstadoConsulta {
        COMPLETADO, PENDIENTE, EN_PROGRESO
    }

    public HistorialClinico() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }

    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public TipoConsulta getTipoConsulta() { return tipoConsulta; }
    public void setTipoConsulta(TipoConsulta tipoConsulta) { this.tipoConsulta = tipoConsulta; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public EstadoConsulta getEstado() { return estado; }
    public void setEstado(EstadoConsulta estado) { this.estado = estado; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

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
        return "HistorialClinico{" + "id=" + id + ", mascota=" + (mascota != null ? mascota.getNombre() : "null") + ", fecha=" + fecha + '}';
    }
}

