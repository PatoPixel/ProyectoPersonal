package com.soltel.elex.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.TiposExpedienteModel;

@Repository
public interface ITiposExpedienteRepository extends JpaRepository<TiposExpedienteModel, Integer>{
	
	Optional<TiposExpedienteModel> findByMateria(String materia);
	
}
