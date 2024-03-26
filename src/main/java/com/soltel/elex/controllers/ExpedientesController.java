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
import org.springframework.web.bind.annotation.RestController;

import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.models.Situacion;
import com.soltel.elex.models.TiposExpedienteModel;
import com.soltel.elex.services.ExpedientesService;
import com.soltel.elex.services.TiposExpedienteService;

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
	public ResponseEntity<List<ExpedientesModel>> getAllTipos(){
		return ResponseEntity.ok(expedienteService.findAllTipos());
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
	
	
			//Solo Codigo (Activo valor predeterminado 1)
	
	
	//
	/**
	 * @todo
	 * @param Comprobar que la sesion está iniciada
	 * @return 
	 */
	@PostMapping("/insertar/{Codigo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}")
	public ResponseEntity<?> createTiposExpediente
	(@PathVariable String Codigo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
	@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia) {
		
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
			ExpedientesModel nuevoExpediente = new ExpedientesModel(Codigo, fecha, situacion, opciones, descripcion, prioridad, ubicacion, tipo);
			ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(nuevoExpediente);
			return ResponseEntity.ok(guardarExpediente);
			
			}
		}
	}
	
			//Puedes modificar el activo
	
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
			ExpedientesModel nuevoExpediente = new ExpedientesModel(Codigo, fecha, situacion, opciones, descripcion, prioridad, ubicacion, tipo, activo);
			ExpedientesModel guardarExpediente = expedienteService.CreateYUpdateExpedientes(nuevoExpediente);
			return ResponseEntity.ok(guardarExpediente);
			
			}
		}
	}
	
	
	//Update
	
			//Solo Codigo (Activo valor predeterminado 1)
	
	@PutMapping("/actualizar/{Codigo}/{CodigoNuevo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}")
	public ResponseEntity<?> actualizarTiposExpediente
	(@PathVariable String Codigo, @PathVariable String CodigoNuevo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
	@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia) {
				
		Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
		Optional<ExpedientesModel> expedienteNuevo = expedienteService.findByCodigo(CodigoNuevo);
		
		if (expediente.isPresent() && !expedienteNuevo.isPresent()) {
			
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
					expedienteActualizado.setUbicacion(ubicacion);
					expedienteActualizado.setTipoExpediente(tipo);
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
	
	
	
			//Puedes modificar la Codigo junto con el activo
	@PutMapping("/actualizar/{Codigo}/{CodigoNuevo}/{fecha}/{situacion}/{opciones}/{descripcion}/{prioridad}/{ubicacion}/{materia}/{activo}")
	public ResponseEntity<?> actualizarTiposExpedienteActivo
	(@PathVariable String Codigo, @PathVariable String CodigoNuevo, @PathVariable LocalDate fecha, @PathVariable Situacion situacion, @PathVariable String opciones,
	@PathVariable String descripcion, @PathVariable String prioridad, @PathVariable String ubicacion, @PathVariable String materia, @PathVariable int activo) {
				
		Optional<ExpedientesModel> expediente = expedienteService.findByCodigo(Codigo);
		Optional<ExpedientesModel> expedienteNuevo = expedienteService.findByCodigo(CodigoNuevo);
		
		if (expediente.isPresent() && !expedienteNuevo.isPresent()) {
			
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
					expedienteActualizado.setUbicacion(ubicacion);
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
	
		 
}
