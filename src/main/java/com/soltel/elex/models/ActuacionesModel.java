package com.soltel.elex.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name = "actuaciones")
public class ActuacionesModel {
	
	
	// Atributos (campos BBDD)
	
		@Id
		private int actuaciones_id;
		
		@Column
		private String descripcion;
		
		@Column
		private int finalizado;
		 
		@Column
		private LocalDate fecha;
		
		@Column
		private String modalidad;
		
		@Column
		private int activo;
		
		@ManyToOne
		@JoinColumn(name = "expedientes_id", nullable = false)
		private ExpedientesModel expediente;

		
		// Setters y Getters
		
		
		public int getId() {
			return actuaciones_id;
		}

		public void setId(int id) {
			this.actuaciones_id = id;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public int getFinalizado() {
			return finalizado;
		}

		public void setFinalizado(int finalizado) {
			this.finalizado = finalizado;
		}

		public LocalDate getFecha() {
			return fecha;
		}

		public void setFecha(LocalDate fecha) {
			this.fecha = fecha;
		}

		public String getModalidad() {
			return modalidad;
		}

		public void setModalidad(String modalidad) {
			this.modalidad = modalidad;
		}

		public int getActivo() {
			return activo;
		}

		public void setActivo(int activo) {
			this.activo = activo;
		}

		public ExpedientesModel getExpediente() {
			return expediente;
		}

		public void setExpediente(ExpedientesModel expediente) {
			this.expediente = expediente;
		}

		
		
		//Constructor
		
		public ActuacionesModel() {
			super();
		}
		
		
		public ActuacionesModel(int id, String descripcion, int finalizado, String modalidad,LocalDate fecha, int activo,
				ExpedientesModel expediente) {
			this.actuaciones_id = id;
			this.descripcion = descripcion;
			this.finalizado = finalizado;
			this.fecha = fecha;
			this.modalidad = modalidad;
			this.activo = activo;
			this.expediente = expediente;
		}
		
		
		
		
		
}
