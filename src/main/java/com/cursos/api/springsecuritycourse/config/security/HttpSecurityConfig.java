package com.cursos.api.springsecuritycourse.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cursos.api.springsecuritycourse.config.security.filter.JwtAuthenticationFilter;

import util.Role;
import util.RolePermission;

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
		
			buildRequestMatchers(authReqConfig);
		})
		.build();
		
		return securityFilterChain;
		
	}
	
	
	// AUTORIZACION DE SOLICITUDES HTTP REQUEST SEGUN ROLE .HASROLE() .HASANYROLE()
	private void buildRequestMatchers(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
		/*
		 * Autorizacion de endpoints de products
		 * 
		 */
		
		authReqConfig.antMatchers(HttpMethod.GET, "/products")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.GET, "/products/{productId}")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.POST, "/products")
		.hasRole(Role.ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.PUT, "/products/{productId}")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
		.hasRole(Role.ADMINISTRATOR.name());
		
		/*
		 * Autorizacion de endpoints de categorias
		 * 
		 */
		
		authReqConfig.antMatchers(HttpMethod.GET, "/categories")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.GET, "/categories/{categoryId}")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.POST, "/categories")
		.hasRole(Role.ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.PUT, "/categories/{categoryId}")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
		
		authReqConfig.antMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
		.hasRole(Role.ADMINISTRATOR.name());
		
		/*
		 * Autorizacion de endpoint profile
		 * 
		 */
		
		authReqConfig.antMatchers(HttpMethod.GET, "/auth/profile")
		.hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.CUSTOMER.name());
		
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
	}


//	AUTORIZACION DE SOLICITUDES HTTP REQUEST SEGUN AUTHORITIES .HASAUTHORITY()
//	private void buildRequestMatchers(
//			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
//		/*
//		 * Autorizacion de endpoints de products
//		 * 
//		 */
//		
//		authReqConfig.antMatchers(HttpMethod.GET, "/products")
//		.hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());
//		
//		authReqConfig.antMatchers(HttpMethod.GET, "/products/{productId}")
//		.hasAuthority(RolePermission.READ_ONE_PRODUCT.name());
//		
//		authReqConfig.antMatchers(HttpMethod.POST, "/products")
//		.hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());
//		
//		authReqConfig.antMatchers(HttpMethod.PUT, "/products/{productId}")
//		.hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());
//		
//		authReqConfig.antMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
//		.hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());
//		
//		/*
//		 * Autorizacion de endpoints de categorias
//		 * 
//		 */
//		
//		authReqConfig.antMatchers(HttpMethod.GET, "/categories")
//		.hasAuthority(RolePermission.READ_ALL_CATEGORIES.name());
//		
//		authReqConfig.antMatchers(HttpMethod.GET, "/categories/{categoryId}")
//		.hasAuthority(RolePermission.READ_ONE_CATEGORY.name());
//		
//		authReqConfig.antMatchers(HttpMethod.POST, "/categories")
//		.hasAuthority(RolePermission.CREATE_ONE_CATEGORY.name());
//		
//		authReqConfig.antMatchers(HttpMethod.PUT, "/categories/{categoryId}")
//		.hasAuthority(RolePermission.UPDATE_ONE_CATEGORY.name());
//		
//		authReqConfig.antMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
//		.hasAuthority(RolePermission.DISABLE_ONE_CATEGORY.name());
//		
//		/*
//		 * Autorizacion de endpoint profile
//		 * 
//		 */
//		
//		authReqConfig.antMatchers(HttpMethod.GET, "/auth/profile")
//		.hasAuthority(RolePermission.READ_MY_PROFILE.name());
//		
//		/*
//		 * Endpoints publicos
//		 */
//		authReqConfig.antMatchers(HttpMethod.POST,"/customers").permitAll();
//		authReqConfig.antMatchers(HttpMethod.POST,"/auth/authenticate").permitAll();
//		authReqConfig.antMatchers(HttpMethod.GET,"/auth/validate-token").permitAll();
//		
//		/*
//		 * Todos los demas endpoints deben ser con usuario logueado
//		 */
//		authReqConfig.anyRequest().authenticated();
//	}
     
}
