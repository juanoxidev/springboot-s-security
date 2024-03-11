package com.cursos.api.springsecuritycourse.config.security.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cursos.api.springsecuritycourse.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		ApiError apiError = new ApiError();
		apiError.setBackendMessage(authException.getLocalizedMessage());
		apiError.setUrl(request.getRequestURL().toString());
		apiError.setMethod(request.getMethod());
		apiError.setTimestamp(LocalDateTime.now());
		apiError.setMessage("No se encontraron credenciales de autenticacion. Por favor, inicie sesión para acceder a esta función.");
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		//creamos el objeto para mapear nuestro ApiError
		ObjectMapper objetM = new ObjectMapper();
		// agregamos el modulo de LocalDataTime a la libreria de Jackson
		objetM.registerModule(new JavaTimeModule());
		// transformamos ese dto java a String Json.
		String ApiErrorAsJson = objetM.writeValueAsString(apiError);
		// enviamos ese json en la respuesta		
		response.getWriter().write(ApiErrorAsJson);
		
		/*
		 * 
		 * Si desemos que el authenticationEntryPoint sea lo mismo que el AccessDeniedHandler 
		 * 
		 *- Inyectamos el  AccessDeniedHandler con @Autowired
		 *	@Autowired
		 *	private AccessDeniedHandler accessDeniedHandler;
		 *	 
		 *	y dentro del metodo commence hacemos
		 *			@Override
		 *  public void commence(HttpServletRequest request, HttpServletResponse response,
		 *	AuthenticationException authException) throws IOException, ServletException {
		 *  
		 *  	accessDeniedHandler.handle(request,response, AccessDeniedException("Access Denied"))
		 *	
		 *	}
		 */
	
	}

}
