package com.soltel.elex.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MiConfiguracionCors implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Digo cuales van a ser los endpoint mapeados
        registry.addMapping("/tiposexpediente/**")
        	.allowedOrigins("http://localhost:4200")
        	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
        	.allowedHeaders("Content-Type", "Authorization")
            .allowCredentials(true);
        
        registry.addMapping("/expedientes/**")
        	.allowedOrigins("http://localhost:4200")
        	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
        	.allowedHeaders("Content-Type", "Authorization")
            .allowCredentials(true);
        
        registry.addMapping("/actuaciones/**")
    	.allowedOrigins("http://localhost:4200")
    	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
    	.allowedHeaders("Content-Type", "Authorization")
        .allowCredentials(true);
    
        registry.addMapping("/documentos/**")
    	.allowedOrigins("http://localhost:4200")
    	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
    	.allowedHeaders("Content-Type", "Authorization")
        .allowCredentials(true);
        registry.addMapping("/media/**")
    	.allowedOrigins("http://localhost:4200")
    	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
    	.allowedHeaders("Content-Type", "Authorization")
        .allowCredentials(true);
    	
    }
}
