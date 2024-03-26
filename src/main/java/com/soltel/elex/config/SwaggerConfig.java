package com.soltel.elex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class SwaggerConfig {
	@Configuration
	public class OpenApiConfig {
		@Bean
		public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Endpoints Elex")
					.description("Documentación de la API" )
					.version("V1. 0"))
				.components(new Components()
					.addSecuritySchemes("bearer-jwt" ,
							new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
					)
				);
		}
	}
}
