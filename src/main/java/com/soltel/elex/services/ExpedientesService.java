package com.soltel.elex.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
