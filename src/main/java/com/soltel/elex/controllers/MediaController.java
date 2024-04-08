package com.soltel.elex.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.soltel.elex.repositories.Storage;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Hidden
@PermitAll
@RestController
@RequestMapping("media")
@AllArgsConstructor
public class MediaController {

	private final Storage storageService;
	private final HttpServletRequest request;

	@PermitAll
	@PostMapping("upload")
	public Map<String, String> uploadFile(@RequestParam("file")MultipartFile multipartFile){
		String path = storageService.store(multipartFile);
		String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		String url = ServletUriComponentsBuilder
				.fromHttpUrl(host)
				.path("/media/")
				.path(path)
				.toUriString();

		return Map.of("url", url);
	}

	@PermitAll
	@GetMapping("{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException{
		Resource file = storageService.loadAsResource(filename);
		String contentType = Files.probeContentType(file.getFile().toPath());
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(file);
	}
	
	@Hidden
    @PostMapping("delete")
    public ResponseEntity<?> deleteFile(@RequestParam("file")MultipartFile filename) {
        // Lógica para eliminar el archivo utilizando el servicio de almacenamiento
        boolean deleted = storageService.delete(filename);
        if (deleted) {
            return ResponseEntity.ok().build(); // Retorna 200 OK si se elimina correctamente
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found si el archivo no se encuentra
        }
    }
	
}
