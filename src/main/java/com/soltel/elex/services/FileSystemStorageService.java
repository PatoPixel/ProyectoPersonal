package com.soltel.elex.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.soltel.elex.repositories.Storage;

import jakarta.annotation.PostConstruct;

@Service
public class FileSystemStorageService implements Storage {
	
	
	@Value("${media.location}")
	private String mediaLocation;
	
	private Path rootLocation;
	
	@Override
	@PostConstruct
	public void init() throws IOException {
		rootLocation = Paths.get(mediaLocation);
		Files.createDirectories(rootLocation);
	}

	@Override
	public String store(MultipartFile file) {
		
	try {
		if(file.isEmpty()) {
			throw new RuntimeException("Fallo al almacenar archivo vacío");
			}
			
		String filename = file.getOriginalFilename();
		Path destinationFile = rootLocation.resolve(Paths.get(filename))
				.normalize().toAbsolutePath();
		
		try(InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
		}
		
		return filename;
			
		} catch (IOException e) {
			throw new RuntimeException("Fallo al almacenar el archivio", e);
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		
	try {	
		Path file = rootLocation.resolve(filename);
		Resource resource = new UrlResource((file.toUri()));
		
		if (resource.exists() || resource.isReadable()) {
			return resource;
		} else {
			throw new RuntimeException("No se pudo leer el fichero: " + filename);
		}
	} catch (MalformedURLException e) {
		throw new RuntimeException("No se pudo leer el fichero: " + filename);
	}

	}
	
	
	@Override
    public boolean delete(MultipartFile filename) {
        try {
        	String filename2 = filename.getOriginalFilename();
            Path file = rootLocation.resolve(Paths.get(filename2));
            Files.delete(file);
            return true; // La eliminación fue exitosa
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error al eliminar el archivo
        }
    }
}