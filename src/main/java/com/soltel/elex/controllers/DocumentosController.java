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
	public ResponseEntity<?> getByRuta(@RequestParam("ruta") String ruta) {
	    Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
	    if (documento.isPresent()) {
	        return ResponseEntity.ok(documento.get());
	    } else {
	        String mensaje = "No se encontró ningún documento con ruta: " + ruta;
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
	    }
	}
	
	
	
	// Insertar  

	@PostMapping("/insertar/{ruta}/{tarifa}/{categoria}/{expediente}/{activo}")
	public ResponseEntity<?> createTiposdocumento
	(@RequestParam("ruta") String ruta, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @RequestParam(required = false) Integer activo) {
		
		Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
		if (documento.isPresent()) {
			String mensaje = "Ya existe un documento con ruta: " + ruta;
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
		}else {
			Optional<ExpedientesModel> expedienteCodigo = expedienteService.findByCodigo(expediente);
			if (!expedienteCodigo.isPresent()) {
				String mensaje = "No existe un expediente con codigo: " + expediente;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}else {
				if (activo == null) activo = 1;

				ExpedientesModel expedienteCompleto = expedienteCodigo.get();
				DocumentosModel nuevodocumento = new DocumentosModel(ruta, tarifa, categoria, expedienteCompleto, activo);
				DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(nuevodocumento);
				return ResponseEntity.ok(guardardocumento);
				
			}
		}
	}
	
	
	//Update

	@PutMapping("/actualizar/{ruta}/{rutaNueva}/{tarifa}/{categoria}/{expediente}/{activo}")
	public ResponseEntity<?> actualizarTiposdocumento
	(@RequestParam("ruta") String ruta,@RequestParam("rutaNueva") String rutaNueva, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @RequestParam(required = false) Integer activo) {
				
		Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
		Optional<DocumentosModel> documentoNuevo = documentoService.findByRuta(rutaNueva);
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
					if (activo == null) activo = 1;
					
					ExpedientesModel expedienteCompleto = expedienteCodigo.get();
					DocumentosModel documentoActualizado = documento.get();
					documentoActualizado.setRuta(rutaNueva);
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
		}
	
	
	
	
	//Borrado 
			
	
		//Borrado Logico
	
		@PutMapping("/borrarlogico/{ruta}")
		public ResponseEntity<?> borradoLogicoTiposDocumento(@RequestParam("ruta") String ruta) {
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
			
			if (documento.isPresent()) {
				
				DocumentosModel documentoBorrarLogico = documento.get();
				documentoBorrarLogico.setActivo(0);
				DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
				return ResponseEntity.ok(guardardocumento);
				
			}else{
				String mensaje = "No existe un documento con ruta: " + ruta;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
			}
		}
		
		//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
		
		@DeleteMapping("/borradofisico/{ruta}")
		public ResponseEntity<?> borradoFisicoTiposdocumento(@RequestParam("ruta") String ruta){
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
			
			if (documento.isPresent()) {
				documentoService.DeleteDocumentos(ruta);
				return ResponseEntity.ok("Documento borrado");
			} else {
				return ResponseEntity.ok("No existe ningún documento con ruta: " + ruta);
			}
		}
	
		// --------------------------------------------------------------	
		
		// ESTA PARTE ES SOLO PARA QUE FUNCIONE EL ANGULAR YA QUE NO NOS PERMITE USAR PUT/DELETE TENMOS QUE USAR POST	
					
		// --------------------------------------------------------------
		
		
		//Update

		@PostMapping("/actualizar/{ruta}/{rutaNueva}/{tarifa}/{categoria}/{expediente}/{activo}")
		public ResponseEntity<?> actualizarTiposdocumentoPOST
		(@RequestParam("ruta") String ruta,@RequestParam("rutaNueva") String rutaNueva, @PathVariable float tarifa, @PathVariable String categoria, @PathVariable String expediente, @RequestParam(required = false) Integer activo) {
					
			Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
			Optional<DocumentosModel> documentoNuevo = documentoService.findByRuta(rutaNueva);
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
						if (activo == null) activo = 1;
						
						ExpedientesModel expedienteCompleto = expedienteCodigo.get();
						DocumentosModel documentoActualizado = documento.get();
						documentoActualizado.setRuta(rutaNueva);
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
			}
		
		
		
		
		//Borrado 
				
		
			//Borrado Logico
		
			@PostMapping("/borrarlogico/{ruta}")
			public ResponseEntity<?> borradoLogicoTiposDocumentoPOST(@RequestParam("ruta") String ruta) {
				Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
				
				if (documento.isPresent()) {
					
					DocumentosModel documentoBorrarLogico = documento.get();
					documentoBorrarLogico.setActivo(0);
					DocumentosModel guardardocumento = documentoService.CreateYUpdateDocumentos(documentoBorrarLogico);
					return ResponseEntity.ok(guardardocumento);
					
				}else{
					String mensaje = "No existe un documento con ruta: " + ruta;
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
				}
			}
			
			//Borrado Fisico NO ES RECOMENDADO USARLO PARA ESTO, BORRARA TODO LO RELACIONADO QUE TENGA
			
			@PostMapping("/borradofisico/{ruta}")
			public ResponseEntity<?> borradoFisicoTiposdocumentoPOST(@RequestParam("ruta") String ruta){
				Optional<DocumentosModel> documento = documentoService.findByRuta(ruta);
				
				if (documento.isPresent()) {
					documentoService.DeleteDocumentos(ruta);
					return ResponseEntity.ok("Documento borrado");
				} else {
					return ResponseEntity.ok("No existe ningún documento con ruta: " + ruta);
				}
			}
		
}

