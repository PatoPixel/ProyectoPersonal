package com.soltel.elex.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.models.Situacion;
import com.soltel.elex.models.TiposExpedienteModel;
import com.soltel.elex.services.ExpedientesService;
import com.soltel.elex.services.TiposExpedienteService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/expedientes")
public class ExpedientesController {
		

	private final ExpedientesService expedienteService;
	private final TiposExpedienteService tiposService;
	
	public ExpedientesController (ExpedientesService expedienteService, TiposExpedienteService tiposService) {
		this.expedienteService = expedienteService;
		this.tiposService = tiposService;
	}
	
	
	
	//Consultar
	
	
		//FindALL
	
	@GetMapping("/consultar")
	public ResponseEntity<List<ExpedientesModel>> getAllExpedientes(){
		return ResponseEntity.ok(expedienteService.findAllExpedientes());
	}
	
	
	 	//FinByCodigo
	@GetMapping("/consultar/{Codigo}")
	public ResponseEntity<?> getByCodigo(@PathVariable String Codigo) {
		Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
		if (expediente.isPresent()) {
			return ResponseEntity.ok(expediente.get());
		}else {
			String mensaje = "No se encontró ningun expediente con código: " + Codigo;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}	
	}
	
	
	
	// Insertar  
	
	@Operation(summary = "En la Ubicacion usar '_' luego se cambiara a '/'", description = "No se puede enviar una ubiacicon con '/' asi que usamos '_'.")
	@PostMapping("/insertar/{Codigo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}/{activo}")
	public ResponseEntity<?> createTiposExpediente
	(@PathVariable String Codigo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
	@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia, @PathVariable int activo) {
		
		Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
		if (expediente.isPresent()) {
			String mensaje = "Ya existe un expediente con codigo: " + Codigo;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}else {
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			if (!tipoExpediente.isPresent()) {
				String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}else {
				
			TiposExpedienteModel tipo = tipoExpediente.get();
			ExpedientesModel nuevoExpediente = new ExpedientesModel(Codigo, fecha, situacion, opciones, descripcion, prioridad, ubicacion.replace("_", "/"), tipo, activo);
			ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(nuevoExpediente);
			return ResponseEntity.ok(guardarExpediente);
			
			}
		}
	}
	
	
	//Update
	@Operation(summary = "En la Ubicacion usar '_' luego se cambiara a '/'", description = "No se puede enviar una ubiacicon con '/' asi que usamos '_'.")
	@PutMapping("/actualizar/{Codigo}/{CodigoNuevo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}/{activo}")
	public ResponseEntity<?> actualizarTiposExpedienteActivo
	(@PathVariable String Codigo, @PathVariable String CodigoNuevo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
	@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia, @PathVariable int activo) {
				
		Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
		Optional<ExpedientesModel> expedienteNuevo = expedienteService.findByCodigo(CodigoNuevo);
		int expedienteDatos;
		int expedienteNuevoDatos;
		
		
		if (expediente.isPresent() && expedienteNuevo.isPresent()) {
			expedienteDatos = expediente.get().getId();
			expedienteNuevoDatos = expedienteNuevo.get().getId();
		}else {
			expedienteDatos = expediente.map(ExpedientesModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
		    expedienteNuevoDatos = expedienteNuevo.map(ExpedientesModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
		}
		
		if (expediente.isPresent() && !expedienteNuevo.isPresent() ||
				expedienteDatos == expedienteNuevoDatos) {
			
			Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
			if (!tipoExpediente.isPresent()) {
				String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}else {
					
					TiposExpedienteModel tipo = tipoExpediente.get();
					ExpedientesModel expedienteActualizado = expediente.get();
					expedienteActualizado.setCodigo(CodigoNuevo);
					expedienteActualizado.setFecha(fecha);
					expedienteActualizado.setSituacion(situacion);
					expedienteActualizado.setOpciones(opciones);
					expedienteActualizado.setDescripcion(descripcion);	
					expedienteActualizado.setPrioridad(prioridad);
					expedienteActualizado.setUbicacion(ubicacion.replace("_", "/"));
					expedienteActualizado.setTipoExpediente(tipo);
					expedienteActualizado.setActivo(activo);
					ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteActualizado);
					return ResponseEntity.ok(guardarExpediente);
			}
		}else if (!expediente.isPresent()){
			String mensaje = "No existe un expediente con código: " + Codigo;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			
			}else{
				String mensaje = "Ya existe un expediente con código: " + CodigoNuevo;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{Codigo}")
		public ResponseEntity<?> borradoLogicoTiposExpediente(@PathVariable String Codigo) {
			Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
			
			if (expediente.isPresent()) {
				
				ExpedientesModel expedienteBorrarLogico = expediente.get();
				expedienteBorrarLogico.setActivo(0);
				ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteBorrarLogico);
				return ResponseEntity.ok(guardarExpediente);
				
			}else{
				String mensaje = "No existe un expediente con código: " + Codigo;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		// Revertir borrado logico
		@PutMapping("/activar/{Codigo}")
		public ResponseEntity<?> activarTiposExpediente(@PathVariable String Codigo) {
			Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
			
			if (expediente.isPresent()) {
				
				ExpedientesModel expedienteBorrarLogico = expediente.get();
				expedienteBorrarLogico.setActivo(1);
				ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteBorrarLogico);
				return ResponseEntity.ok(guardarExpediente);
				
			}else{
				String mensaje = "No existe un expediente con código: " + Codigo;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borradofisico/{Codigo}")
		public ResponseEntity<?> borradoFisicoTiposExpediente(@PathVariable String Codigo){
			Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
			
			if (expediente.isPresent()) {
				expedienteService.DeleteTiposExpediente(Codigo);
				return ResponseEntity.ok("Expediente borrado");
			} else {
				return ResponseEntity.ok("No existe ningún expediente con código: " + Codigo);
			}
		}
		
		// --------------------------------------------------------------	
		
		// ESTA PARTE ES SOLO PARA QUE FUNCIONE EL ANGULAR YA QUE NO NOS PERMITE USAR PUT/DELETE TENMOS QUE USAR POST	
			
		// --------------------------------------------------------------
		
		//Update

		@Hidden
		@PostMapping("/actualizar/{Codigo}/{CodigoNuevo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}/{activo}")
		public ResponseEntity<?> actualizarTiposExpedienteActivoPOST
		(@PathVariable String Codigo, @PathVariable String CodigoNuevo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
		@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia, @PathVariable int activo) {
					
			Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
			Optional<ExpedientesModel> expedienteNuevo = expedienteService.findByCodigo(CodigoNuevo);
			int expedienteDatos;
			int expedienteNuevoDatos;
			
			
			if (expediente.isPresent() && expedienteNuevo.isPresent()) {
				expedienteDatos = expediente.get().getId();
				expedienteNuevoDatos = expedienteNuevo.get().getId();
			}else {
				expedienteDatos = expediente.map(ExpedientesModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
			    expedienteNuevoDatos = expedienteNuevo.map(ExpedientesModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
			}
			
			if (expediente.isPresent() && !expedienteNuevo.isPresent() ||
					expedienteDatos == expedienteNuevoDatos) {
				
				Optional<TiposExpedienteModel> tipoExpediente = tiposService.findByMateria(materia);
				if (!tipoExpediente.isPresent()) {
					String mensaje = "No existe un tipo de expediente con el nombre: " + materia;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
					}else {
						
						TiposExpedienteModel tipo = tipoExpediente.get();
						ExpedientesModel expedienteActualizado = expediente.get();
						expedienteActualizado.setCodigo(CodigoNuevo);
						expedienteActualizado.setFecha(fecha);
						expedienteActualizado.setSituacion(situacion);
						expedienteActualizado.setOpciones(opciones);
						expedienteActualizado.setDescripcion(descripcion);	
						expedienteActualizado.setPrioridad(prioridad);
						expedienteActualizado.setUbicacion(ubicacion.replace("_", "/"));
						expedienteActualizado.setTipoExpediente(tipo);
						expedienteActualizado.setActivo(activo);
						ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteActualizado);
						return ResponseEntity.ok(guardarExpediente);
				}
			}else if (!expediente.isPresent()){
				String mensaje = "No existe un expediente con código: " + Codigo;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				
				}else{
					String mensaje = "Ya existe un expediente con código: " + CodigoNuevo;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
		
		
		
		
		//Borrado 
				
		
			//Borrado Logico
			@Hidden
			@PostMapping("/borrarlogico/{Codigo}")
			public ResponseEntity<?> borradoLogicoTiposExpedientePOST(@PathVariable String Codigo) {
				Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
				
				if (expediente.isPresent()) {
					
					ExpedientesModel expedienteBorrarLogico = expediente.get();
					expedienteBorrarLogico.setActivo(0);
					ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteBorrarLogico);
					return ResponseEntity.ok(guardarExpediente);
					
				}else{
					String mensaje = "No existe un expediente con código: " + Codigo;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
			//Borrado Logico
			@Hidden
			@PostMapping("/activar/{Codigo}")
			public ResponseEntity<?> activarTiposExpedientePOST(@PathVariable String Codigo) {
				Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
				
				if (expediente.isPresent()) {
					
					ExpedientesModel expedienteBorrarLogico = expediente.get();
					expedienteBorrarLogico.setActivo(1);
					ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(expedienteBorrarLogico);
					return ResponseEntity.ok(guardarExpediente);
					
				}else{
					String mensaje = "No existe un expediente con código: " + Codigo;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
			
			//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
			@Hidden 
			@PostMapping("/borradofisico/{Codigo}")
			public ResponseEntity<?> borradoFisicoTiposExpedientePOST(@PathVariable String Codigo){
				Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
				
				if (expediente.isPresent()) {
					expedienteService.DeleteTiposExpediente(Codigo);
					return ResponseEntity.ok("Expediente borrado");
				} else {
					return ResponseEntity.ok("No existe ningún expediente con código: " + Codigo);
				}
			}
			
			
			
	
		 
}
