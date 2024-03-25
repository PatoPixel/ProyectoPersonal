package com.soltel.elex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soltel.elex.models.DocumentosModel;

@Repository
public interface IDocumentosModel extends JpaRepository<DocumentosModel, Integer> {

}
