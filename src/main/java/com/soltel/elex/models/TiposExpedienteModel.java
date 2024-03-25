package com.soltel.elex.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "tipos_expediente")

public class TiposExpedienteModel {

	// Atributos (campos BBDD)
	
	@Id
	private int tipos_id;
	
	@Column
	private String materia;
	
	@Column
	private int activo;

	// Getters y Setters
	
	public int getId() {
		return tipos_id;
	}

	public void setId(int id) {
		this.tipos_id = id;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	// Constructor 
	
	public TiposExpedienteModel() {}
	
	public TiposExpedienteModel(String materia) {
	    this(materia, 1); // Se establece el valor predeterminado a 1
	}
	
	public TiposExpedienteModel(String materia, int activo) {
		this.materia = materia;
		this.activo = activo;
	}
	
	
}
