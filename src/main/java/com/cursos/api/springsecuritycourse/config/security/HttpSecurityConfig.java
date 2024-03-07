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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cursos.api.springsecuritycourse.config.security.filter.JwtAuthenticationFilter;

import util.Role;

@Configuration
// Crea componentes y los configura por default. Ej el authentication configuration que se usa para devolver el authentication manager por defecto (provider manager)
@EnableWebSecurity
public class HttpSecurityConfig {

	// devolvemos un security filterchain que es construido gracias a un builder
	// HttpSecurity : permite gestionar/personalziar como se van a gestionar y proteger las solicitudes HTTP, agregar permisoss o roles a las rutas, 
	//cuales rutas son publicas o privadas.
	
	@Autowired
	private AuthenticationProvider daoAuthProvider;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean 
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
			
		SecurityFilterChain securityFilterChain = http
		// crosssiterequestforgerin no lo utiliamos, esta basado en tokens y se usa cuando hay objetos de tipo sesion, esta basado en tokens que se intercambian entre las peticiones.
		.csrf(csrfConfig -> csrfConfig.disable())
		// indicamos que tipo de manejo de session vamos a tener.
		.sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		// indicamos que tipo de autentificacion vamos a usar
		.authenticationProvider(daoAuthProvider)
		/* .addFilterBefore : Agregar filtro antes de que se ejecute el UsernamePasswordAuthenticationFilter.class
		 * se deja como argumento al UsernamePasswordAuthenticationFilter.class solo para que de un peso anterior a 1900
		 * A + peso tenga el filtro mas tarde se ejecuta, solo indica cuando se va a ejecutar el filtro que creamos.
		*/
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.authorizeHttpRequests(authReqConfig -> {
			/*
			 * Endpoints publicos
			 */
			authReqConfig.antMatchers(HttpMethod.POST,"/customers").permitAll();
			authReqConfig.antMatchers(HttpMethod.POST,"/auth/authenticate").permitAll();
			authReqConfig.antMatchers(HttpMethod.GET,"/auth/validate-token").permitAll();
			/*
			 * Todos los demas endpoints deben ser con usuario logueado
			 */
			authReqConfig.anyRequest().authenticated();
		})
		.build();
		
		return securityFilterChain;
		
	}
}
