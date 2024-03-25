package com.soltel.elex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.ExpedientesModel;

@Repository
public interface IExpedientesRepository extends JpaRepository<ExpedientesModel, Integer> {

}
