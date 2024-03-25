package com.soltel.elex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.ActuacionesModel;

@Repository
public interface IActuacionesRepository extends JpaRepository<ActuacionesModel, Integer> {

}
