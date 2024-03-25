package com.soltel.elex.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name = "expedientes")
public class ExpedientesModel {

	// Atributos (campos BBDD)
	
	@Id
	private int expedientes_id;
	
	@Column
	private String codigo;
	
	@Column
	private LocalDate fecha;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "situacion")
	private Situacion situacion;
	
	@Column
	private String opciones;
	
	@Column
	private String descripcion;
	
	@Column
	private String prioridad;
	
	@Column
	private String ubicacion;
	
	
	@ManyToOne
	@JoinColumn(name = "tipo", referencedColumnName = "tipos_id", nullable = false)
	private TiposExpedienteModel tipoExpediente;
	
	@Column
	private int activo;

	
	
	// Setters y Getters
	
	
	public int getExpedientes_id() {
		return expedientes_id;
	}

	public void setExpedientes_id(int expedientes_id) {
		this.expedientes_id = expedientes_id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Situacion getSituacion() {
		return situacion;
	}

	public void setSituacion(Situacion situacion) {
		this.situacion = situacion;
	}

	public String getOpciones() {
		return opciones;
	}

	public void setOpciones(String opciones) {
		this.opciones = opciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public TiposExpedienteModel getTipoExpediente() {
		return tipoExpediente;
	}

	public void setTipoExpediente(TiposExpedienteModel tipoExpediente) {
		this.tipoExpediente = tipoExpediente;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	
	
	
	//Contructor
	
	public ExpedientesModel() {
		super();
	}

	public ExpedientesModel(int expedientes_id, String codigo, LocalDate fecha, Situacion situacion, String opciones,
			String descripcion, String prioridad, String ubicacion, TiposExpedienteModel tipoExpediente, int activo) {
		super();
		this.expedientes_id = expedientes_id;
		this.codigo = codigo;
		this.fecha = fecha;
		this.situacion = situacion;
		this.opciones = opciones;
		this.descripcion = descripcion;
		this.prioridad = prioridad;
		this.ubicacion = ubicacion;
		this.tipoExpediente = tipoExpediente;
		this.activo = activo;
	}

	
	
	
	
	
	
	
	
	
}
