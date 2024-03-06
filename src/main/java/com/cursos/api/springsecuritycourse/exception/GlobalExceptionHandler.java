package com.cursos.api.springsecuritycourse.exception;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cursos.api.springsecuritycourse.dto.ApiError;


// controla excepciones
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handlerGenericException(HttpServletRequest request, Exception exception) {
		ApiError apiError = new ApiError();
		apiError.setBackendMessage(exception.getLocalizedMessage());
		apiError.setUrl(request.getRequestURL().toString());
		apiError.setMethod(request.getMethod());
		apiError.setTimestamp(LocalDate.now());
		apiError.setMessage("Error interno en el servidor, comuniquese con sistemas");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handlerMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {
		ApiError apiError = new ApiError();
		apiError.setBackendMessage(exception.getLocalizedMessage());
		apiError.setUrl(request.getRequestURL().toString());
		apiError.setMethod(request.getMethod());
		apiError.setTimestamp(LocalDate.now());
		apiError.setSubmessages(exception.getAllErrors().stream().map(each -> each.getDefaultMessage()).collect(Collectors.toList()));
		apiError.setMessage("Error en la peticion enviada");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
		
	}

}
