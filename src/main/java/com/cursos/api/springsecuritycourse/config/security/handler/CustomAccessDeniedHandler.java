package com.cursos.api.springsecuritycourse.config.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.cursos.api.springsecuritycourse.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ApiError apiError = new ApiError();
		apiError.setBackendMessage(accessDeniedException.getLocalizedMessage());
		apiError.setUrl(request.getRequestURL().toString());
		apiError.setMethod(request.getMethod());
		apiError.setTimestamp(LocalDateTime.now());
		apiError.setMessage("Acesso denegado. No tienes los permisos necesarios para acceder a esta funcion. Por favor, contacta al administrador si crees que esto es un error.");
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		
		//creamos el objeto para mapear nuestro ApiError
		ObjectMapper objetM = new ObjectMapper();
		// agregamos el modulo de LocalDataTime a la libreria de Jackson
		objetM.registerModule(new JavaTimeModule());
		// transformamos ese dto java a String Json.
		String ApiErrorAsJson = objetM.writeValueAsString(apiError);
		// enviamos ese json en la respuesta		
		response.getWriter().write(ApiErrorAsJson);
		

	}

}
