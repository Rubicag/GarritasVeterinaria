/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reporte")
public class Reporte {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reporte")
	private Long id;

	private String titulo;

	private String descripcion;

	@Column(name = "fecha_generacion")
	private LocalDateTime fechaGeneracion;

	@Column(name = "id_usuario")
	private Long idUsuario;

	public Reporte() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }
	
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	
	public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
	public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
	
	public Long getIdUsuario() { return idUsuario; }
	public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reporte reporte = (Reporte) o;
		return Objects.equals(id, reporte.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Reporte{" + "id=" + id + ", titulo=" + titulo + ", idUsuario=" + idUsuario + '}';
	}
}
