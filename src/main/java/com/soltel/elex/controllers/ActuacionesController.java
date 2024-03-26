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
	
	
			//Solo ruta (Activo valor predeterminado 1)
	
	
	//
	/**
	 * @todo
	 * @param Comprobar que la sesion está iniciada
	 * @return 
	 */
	@PostMapping("/insertar/{descripcion}/{finalizado}/{modalidad}/{fecha}/{expediente}")
	public ResponseEntity<?> CreateActuacion
	(@PathVariable String descripcion, @PathVariable int finalizado, @PathVariable LocalDate fecha,@PathVariable String modalidad, @PathVariable String expediente) {
		
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
		
		if (expedienteCodigo.isPresent()) {
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			
			if (actuacion.isPresent()) {
				String mensaje = "Ya existe una actuación con descripcion:'" + descripcion + "' en el expediente: " + expediente;
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		        
				} else {
						ActuacionesModel nuevaActuacion = new ActuacionesModel(descripcion, finalizado, modalidad, fecha, expedienteCompleto);
						ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateActuaciones(nuevaActuacion);
						return ResponseEntity.ok(guardaractuacion);
					}
			
			} else {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}
	}
	
			//Puedes modificar el activo
	
	@PostMapping("/insertar/{descripcion}/{finalizado}/{modalidad}/{fecha}/{expediente}/{activo}")
	public ResponseEntity<?> CreateActuacion
	(@PathVariable String descripcion, @PathVariable int finalizado, @PathVariable LocalDate fecha,@PathVariable String modalidad, @PathVariable String expediente, @PathVariable int activo) {
		
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
		
		if (expedienteCodigo.isPresent()) {
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			
			if (actuacion.isPresent()) {
				String mensaje = "Ya existe una actuación con descripcion:'" + descripcion + "' en el expediente: " + expediente;
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		        
				} else {
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
	
			//Solo ruta (Activo valor predeterminado 1)
	
	@PutMapping("/actualizar/{descripcion}/{descripcionNueva}/{expediente}/{expedienteNuevo}/{finalizado}/{modalidad}/{fecha}")
	public ResponseEntity<?> UpdateActuaciones
	(@PathVariable String descripcion, @PathVariable String descripcionNueva, @PathVariable String expediente, @PathVariable String expedienteNuevo,@PathVariable int finalizado, @PathVariable LocalDate fecha,@PathVariable String modalidad) {
		Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
		Optional<ExpedientesModel> expedienteCodigoNuevo = expedienteService.findByCodigo(expedienteNuevo);
		
		if (expedienteCodigo.isPresent() && expedienteCodigoNuevo.isPresent()) {
			ExpedientesModel expedienteCompleto = expedienteCodigo.get(); 
			ExpedientesModel expedienteCompletoNuevo = expedienteCodigoNuevo.get();
			Optional<ActuacionesModel> actuacion = actuacionService.findByDescripcionAndExpediente(descripcion, expedienteCompleto);
			Optional<ActuacionesModel> actuacionNueva = actuacionService.findByDescripcionAndExpediente(descripcionNueva, expedienteCompletoNuevo);
			
			if (actuacion.isPresent() && !actuacionNueva.isPresent()) {
					ActuacionesModel actuacionActualizado = actuacion.get();
					actuacionActualizado.setDescripcion(descripcionNueva);
					actuacionActualizado.setFinalizado(finalizado);
					actuacionActualizado.setModalidad(modalidad);
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


	
	/*
	
			//Puedes modificar la ruta junto con el activo
	@PutMapping("/actualizar/{ruta}/{rutaNueva}/{tarifa}/{categoria}/{expediente}/{activo}")
	public ResponseEntity<?> actualizarTiposactuacion
	(@RequestParam("ruta") String ruta,@RequestParam("rutaNueva") String rutaNueva, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @PathVariable int activo) {
				
		Optional<ActuacionesModel> actuacion = actuacionService.findByRuta(ruta);
		Optional<ActuacionesModel> actuacionNuevo = actuacionService.findByRuta(rutaNueva);
		
		if (actuacion.isPresent() && !actuacionNuevo.isPresent()) {
			
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			if (!expedienteCodigo.isPresent()) {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}else {
					ExpedientesModel expedienteCompleto = expedienteCodigo.get();
					ActuacionesModel actuacionActualizado = actuacion.get();
					actuacionActualizado.setRuta(rutaNueva);
					actuacionActualizado.setTarifa(tarifa);
					actuacionActualizado.setCategoria(categoria);
					actuacionActualizado.setActivo(activo);
					actuacionActualizado.setExpediente(expedienteCompleto);
					ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateactuacions(actuacionActualizado);
					return ResponseEntity.ok(guardaractuacion);
			}
		}else if (!actuacion.isPresent()){
			String mensaje = "No existe un actuacion con la siguiente ruta: " + ruta;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			
			}else{
				String mensaje = "Ya existe un actuacion con la siguiente ruta: " + rutaNueva;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
	
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{ruta}")
		public ResponseEntity<?> borradoLogicoTiposactuacion(@RequestParam("ruta") String ruta) {
			Optional<ActuacionesModel> actuacion = actuacionService.findByRuta(ruta);
			
			if (actuacion.isPresent()) {
				
				ActuacionesModel actuacionBorrarLogico = actuacion.get();
				actuacionBorrarLogico.setActivo(0);
				ActuacionesModel guardaractuacion = actuacionService.CreateYUpdateactuacions(actuacionBorrarLogico);
				return ResponseEntity.ok(guardaractuacion);
				
			}else{
				String mensaje = "No existe un actuacion con ruta: " + ruta;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borradofisico/{ruta}")
		public ResponseEntity<?> borradoFisicoTiposactuacion(@RequestParam("ruta") String ruta){
			Optional<ActuacionesModel> actuacion = actuacionService.findByRuta(ruta);
			
			if (actuacion.isPresent()) {
				actuacionService.Deleteactuacions(ruta);
				return ResponseEntity.ok("actuacion borrado");
			} else {
				return ResponseEntity.ok("No existe ningún actuacion con ruta: " + ruta);
			}
		}
	*/
}
