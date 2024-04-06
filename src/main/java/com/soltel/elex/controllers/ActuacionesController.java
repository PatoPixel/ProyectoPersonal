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

import com.soltel.elex.models.ActuacionesModel;
import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.services.ActuacionesService;
import com.soltel.elex.services.ExpedientesService;

@RestController
@RequestMapping("/actuaciones")
public class ActuacionesController {
	private final ActuacionesService actuacionService;	
	private final ExpedientesService expedienteService;
	
	
	
	public ActuacionesController (ActuacionesService actuacionService, ExpedientesService expedienteService) {
		this.actuacionService = actuacionService;
		this.expedienteService = expedienteService;
	}
	
	
	
	//Consultar
	
	
		//FindALL
	
	@GetMapping("/consultar")
	public ResponseEntity<List<ActuacionesModel>> getAllActuaciones(){
		return ResponseEntity.ok(actuacionService.findAllActuaciones());
	}
	
	
	 	//FinByDescripcionAndExpediente
	@GetMapping("/consultar/{descripcion}/{expediente}")
	public ResponseEntity<?> getByDescripcionAndExpediente(@PathVariable String descripcion, @PathVariable String expediente) {
		
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
	
		if (expedienteCodigo.isPresent()) {
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			if (actuacion.isPresent()) {
	        return ResponseEntity.ok(actuacion.get());
	        
	    } else {
	        String mensaje = "No se encontró ninguna actuación con descripcion:'" + descripcion + "' en el expediente: " + expediente;
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
	    }
	}else {
		String mensaje = "No se encontró ningún expediente con codigo: " + expediente;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
	}
}
	
	
	
	// Insertar  

	
	@PostMapping("/insertar/{descripcion}/{finalizado}/{modalidad}/{fecha}/{expediente}/{activo}")
	public ResponseEntity<?> CreateActuacion
	(@PathVariable String descripcion, @PathVariable int finalizado, @PathVariable LocalDate fecha,@PathVariable String modalidad, @PathVariable String expediente, @RequestParam(required = false) Integer activo) {
		
		
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
		
		if (expedienteCodigo.isPresent()) {
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			
			if (actuacion.isPresent()) {
				String mensaje = "Ya existe una actuación con descripcion:'" + descripcion + "' en el expediente: " + expediente;
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		        
				} else {
					if (activo == null) activo = 1;

						ActuacionesModel nuevaActuacion = new ActuacionesModel(descripcion, finalizado, modalidad, fecha, expedienteCompleto, activo);							
						ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(nuevaActuacion);
						return ResponseEntity.ok(guardaractuacion);
					}
			
			} else {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}

	
	//Update
	
	@PutMapping("/actualizar/{descripcion}/{descripcionNueva}/{expediente}/{expedienteNuevo}/{finalizado}/{modalidad}/{fecha}/{activo}")
	public ResponseEntity<?> UpdateActuaciones
	(@PathVariable String descripcion, @PathVariable String descripcionNueva, @PathVariable String expediente,
	@PathVariable String expedienteNuevo,@PathVariable int finalizado, @PathVariable LocalDate fecha,
	@PathVariable String modalidad, @RequestParam(required = false) Integer activo) {
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
		Optional<ExpedientesModel> expedienteCodigoNuevo = expedienteService.findByCodigo(expedienteNuevo);
		
		if (expedienteCodigo.isPresent() && expedienteCodigoNuevo.isPresent()) {
			
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			ExpedientesModel expedienteCompletoNuevo = expedienteCodigoNuevo.get();
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			Optional<ActuacionesModel> actuacionNueva = actuacionService.findByDescripcionAndExpediente(descripcionNueva, expedienteCompletoNuevo);
			int actuacionDatos;
			int actuacionNuevaDatos;
			
			if (actuacion.isPresent() && actuacionNueva.isPresent()) {
				actuacionDatos = actuacion.get().getId();
				actuacionNuevaDatos = actuacionNueva.get().getId();
			}else {
				actuacionDatos = actuacion.map(ActuacionesModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
			    actuacionNuevaDatos = actuacionNueva.map(ActuacionesModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
			}
			if (actuacion.isPresent() && !actuacionNueva.isPresent() ||
					actuacionDatos == actuacionNuevaDatos) {
				
					ActuacionesModel actuacionActualizado = actuacion.get();
					
					if (activo == null) activo = 1;

					
					actuacionActualizado.setDescripcion(descripcionNueva);
					actuacionActualizado.setExpediente(expedienteCompletoNuevo);
					actuacionActualizado.setFinalizado(finalizado);
					actuacionActualizado.setModalidad(modalidad);
					actuacionActualizado.setActivo(activo);
					actuacionActualizado.setExpediente(expedienteCompletoNuevo);
					ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(actuacionActualizado);
					return ResponseEntity.ok(guardaractuacion);
					
				}if (!actuacion.isPresent()) {
					String mensaje = "No existe la descripcion: '" + descripcion + "' para el expediente: " + expediente;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
					}else {
						String mensaje = "Ya existe la descripcion: '" + descripcionNueva + "' para el expediente: " + expedienteNuevo;
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
					}
			}else if(!expedienteCodigo.isPresent()){
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			
		}else {
			String mensaje = "No existe un expediente con codigo: " + expedienteNuevo;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}
	}


	
	
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{descripcion}/{expediente}")
		public ResponseEntity<?> borradoLogicoActuacion(@PathVariable String descripcion, @PathVariable String expediente) {
			
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			
			if (expedienteCodigo.isPresent()) {
				ExpedientesModel expedienteCompleto = expedienteCodigo.get();
				Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			
				if (actuacion.isPresent()) {
				
					ActuacionesModel actuacionBorrarLogico = actuacion.get();
					actuacionBorrarLogico.setActivo(0);
					ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(actuacionBorrarLogico);
					return ResponseEntity.ok(guardaractuacion);
				
				}else{
					String mensaje = "No existe una actuacion con descripcion: '" + descripcion + "' y expediente: " + expediente;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}else {
				String mensaje = "No existe un expediente: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borrarfisico/{descripcion}/{expediente}")
		public ResponseEntity<?> borradoFisicoActuacion(@PathVariable String descripcion, @PathVariable String expediente){
			
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			
			if (expedienteCodigo.isPresent()) {
				ExpedientesModel expedienteCompleto = expedienteCodigo.get();
				Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			
				if (actuacion.isPresent()) {
					actuacionService.DeleteActuaciones(descripcion, expedienteCompleto);
					return ResponseEntity.ok("Actuación borrada");
				} else {
					return ResponseEntity.ok("No existe una actuacion con descripcion: '" + descripcion + "' y expediente: " + expediente);
				}
			} else {
				return ResponseEntity.ok("No existe un expediente: " + expediente);
			}
		}

		
		// --------------------------------------------------------------	
		
		// ESTA PARTE ES SOLO PARA QUE FUNCIONE EL ANGULAR YA QUE NO NOS PERMITE USAR PUT/DELETE TENMOS QUE USAR POST	
					
		// --------------------------------------------------------------
		
		//Update
		
		@PostMapping("/actualizar/{descripcion}/{descripcionNueva}/{expediente}/{expedienteNuevo}/{finalizado}/{modalidad}/{fecha}/{activo}")
		public ResponseEntity<?> UpdateActuacionesPOST
		(@PathVariable String descripcion, @PathVariable String descripcionNueva, @PathVariable String expediente,
		@PathVariable String expedienteNuevo,@PathVariable int finalizado, @PathVariable LocalDate fecha,
		@PathVariable String modalidad, @RequestParam(required = false) Integer activo) {
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			Optional<ExpedientesModel> expedienteCodigoNuevo = expedienteService.findByCodigo(expedienteNuevo);
			
			if (expedienteCodigo.isPresent() && expedienteCodigoNuevo.isPresent()) {
				
				ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
				ExpedientesModel expedienteCompletoNuevo = expedienteCodigoNuevo.get();
				Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
				Optional<ActuacionesModel> actuacionNueva = actuacionService.findByDescripcionAndExpediente(descripcionNueva, expedienteCompletoNuevo);
				int actuacionDatos;
				int actuacionNuevaDatos;
				
				if (actuacion.isPresent() && actuacionNueva.isPresent()) {
					actuacionDatos = actuacion.get().getId();
					actuacionNuevaDatos = actuacionNueva.get().getId();
				}else {
					actuacionDatos = actuacion.map(ActuacionesModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
				    actuacionNuevaDatos = actuacionNueva.map(ActuacionesModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
				}
				if (actuacion.isPresent() && !actuacionNueva.isPresent() ||
						actuacionDatos == actuacionNuevaDatos) {
					
						ActuacionesModel actuacionActualizado = actuacion.get();
						
						if (activo == null) activo = 1;

						
						actuacionActualizado.setDescripcion(descripcionNueva);
						actuacionActualizado.setExpediente(expedienteCompletoNuevo);
						actuacionActualizado.setFinalizado(finalizado);
						actuacionActualizado.setModalidad(modalidad);
						actuacionActualizado.setActivo(activo);
						actuacionActualizado.setExpediente(expedienteCompletoNuevo);
						ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(actuacionActualizado);
						return ResponseEntity.ok(guardaractuacion);
						
					}if (!actuacion.isPresent()) {
						String mensaje = "No existe la descripcion: '" + descripcion + "' para el expediente: " + expediente;
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
						}else {
							String mensaje = "Ya existe la descripcion: '" + descripcionNueva + "' para el expediente: " + expedienteNuevo;
							return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
						}
				}else if(!expedienteCodigo.isPresent()){
					String mensaje = "No existe un expediente con codigo: " + expediente;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				
			}else {
				String mensaje = "No existe un expediente con codigo: " + expedienteNuevo;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}


		
		
		
		
		
		//Borrado 
				
		
			//Borrado Logico
		
			@PostMapping("/borrarlogico/{descripcion}/{expediente}")
			public ResponseEntity<?> borradoLogicoActuacionPOST(@PathVariable String descripcion, @PathVariable String expediente) {
				
				Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
				
				if (expedienteCodigo.isPresent()) {
					ExpedientesModel expedienteCompleto = expedienteCodigo.get();
					Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
				
					if (actuacion.isPresent()) {
					
						ActuacionesModel actuacionBorrarLogico = actuacion.get();
						actuacionBorrarLogico.setActivo(0);
						ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(actuacionBorrarLogico);
						return ResponseEntity.ok(guardaractuacion);
					
					}else{
						String mensaje = "No existe una actuacion con descripcion: '" + descripcion + "' y expediente: " + expediente;
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
					}
				}else {
					String mensaje = "No existe un expediente: " + expediente;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
			//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
			
			@PostMapping("/borrarfisico/{descripcion}/{expediente}")
			public ResponseEntity<?> borradoFisicoActuacionPOST(@PathVariable String descripcion, @PathVariable String expediente){
				
				Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
				
				if (expedienteCodigo.isPresent()) {
					ExpedientesModel expedienteCompleto = expedienteCodigo.get();
					Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
				
					if (actuacion.isPresent()) {
						actuacionService.DeleteActuaciones(descripcion, expedienteCompleto);
						return ResponseEntity.ok("Actuación borrada");
					} else {
						return ResponseEntity.ok("No existe una actuacion con descripcion: '" + descripcion + "' y expediente: " + expediente);
					}
				} else {
					return ResponseEntity.ok("No existe un expediente: " + expediente);
				}
			}
		
			
}
		
