package com.soltel.elex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soltel.elex.models.DocumentosModel;
import com.soltel.elex.repositories.IDocumentosRepository;

@Service
public class DocumentosService {
	private final IDocumentosRepository documentosRepository;
	
	@Autowired
	public DocumentosService(IDocumentosRepository documentosRepository)
	{
	this.documentosRepository = documentosRepository;
	}

	//FindAll

		public List<DocumentosModel> findAllDocumentos(){
			return documentosRepository.findAll();
	}

	//FindPorCodigo

		public Optional<DocumentosModel> findByRuta(String ruta){
			return documentosRepository.findByRuta(ruta);
	}

	//Create and Update
		public DocumentosModel CreateYUpdateDocumentos(DocumentosModel documento){
			return documentosRepository.save(documento);
	}
		
	//Delete
		@Transactional
		public void DeleteDocumentos (String ruta) {
			documentosRepository.deleteByRuta(ruta);
	}
}

