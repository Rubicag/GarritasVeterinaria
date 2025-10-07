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
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mascotas")
public class Mascota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	private String especie;

	private String raza;

	private Integer edad;

	private Long propietarioId; // referencia simple al usuario

	private LocalDateTime createdAt;

	public Mascota() {}

	public Mascota(Long id, String nombre, String especie, String raza, Integer edad, Long propietarioId, LocalDateTime createdAt) {
		this.id = id;
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.edad = edad;
		this.propietarioId = propietarioId;
		this.createdAt = createdAt;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getEspecie() { return especie; }
	public void setEspecie(String especie) { this.especie = especie; }

	public String getRaza() { return raza; }
	public void setRaza(String raza) { this.raza = raza; }

	public Integer getEdad() { return edad; }
	public void setEdad(Integer edad) { this.edad = edad; }

	public Long getPropietarioId() { return propietarioId; }
	public void setPropietarioId(Long propietarioId) { this.propietarioId = propietarioId; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
