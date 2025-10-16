/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String apellido;

	@Column(nullable = false, unique = true, length = 8)
	private String dni;

	@Column(nullable = false, unique = true)
	private String correo;

	private String telefono;

	private String direccion;

	@Column(nullable = false, unique = true)
	private String usuario;

	@Column(nullable = false)
	private String contrasena;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_rol")
	private Rol rol;

	@OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Mascota> mascotas;

	public Usuario() {}

	public Usuario(Long id, String nombre, String apellido, String dni, String correo, String usuario, String contrasena, Rol rol) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.correo = correo;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.rol = rol;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getApellido() { return apellido; }
	public void setApellido(String apellido) { this.apellido = apellido; }

	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public String getCorreo() { return correo; }
	public void setCorreo(String correo) { this.correo = correo; }

	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }

	public String getDireccion() { return direccion; }
	public void setDireccion(String direccion) { this.direccion = direccion; }

	public String getUsuario() { return usuario; }
	public void setUsuario(String usuario) { this.usuario = usuario; }

	public String getContrasena() { return contrasena; }
	public void setContrasena(String contrasena) { this.contrasena = contrasena; }

	public Rol getRol() { return rol; }
	public void setRol(Rol rol) { this.rol = rol; }

	public List<Mascota> getMascotas() { return mascotas; }
	public void setMascotas(List<Mascota> mascotas) { this.mascotas = mascotas; }

	@Override
	public String toString() {
		return "Usuario{" + "id=" + id + ", usuario='" + usuario + '\'' + ", correo='" + correo + '\'' + ", rol='" + (rol != null ? rol.getNombre() : "null") + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
