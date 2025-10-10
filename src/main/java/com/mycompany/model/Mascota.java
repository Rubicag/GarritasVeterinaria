/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Enum para el sexo de la mascota
 */
enum Sexo {
	Macho, Hembra
}

@Entity
@Table(name = "mascota")
public class Mascota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mascota")
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String especie;

	private String raza;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Sexo sexo;

	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column(precision = 5, scale = 2)
	private BigDecimal peso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario propietario;

	// Mantenemos edad como campo calculado o adicional si es necesario
	private Integer edad;

	@OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Cita> citas;

	@OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<HistorialClinico> historialClinico;

	public Mascota() {}

	public Mascota(String nombre, String especie, String raza, Sexo sexo, LocalDate fechaNacimiento, BigDecimal peso, Usuario propietario) {
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.sexo = sexo;
		this.fechaNacimiento = fechaNacimiento;
		this.peso = peso;
		this.propietario = propietario;
	}

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

	public Usuario getPropietario() { return propietario; }
	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	public List<Cita> getCitas() { return citas; }
	public void setCitas(List<Cita> citas) { this.citas = citas; }

	public List<HistorialClinico> getHistorialClinico() { return historialClinico; }
	public void setHistorialClinico(List<HistorialClinico> historialClinico) { this.historialClinico = historialClinico; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Mascota mascota = (Mascota) o;
		return Objects.equals(id, mascota.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Mascota{" + "id=" + id + ", nombre='" + nombre + '\'' + ", especie='" + especie + '\'' + ", propietario=" + (propietario != null ? propietario.getUsuario() : "null") + '}';
	}
}
