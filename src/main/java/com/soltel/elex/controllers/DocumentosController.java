package com.soltel.elex.controllers;

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

import com.soltel.elex.models.DocumentosModel;
import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.services.DocumentosService;
import com.soltel.elex.services.ExpedientesService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/documentos")
public class DocumentosController {
	
	private final DocumentosService documentoService;	
	private final ExpedientesService expedienteService;
	
	
	
	public DocumentosController (DocumentosService documentoService, ExpedientesService expedienteService) {
		this.documentoService = documentoService;
		this.expedienteService = expedienteService;
	}
	
	
	
	//Consultar
	
	
		//FindALL
	
	@GetMapping("/consultar")
	public ResponseEntity<List<DocumentosModel>> getAllDocumentos(){
		return ResponseEntity.ok(documentoService.findAllDocumentos());
	}
	
	
	 	//FinByruta
	@GetMapping("/consultar/{ruta}")
	public ResponseEntity<?> getByRuta(@PathVariable String ruta) {
	    Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
	    if (documento.isPresent()) {
	        return ResponseEntity.ok(documento.get());
	    } else {
	        String mensaje = "No se encontró ningún documento con ruta: " + ruta.replace("_", "/");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
	    }
	}
	
	
	
	// Insertar  

	@PostMapping("/insertar/{ruta}/{tarifa}/{categoria}/{expediente}/{activo}")
	public ResponseEntity<?> createTiposdocumento
	(@PathVariable String ruta, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @PathVariable int activo) {
		
		if (tarifa >= 0) {
		Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
		if (documento.isPresent()) {
			String mensaje = "Ya existe un documento con ruta: " + ruta.replace("_", "/");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}else {
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			if (!expedienteCodigo.isPresent()) {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}else {
				
				ExpedientesModel expedienteCompleto = expedienteCodigo.get();
				DocumentosModel nuevodocumento = new DocumentosModel(ruta.replace("_", "/"), tarifa, categoria, expedienteCompleto, activo);
				DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(nuevodocumento);
				return ResponseEntity.ok(guardardocumento);
				}
			}
		}else {
			String mensaje = "La tarifa no puede ser un numero negativo";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}
		
	}
	
	
	//Update

	@PutMapping("/actualizar/{ruta}/{rutaNueva}/{tarifa}/{categoria}/{expediente}/{activo}")
	public ResponseEntity<?> actualizarTiposdocumento
	(@PathVariable String ruta,@PathVariable String rutaNueva, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @PathVariable int activo) {
		if (tarifa >= 0) {		
		Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
		Optional<DocumentosModel> documentoNuevo = documentoService.findByRuta(rutaNueva.replace("_", "/"));
		int documentoDatos;
		int documentoNuevoDatos;
		
		if (documento.isPresent() && documentoNuevo.isPresent()) {
			documentoDatos = documento.get().getId();
			documentoNuevoDatos = documentoNuevo.get().getId();
		}else {
			documentoDatos = documento.map(DocumentosModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
		    documentoNuevoDatos = documentoNuevo.map(DocumentosModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
		}
		if (documento.isPresent() && !documentoNuevo.isPresent() ||
				documentoDatos == documentoNuevoDatos) {
			
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			if (!expedienteCodigo.isPresent()) {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}else {
					
					ExpedientesModel expedienteCompleto = expedienteCodigo.get();
					DocumentosModel documentoActualizado = documento.get();
					documentoActualizado.setRuta(rutaNueva.replace("_", "/"));
					documentoActualizado.setTarifa(tarifa);
					documentoActualizado.setCategoria(categoria);
					documentoActualizado.setActivo(activo);
					documentoActualizado.setExpediente(expedienteCompleto);
					DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoActualizado);
					return ResponseEntity.ok(guardardocumento);
			}
		}else if (!documento.isPresent()){
			String mensaje = "No existe un documento con la siguiente ruta: " + ruta;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			
			}else{
				String mensaje = "Ya existe un documento con la siguiente ruta: " + rutaNueva;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}else {
			String mensaje = "La tarifa no puede ser un numero negativo";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}
	}
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{ruta}")
		public ResponseEntity<?> borradoLogicoTiposDocumento(@PathVariable String ruta) {
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
			
			if (documento.isPresent()) {
				
				DocumentosModel documentoBorrarLogico = documento.get();
				documentoBorrarLogico.setActivo(0);
				DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
				return ResponseEntity.ok(guardardocumento);
				
			}else{
				String mensaje = "No existe un documento con ruta: " + ruta.replace("_", "/");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borradofisico/{ruta}")
		public ResponseEntity<?> borradoFisicoTiposdocumento(@PathVariable String ruta){
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
			
			if (documento.isPresent()) {
				documentoService.DeleteDocumentos(ruta.replace("_", "/"));
				return ResponseEntity.ok("Documento borrado");
			} else {
				return ResponseEntity.ok("No existe ningún documento con ruta: " + ruta.replace("_", "/"));
			}
		}
		@PutMapping("/activar/{ruta}")
		public ResponseEntity<?> activarDocumento(@PathVariable String ruta) {
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
			
			if (documento.isPresent()) {
				
				DocumentosModel documentoBorrarLogico = documento.get();
				documentoBorrarLogico.setActivo(1);
				DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
				return ResponseEntity.ok(guardardocumento);
				
			}else{
				String mensaje = "No existe un documento con ruta: " + ruta.replace("_", "/");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
	
		// --------------------------------------------------------------	
		
		// ESTA PARTE ES SOLO PARA QUE FUNCIONE EL ANGULAR YA QUE NO NOS PERMITE USAR PUT/DELETE TENMOS QUE USAR POST	
					
		// --------------------------------------------------------------
		
		
		//Update
		@Hidden
		@PostMapping("/actualizar/{ruta}/{rutaNueva}/{tarifa}/{categoria}/{expediente}/{activo}")
		public ResponseEntity<?> actualizarTiposdocumentoPOST
		(@PathVariable String ruta,@PathVariable String rutaNueva, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @PathVariable int activo) {
			
			
			if (tarifa >= 0){
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
			Optional<DocumentosModel> documentoNuevo = documentoService.findByRuta(rutaNueva.replace("_", "/"));
			int documentoDatos;
			int documentoNuevoDatos;
			
			if (documento.isPresent() && documentoNuevo.isPresent()) {
				documentoDatos = documento.get().getId();
				documentoNuevoDatos = documentoNuevo.get().getId();
			}else {
				documentoDatos = documento.map(DocumentosModel::getId).orElse(0); // Asigna 0 si el expediente está vacío
			    documentoNuevoDatos = documentoNuevo.map(DocumentosModel::getId).orElse(1); // Asigna 1 si el expedienteNuevo está vacío
			}
			if (documento.isPresent() && !documentoNuevo.isPresent() ||
					documentoDatos == documentoNuevoDatos) {
				
				Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente.replace("_", "/"));
				if (!expedienteCodigo.isPresent()) {
					String mensaje = "No existe un expediente con codigo: " + expediente;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
					}else {
						
						ExpedientesModel expedienteCompleto = expedienteCodigo.get();
						DocumentosModel documentoActualizado = documento.get();
						documentoActualizado.setRuta(rutaNueva.replace("_", "/"));
						documentoActualizado.setTarifa(tarifa);
						documentoActualizado.setCategoria(categoria);
						documentoActualizado.setActivo(activo);
						documentoActualizado.setExpediente(expedienteCompleto);
						DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoActualizado);
						return ResponseEntity.ok(guardardocumento);
				}
			}else if (!documento.isPresent()){
				String mensaje = "No existe un documento con la siguiente ruta: " + ruta;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				
				}else{
					String mensaje = "Ya existe un documento con la siguiente ruta: " + rutaNueva;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}else {
				String mensaje = "La tarifa no puede ser un numero negativo";
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
}
		
		
		
		
		//Borrado 
				
		
			//Borrado Logico
			@Hidden
			@PostMapping("/borrarlogico/{ruta}")
			public ResponseEntity<?> borradoLogicoTiposDocumentoPOST(@PathVariable String ruta) {
				Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
				
				if (documento.isPresent()) {
					
					DocumentosModel documentoBorrarLogico = documento.get();
					documentoBorrarLogico.setActivo(0);
					DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
					return ResponseEntity.ok(guardardocumento);
					
				}else{
					String mensaje = "No existe un documento con ruta: " + ruta.replace("_", "/");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			@Hidden
			@PostMapping("/activar/{ruta}")
			public ResponseEntity<?> activarDocumentoPOST(@PathVariable String ruta) {
				Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
				
				if (documento.isPresent()) {
					
					DocumentosModel documentoBorrarLogico = documento.get();
					documentoBorrarLogico.setActivo(1);
					DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
					return ResponseEntity.ok(guardardocumento);
					
				}else{
					String mensaje = "No existe un documento con ruta: " + ruta.replace("_", "/");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
			//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
			@Hidden
			@PostMapping("/borradofisico/{ruta}")
			public ResponseEntity<?> borradoFisicoTiposdocumentoPOST(@PathVariable String ruta){
				Optional<DocumentosModel> documento = documentoService.findByRuta(ruta.replace("_", "/"));
				
				if (documento.isPresent()) {
					documentoService.DeleteDocumentos(ruta.replace("_", "/"));
					return ResponseEntity.ok("Documento borrado");
				} else {
					return ResponseEntity.ok("No existe ningún documento con ruta: " + ruta.replace("_", "/"));
				}
			}
		
}

