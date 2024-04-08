package com.soltel.elex.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.soltel.elex.models.DocumentosModel;

@Repository
public interface IDocumentosRepository extends JpaRepository<DocumentosModel, Integer> {

	Optional<DocumentosModel> findByRuta(String ruta);
	
	
    void deleteByRuta(String ruta);
    
}
