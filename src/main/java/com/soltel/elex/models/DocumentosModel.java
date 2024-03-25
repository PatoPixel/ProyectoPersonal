package com.soltel.elex.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "documentos")
public class DocumentosModel {

	// Atributos (campos BBDD)
	
			@Id
			private int documentos_id;
			
			@Column
			private String ruta;
			
			@Column
			private float tarifa;
			
			@Column
			private String categoria;
			 
			@Column
			private int activo;
			
			@ManyToOne
			@JoinColumn(name = "expedientes_id", nullable = false)
			private ExpedientesModel expediente;

			public int getId() {
				return documentos_id;
			}

			public void setId(int id) {
				this.documentos_id = id;
			}

			public String getRuta() {
				return ruta;
			}

			public void setRuta(String ruta) {
				this.ruta = ruta;
			}

			public float getTarifa() {
				return tarifa;
			}

			public void setTarifa(float tarifa) {
				this.tarifa = tarifa;
			}

			public int getActivo() {
				return activo;
			}

			public String getCategoria() {
				return categoria;
			}

			public void setCategoria(String categoria) {
				this.categoria = categoria;
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

			
			
			
			// Constructores
			
			public DocumentosModel() {
				super();
			}
			
			
			public DocumentosModel(int id, String ruta, float tarifa, int activo, String categoria ,ExpedientesModel expediente) {
				super();
				this.documentos_id = id;
				this.ruta = ruta;
				this.tarifa = tarifa;
				this.categoria = categoria;
				this.activo = activo;
				this.expediente = expediente;
			}

			
			
	
}
