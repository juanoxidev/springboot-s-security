package com.cursos.api.springsecuritycourse.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// Crea componentes y los configura por default. Ej el authentication configuration que se usa para devolver el authentication manager por defecto (provider manager)
@EnableWebSecurity
public class HttpSecurityConfig {

	// devolvemos un security filterchain que es construido gracias a un builder
	// HttpSecurity : permite gestionar/personalziar como se van a gestionar y proteger las solicitudes HTTP, agregar permisoss o roles a las rutas, 
	//cuales rutas son publicas o privadas.
	
	@Autowired
	private AuthenticationProvider daoAuthProvider;
	
	@Bean 
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		
		SecurityFilterChain filterChain = http
				
	
		// crosssiterequestforgerin no lo utiliamos, esta basado en tokens y se usa cuando hay objetos de tipo sesion, esta basado en tokens que se intercambian entre las peticiones.
		.csrf(csrfConfig -> csrfConfig.disable())
		// indicamos que tipo de manejo de session vamos a tener.
		.sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		// indicamos que tipo de autentificacion vamos a usar
		.authenticationProvider(daoAuthProvider)
		.authorizeHttpRequests(authReqConfig -> {
			/*
			 * Endpoints publicos
			 */
			authReqConfig.requestMatchers(HttpMethod.POST,"/customers").permitAll();
			authReqConfig.requestMatchers(HttpMethod.POST,"/auth/authenticate").permitAll();
			authReqConfig.requestMatchers(HttpMethod.GET,"/auth/validate-token").permitAll();
			/*
			 * Todos los demas endpoints deben ser con usuario logueado
			 */
			authReqConfig.anyRequest().authenticated();
		})
		.build();
		
		return filterChain;
		
	}
}
