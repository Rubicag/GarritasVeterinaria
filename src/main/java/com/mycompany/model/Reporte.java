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

	@Column(name = "tipo_reporte")
	private String tipoReporte;

	@Column(name = "fecha_generacion")
	private LocalDateTime fechaGeneracion;

	private String contenido;

	@Column(name = "id_usuario_generador")
	private Long idUsuarioGenerador;

	public Reporte() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getTipoReporte() { return tipoReporte; }
	public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
	
	public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
	public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
	
	public String getContenido() { return contenido; }
	public void setContenido(String contenido) { this.contenido = contenido; }
	
	public Long getIdUsuarioGenerador() { return idUsuarioGenerador; }
	public void setIdUsuarioGenerador(Long idUsuarioGenerador) { this.idUsuarioGenerador = idUsuarioGenerador; }

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
		return "Reporte{" + "id=" + id + ", tipoReporte=" + tipoReporte + ", idUsuarioGenerador=" + idUsuarioGenerador + '}';
	}
}
