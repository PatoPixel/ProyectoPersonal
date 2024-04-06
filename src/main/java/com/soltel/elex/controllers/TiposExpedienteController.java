package com.soltel.elex.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soltel.elex.models.TiposExpedienteModel;
import com.soltel.elex.services.TiposExpedienteService;

import io.swagger.v3.oas.annotations.Hidden;


@RestController
@RequestMapping("/tiposexpediente")
@CrossOrigin(origins = "http://localhost:4200")
public class TiposExpedienteController {

	private final TiposExpedienteService tiposService;
	
	public TiposExpedienteController (TiposExpedienteService tiposService) {
		this.tiposService = tiposService;
	}
	
	//Consultar
	
	
		//FindALL
	
	@GetMapping("/consultar")
	public ResponseEntity<List<TiposExpedienteModel>> getAllTipos(){
		return ResponseEntity.ok(tiposService.findAllTipos());
	}
	
	
	 	//FinByMateria
	@GetMapping("/consultar/{materia}")
	public ResponseEntity<?> getByMateria(@PathVariable String materia) {
		Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
		if (tipoExpediente.isPresent()) {
			return ResponseEntity.ok(tipoExpediente.get());
		}else {
			String mensaje = "No se encontró ninguna materia con el nombre: " + materia;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}	
	}
	
	
	
	// Insertar  
	
	@PostMapping("/insertar/{materia}/{activo}")
	public ResponseEntity<?> createTiposExpedienteActivo(@PathVariable String materia, @RequestParam(required = false) Integer activo) {
		Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
		if (tipoExpediente.isPresent()) {
			String mensaje = "Ya existe un tipo de expediente con el nombre: " + materia;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}else {
			if (activo == null) activo = 1;
			
			TiposExpedienteModel nuevoTipo = new TiposExpedienteModel(materia, activo);
			TiposExpedienteModel guardarTipo = tiposService.CreateYUpdateTiposExpediente(nuevoTipo);
			return ResponseEntity.ok(guardarTipo);
		}
	}
	
	
	//Update
	
	@PutMapping("/actualizar/{materia}/{materiaNueva}/{activo}")
	public ResponseEntity<?> actualizarTiposExpediente(@PathVariable String materia, @PathVariable String materiaNueva, @RequestParam(required = false) Integer activo) {
		Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
		Optional<TiposExpedienteModel> tipoExpedienteNuevo = tiposService.findByMateria(materiaNueva);
		int tipoExpedienteDatos;
		int tipoExpedienteNuevoDatos;
		
		
		if (tipoExpediente.isPresent() && tipoExpedienteNuevo.isPresent()) {
			tipoExpedienteDatos = tipoExpediente.get().getId();
			tipoExpedienteNuevoDatos = tipoExpedienteNuevo.get().getId();
		}else {
			tipoExpedienteDatos = tipoExpediente.map(TiposExpedienteModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
		    tipoExpedienteNuevoDatos = tipoExpedienteNuevo.map(TiposExpedienteModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
		}
		
		if (tipoExpediente.isPresent() && !tipoExpedienteNuevo.isPresent() || 
				tipoExpedienteDatos == tipoExpedienteNuevoDatos) {
			
			if (activo == null) activo = 1;
			
			TiposExpedienteModel tipoExpedienteActualizado = tipoExpediente.get();
			tipoExpedienteActualizado.setMateria(materiaNueva);
			tipoExpedienteActualizado.setActivo(activo);
			TiposExpedienteModel guardarTipo = tiposService.CreateYUpdateTiposExpediente(tipoExpedienteActualizado);
			return ResponseEntity.ok(guardarTipo);
			
		}else if (!tipoExpediente.isPresent()){
			String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			
			}else{
				String mensaje = "Ya existe un tipo de expediente con el nombre: " + materiaNueva;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
	}
	
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{materia}")
		public ResponseEntity<?> borradoLogicoTiposExpediente(@PathVariable String materia) {
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			
			if (tipoExpediente.isPresent()) {
				
				TiposExpedienteModel tipoExpedienteBorrarLogico = tipoExpediente.get();
				tipoExpedienteBorrarLogico.setActivo(0);
				TiposExpedienteModel guardarTipo = tiposService.CreateYUpdateTiposExpediente(tipoExpedienteBorrarLogico);
				return ResponseEntity.ok(guardarTipo);
				
			}else{
				String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borradofisico/{materia}")
		public ResponseEntity<?> borradoFisicoTiposExpediente(@PathVariable String materia){
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			
			if (tipoExpediente.isPresent()) {
				tiposService.DeleteTiposExpediente(materia);
				return ResponseEntity.ok("Materia borrada");
			} else {
				return ResponseEntity.ok("No existe ninguna materia con el nombre " + materia);
			}
		}
		
	// --------------------------------------------------------------	
		
	// ESTA PARTE ES SOLO PARA QUE FUNCIONE EL ANGULAR YA QUE NO NOS PERMITE USAR PUT/DELETE TENMOS QUE USAR POST	
		
	// --------------------------------------------------------------	
		
		@Hidden
		@PostMapping("/actualizar/{materia}/{materiaNueva}/{activo}")
		public ResponseEntity<?> actualizarTiposExpedientePOST(@PathVariable String materia, @PathVariable String materiaNueva, @RequestParam(required = false) Integer activo) {
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			Optional<TiposExpedienteModel> tipoExpedienteNuevo = tiposService.findByMateria(materiaNueva);
			int tipoExpedienteDatos;
			int tipoExpedienteNuevoDatos;
			
			
			if (tipoExpediente.isPresent() && tipoExpedienteNuevo.isPresent()) {
				tipoExpedienteDatos = tipoExpediente.get().getId();
				tipoExpedienteNuevoDatos = tipoExpedienteNuevo.get().getId();
			}else {
				tipoExpedienteDatos = tipoExpediente.map(TiposExpedienteModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
			    tipoExpedienteNuevoDatos = tipoExpedienteNuevo.map(TiposExpedienteModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
			}
			
			if (tipoExpediente.isPresent() && !tipoExpedienteNuevo.isPresent() || 
					tipoExpedienteDatos == tipoExpedienteNuevoDatos) {
				
				if (activo == null) activo = 1;
				
				TiposExpedienteModel tipoExpedienteActualizado = tipoExpediente.get();
				tipoExpedienteActualizado.setMateria(materiaNueva);
				tipoExpedienteActualizado.setActivo(activo);
				TiposExpedienteModel guardarTipo = tiposService.CreateYUpdateTiposExpediente(tipoExpedienteActualizado);
				return ResponseEntity.ok(guardarTipo);
				
			}else if (!tipoExpediente.isPresent()){
				String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				
				}else{
					String mensaje = "Ya existe un tipo de expediente con el nombre: " + materiaNueva;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
		}
		
		
		
		
		//Borrado 
				
		
			//Borrado Logico
			@Hidden
			@PostMapping("/borrarlogico/{materia}")
			public ResponseEntity<?> borradoLogicoTiposExpedientePOST(@PathVariable String materia) {
				Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
				
				if (tipoExpediente.isPresent()) {
					
					TiposExpedienteModel tipoExpedienteBorrarLogico = tipoExpediente.get();
					tipoExpedienteBorrarLogico.setActivo(0);
					TiposExpedienteModel guardarTipo = tiposService.CreateYUpdateTiposExpediente(tipoExpedienteBorrarLogico);
					return ResponseEntity.ok(guardarTipo);
					
				}else{
					String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
		@Hidden
		@PostMapping("/borradofisico/{materia}")
		public ResponseEntity<?> borradoFisicoTiposExpedientePOST(@PathVariable String materia){
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			
			if (tipoExpediente.isPresent()) {
				tiposService.DeleteTiposExpediente(materia);
				return ResponseEntity.ok("Materia borrada");
			} else {
				return ResponseEntity.ok("No existe ninguna materia con el nombre " + materia);
			}
		}
	
		 
}
