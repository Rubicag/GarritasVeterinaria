package com.mycompany.dto;

import com.mycompany.model.Sexo;
import java.time.LocalDate;
import java.math.BigDecimal;

public class MascotaDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Sexo sexo;
    private LocalDate fechaNacimiento;
    private BigDecimal peso;
    private Integer edad;
    private String color;
    private String observaciones;
    
    // Información del propietario
    private Long propietarioId;
    private String propietarioNombre;
    private String propietarioEmail;
    
    public MascotaDTO() {}
    
    // Constructor completo
    public MascotaDTO(Long id, String nombre, String especie, String raza, Sexo sexo, 
                     LocalDate fechaNacimiento, BigDecimal peso, Integer edad, 
                     Long propietarioId, String propietarioNombre, String propietarioEmail) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.edad = edad;
        this.propietarioId = propietarioId;
        this.propietarioNombre = propietarioNombre;
        this.propietarioEmail = propietarioEmail;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public Long getPropietarioId() { return propietarioId; }
    public void setPropietarioId(Long propietarioId) { this.propietarioId = propietarioId; }
    
    public String getPropietarioNombre() { return propietarioNombre; }
    public void setPropietarioNombre(String propietarioNombre) { this.propietarioNombre = propietarioNombre; }
    
    public String getPropietarioEmail() { return propietarioEmail; }
    public void setPropietarioEmail(String propietarioEmail) { this.propietarioEmail = propietarioEmail; }
}