package com.soltel.elex.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.ActuacionesModel;
import com.soltel.elex.models.ExpedientesModel;


@Repository
public interface IActuacionesRepository extends JpaRepository<ActuacionesModel, Integer> {
	
	@Query("SELECT a FROM ActuacionesModel a WHERE a.descripcion = :descripcion AND a.expediente = :expediente")
    Optional<ActuacionesModel> findByDescripcionAndExpediente(String descripcion, ExpedientesModel expediente);
	
	 void deleteByDescripcionAndExpediente(String descripcion, ExpedientesModel expediente);
}
