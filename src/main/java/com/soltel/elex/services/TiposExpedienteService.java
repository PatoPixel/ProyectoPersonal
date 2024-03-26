package com.soltel.elex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soltel.elex.repositories.ITiposExpedienteRepository;
import com.soltel.elex.models.TiposExpedienteModel;

@Service
public class TiposExpedienteService {
	private final ITiposExpedienteRepository tiposRepository;
	
	@Autowired
	public TiposExpedienteService(ITiposExpedienteRepository tiposRepository)
	{
		this.tiposRepository = tiposRepository;
	}
	
	//FindAll
	
	public List<TiposExpedienteModel> findAllTipos(){
		return tiposRepository.findAll();
	}
	
	//FindPorMateria
	
	public Optional<TiposExpedienteModel> findByMateria(String materia){
		return tiposRepository.findByMateria(materia);
	}
	
	
	//Create and Update
	public TiposExpedienteModel CreateYUpdateTiposExpediente(TiposExpedienteModel tipo){
		return tiposRepository.save(tipo);
	}
	
	
	//Delete
	@Transactional
	public void DeleteTiposExpediente (String materia) {
		tiposRepository.deleteByMateria(materia);
	}
}	
