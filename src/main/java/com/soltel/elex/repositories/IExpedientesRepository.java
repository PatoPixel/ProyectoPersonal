package com.soltel.elex.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.ExpedientesModel;
import com.soltel.elex.models.TiposExpedienteModel;

@Repository
public interface IExpedientesRepository extends JpaRepository<ExpedientesModel, Integer> {
	
	Optional<ExpedientesModel> findByCodigo(String codigo);
	
	
    void deleteByCodigo(String codigo);
}
