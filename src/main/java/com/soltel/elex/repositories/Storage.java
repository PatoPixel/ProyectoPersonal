package com.soltel.elex.repositories;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface Storage {
	
    void init() throws IOException;
    
    String store(MultipartFile file);
    
    Resource loadAsResource(String filename);
    
    boolean delete(MultipartFile filename);
}
