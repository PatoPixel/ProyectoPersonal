package com.soltel.elex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soltel.elex.models.ActuacionesModel;
import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.repositories.IActuacionesRepository;

@Service
public class ActuacionesService {
private final IActuacionesRepository actuacionesRepository;
	
	@Autowired
	public ActuacionesService(IActuacionesRepository actuacionesRepository)
	{
	this.actuacionesRepository = actuacionesRepository;
	}

	//FindAll

		public List<ActuacionesModel> findAllActuaciones(){
			return actuacionesRepository.findAll();
	}

	//FindPorCodigo

		public Optional<ActuacionesModel> findByDescripcionAndExpediente(String descripcion, ExpedientesModel expediente){
			return actuacionesRepository.findByDescripcionAndExpediente(descripcion, expediente);
	}

	//Create and Update
		public ActuacionesModel CreateYUpdateActuaciones(ActuacionesModel documento){
			return actuacionesRepository.save(documento);
	}
		
	//Delete
		@Transactional
		public void DeleteDocumentos (String descripcion, ExpedientesModel expediente) {
			actuacionesRepository.deleteByDescripcionAndExpediente(descripcion, expediente);
	}
}
