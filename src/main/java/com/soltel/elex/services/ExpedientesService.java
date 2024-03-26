package com.soltel.elex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soltel.elex.repositories.IExpedientesRepository;
import com.soltel.elex.models.ExpedientesModel;

@Service
public class ExpedientesService {
private final IExpedientesRepository expedientesRepository;
	
	@Autowired
	public ExpedientesService(IExpedientesRepository expedientesRepository)
	{
	this.expedientesRepository = expedientesRepository;
	}

	//FindAll

		public List<ExpedientesModel> findAllTipos(){
			return expedientesRepository.findAll();
	}

	//FindPorCodigo

		public Optional<ExpedientesModel> findByCodigo(String codigo){
			return expedientesRepository.findByCodigo(codigo);
	}

	//Create and Update
		public ExpedientesModel CreateYUpdateExpedientes(ExpedientesModel expediente){
			return expedientesRepository.save(expediente);
	}
		
	//Delete
		@Transactional
		public void DeleteTiposExpediente (String codigo) {
			expedientesRepository.deleteByCodigo(codigo);
	}
}
