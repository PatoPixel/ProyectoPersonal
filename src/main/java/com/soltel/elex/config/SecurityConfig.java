package com.soltel.elex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	// Segui 
	// https://www.baeldung.com/java-spring-fix-403-error#:~:text=Causes%20of%20Error%20403,resulting%20in%20a%20403%20error.
	// Para poder resolver el problema de falta de permisos (forbidden: 403)
	@Bean
	public SecurityFilterChain filtro (HttpSecurity http) throws Exception{
		
		http
		.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/index.html")
				.hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.defaultSuccessUrl("/swagger-ui/index.html", true));	
		return http.build();
	}
	
	
	@Bean
	public PasswordEncoder codificador(){
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	@Bean
	public UserDetailsService credenciales (PasswordEncoder codificador) {
		UserDetails usuarioAdmin = User.builder()
				.username("soltel")
				.password(codificador.encode("admin"))
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(usuarioAdmin);
	}
}
