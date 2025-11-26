/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "mascota")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Mascota {
	
	/**
	 * Enum para el sexo de la mascota
	 */
	public static enum Sexo {
		Macho, Hembra
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mascota")
	private Long id;

	@NotBlank(message = "El nombre de la mascota es obligatorio")
	@Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
	@Column(nullable = false)
	private String nombre;

	@NotBlank(message = "La especie es obligatoria")
	@Size(max = 30, message = "La especie no puede exceder 30 caracteres")
	@Column(nullable = false)
	private String especie;

	@Size(max = 50, message = "La raza no puede exceder 50 caracteres")
	private String raza;

	@NotNull(message = "El sexo es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Sexo sexo;

	@PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column(precision = 5, scale = 2)
	private BigDecimal peso;

	@NotNull(message = "El propietario es obligatorio")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario propietario;

	// Mantenemos edad como campo calculado o adicional si es necesario
	private Integer edad;

	@OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Cita> citas;

	// NOTA: HistorialMedico ahora se relaciona a través de Cita, no directamente con Mascota
	// Para obtener el historial médico de una mascota, usar: HistorialMedicoRepository.findByMascotaId(mascotaId)

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
