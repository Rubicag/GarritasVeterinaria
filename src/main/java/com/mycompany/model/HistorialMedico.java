package com.mycompany.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para el historial médico de las mascotas.
 * Mapea la tabla historial_medico.
 * Relacionada con Cita (cada historial pertenece a una cita).
 */
@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    public enum TipoConsulta {
        CONSULTA, VACUNACION, CIRUGIA, EMERGENCIA, CONTROL
    }

    public enum Estado {
        COMPLETADO, PENDIENTE, EN_PROGRESO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", columnDefinition = "ENUM('CONSULTA','VACUNACION','CIRUGIA','EMERGENCIA','CONTROL')")
    private TipoConsulta tipoConsulta = TipoConsulta.CONSULTA;

    @Column(name = "motivo_consulta", length = 500)
    private String motivoConsulta;

    @Column(name = "diagnostico", length = 500, nullable = false)
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "medicamentos", length = 500)
    private String medicamentos;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('COMPLETADO','PENDIENTE','EN_PROGRESO')")
    private Estado estado = Estado.COMPLETADO;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo = BigDecimal.ZERO;

    // Archivos adjuntos (radiografías, análisis, etc.) - Campos opcionales
    @Lob
    @Column(name = "archivo")
    private byte[] archivo;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "tipo_archivo", length = 100)
    private String tipoArchivo;

    @Column(name = "tamanio_archivo")
    private Long tamanioArchivo;

    // Constructores
    public HistorialMedico() {
        this.fechaRegistro = LocalDateTime.now();
        this.fechaConsulta = LocalDateTime.now();
        this.tipoConsulta = TipoConsulta.CONSULTA;
        this.estado = Estado.COMPLETADO;
        this.costo = BigDecimal.ZERO;
    }

    public HistorialMedico(Cita cita, String diagnostico, String tratamiento) {
        this();
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public TipoConsulta getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Long getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Long tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    @Override
    public String toString() {
        return "HistorialMedico{" +
                "id=" + id +
                ", diagnostico='" + diagnostico + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
