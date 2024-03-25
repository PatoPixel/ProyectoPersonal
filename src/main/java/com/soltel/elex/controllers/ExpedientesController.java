package com.soltel.elex.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.services.ExpedientesService;

@RestController
@RequestMapping("/expedientes")
public class ExpedientesController {
		
	private final ExpedientesService expedientesService;
	
	public ExpedientesController (ExpedientesService expedientesService) {
		this.expedientesService = expedientesService;
	}
	
	@GetMapping("/consultar")
	public ResponseEntity<List<ExpedientesModel>> getAllTipos(){
		return ResponseEntity.ok(expedientesService.findAllTipos());
	}
	
}
