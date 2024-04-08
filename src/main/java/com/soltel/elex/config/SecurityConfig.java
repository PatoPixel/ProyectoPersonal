package com.soltel.elex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // vamos a levantar la mano para el acceso
    // Evitamos problemas con swagger y el front (Angular)
	@Bean
	public SecurityFilterChain filtradoSeguridad(HttpSecurity http) throws Exception {
	    http
	        .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/tiposexpediente/**", "/expedientes/**", "/actuaciones/**", "/documentos/**", "/media/**").permitAll()
	            .anyRequest().authenticated())
	        .formLogin(form -> form.
	        		defaultSuccessUrl("/swagger-ui/index.html")); // Eliminar la configuración de redirección
	    return http.build();
	}

    // Elegimos el encriptado de la contraseña
    @Bean
    public PasswordEncoder encriptado() {
        return new BCryptPasswordEncoder();
    }

    // Para definir credenciales de acceso
    @Bean
    public UserDetailsService credenciales(PasswordEncoder encriptado) {
        UserDetails usuarioPrincipal = User.builder()
                .username("soltel")
                .password(encriptado.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(usuarioPrincipal);
    }

}